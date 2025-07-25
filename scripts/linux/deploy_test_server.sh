#!/bin/bash

# ================================================================
# DÉPLOIEMENT SERVEUR TEST RHEL - SYSTÈME NSIA
# Déploie l'application sur serveur RHEL avec Oracle
# ================================================================

set -euo pipefail

# Configuration
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$(dirname "$SCRIPT_DIR")")"
WAR_NAME="project-management-0.1.0-SNAPSHOT.war"

# Variables par défaut (modifiables via environnement)
PAYARA_HOME="${PAYARA_HOME:-/opt/payara6}"
DOMAIN_NAME="${DOMAIN_NAME:-domain1}"
ORACLE_HOST="${ORACLE_HOST:-localhost}"
ORACLE_PORT="${ORACLE_PORT:-1521}"
ORACLE_SERVICE="${ORACLE_SERVICE:-XE}"
ORACLE_USER="${ORACLE_USER:-project_user}"
ORACLE_PASSWORD="${ORACLE_PASSWORD:-secret}"

# Couleurs
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

log_info() { echo -e "${BLUE}[INFO]${NC} $1"; }
log_success() { echo -e "${GREEN}[SUCCESS]${NC} $1"; }  
log_warn() { echo -e "${YELLOW}[WARNING]${NC} $1"; }
log_error() { echo -e "${RED}[ERROR]${NC} $1" >&2; }

show_usage() {
    cat << EOF
╔════════════════════════════════════════════════════════════════════════════════╗
║                    DÉPLOIEMENT SERVEUR TEST RHEL                              ║
╚════════════════════════════════════════════════════════════════════════════════╝

UTILISATION:
  $0 [OPTIONS]

OPTIONS:
  --oracle-host HOST        Serveur Oracle (défaut: localhost)
  --oracle-port PORT        Port Oracle (défaut: 1521)  
  --oracle-service SERVICE  Service Oracle (défaut: XE)
  --oracle-user USER        Utilisateur Oracle (défaut: project_user)
  --oracle-password PASS    Mot de passe Oracle (requis)
  --payara-home PATH        Chemin Payara (défaut: /opt/payara6)
  --help                    Afficher cette aide

VARIABLES D'ENVIRONNEMENT:
  ORACLE_HOST, ORACLE_PORT, ORACLE_SERVICE, ORACLE_USER, ORACLE_PASSWORD
  PAYARA_HOME, DOMAIN_NAME

EXEMPLE:
  $0 --oracle-host prod-db.company.local --oracle-password secret123

EOF
}

check_prerequisites() {
    log_info "Vérification des prérequis serveur..."
    
    # Java 11
    if ! command -v java &> /dev/null || ! java -version 2>&1 | grep -q "11\."; then
        log_error "Java 11 requis. Utilisez d'abord: sudo ./setup_rhel_server.sh"
        return 1
    fi
    
    # Maven
    if ! command -v mvn &> /dev/null; then
        log_error "Maven requis. Utilisez d'abord: sudo ./setup_rhel_server.sh"
        return 1
    fi
    
    # Payara
    if [ ! -d "$PAYARA_HOME" ] || [ ! -x "$PAYARA_HOME/bin/asadmin" ]; then
        log_error "Payara non trouvé dans $PAYARA_HOME"
        return 1
    fi
    
    # Node.js (pour frontend)
    if ! command -v node &> /dev/null || ! command -v npm &> /dev/null; then
        log_error "Node.js/npm requis. Utilisez d'abord: sudo ./setup_rhel_server.sh"
        return 1
    fi
    
    log_success "Prérequis satisfaits"
    return 0
}

build_application() {
    log_info "Compilation de l'application..."
    
    cd "$PROJECT_DIR"
    
    # Backend
    log_info "Compilation backend Java..."
    mvn clean package -P oracle-test -DskipTests -q
    
    if [ ! -f "target/$WAR_NAME" ]; then
        log_error "Fichier WAR non créé"
        return 1
    fi
    
    # Frontend
    local frontend_dir="$PROJECT_DIR/project-management-frontend"
    if [ -d "$frontend_dir" ]; then
        log_info "Compilation frontend Next.js..."
        cd "$frontend_dir"
        
        if [ ! -d "node_modules" ]; then
            log_info "Installation dépendances npm..."
            npm ci --silent
        fi
        
        npm run build --silent
        
        # Intégration dans le backend
        log_info "Intégration frontend dans le WAR..."
        local static_dir="$PROJECT_DIR/src/main/resources/static"
        mkdir -p "$static_dir"
        
        if [ -d ".next" ]; then
            cp -r .next/* "$static_dir/" 2>/dev/null || true
            cp -r public/* "$static_dir/" 2>/dev/null || true
        fi
        
        cd "$PROJECT_DIR"
        mvn package -P oracle-test -DskipTests -q
    fi
    
    log_success "Compilation terminée"
    return 0
}

setup_payara() {
    log_info "Configuration Payara Server..."
    
    local asladmin="$PAYARA_HOME/bin/asladmin"
    
    # Arrêter domaine si en cours
    $asadmin stop-domain "$DOMAIN_NAME" 2>/dev/null || true
    sleep 3
    
    # Créer domaine si inexistant  
    if ! $asadmin list-domains | grep -q "$DOMAIN_NAME"; then
        log_info "Création du domaine $DOMAIN_NAME..."
        $asadmin create-domain \
            --adminport 4848 \
            --domainproperties "domain.adminPort=4848:domain.instancePort=8080:http.ssl.port=8443" \
            --user admin \
            --passwordfile <(echo "AS_ADMIN_PASSWORD=adminadmin") \
            "$DOMAIN_NAME"
    fi
    
    # Démarrer domaine
    log_info "Démarrage du domaine..."
    $asladmin start-domain "$DOMAIN_NAME"
    
    # Attendre disponibilité
    local attempts=0
    while [ $attempts -lt 30 ]; do
        if $asladmin list-domains | grep -q "$DOMAIN_NAME running"; then
            break
        fi
        sleep 2
        attempts=$((attempts + 1))
    done
    
    if [ $attempts -eq 30 ]; then
        log_error "Domaine n'a pas démarré"
        return 1
    fi
    
    log_success "Payara configuré"
    return 0
}

setup_oracle_datasource() {
    log_info "Configuration datasource Oracle..."
    
    local asladmin="$PAYARA_HOME/bin/asadmin"
    local pool_name="NsiaProjectPool" 
    local jndi_name="jdbc/NsiaProjectDS"
    
    # Supprimer ressources existantes
    $asladmin delete-jdbc-resource "$jndi_name" 2>/dev/null || true
    $asladmin delete-jdbc-resource "${jndi_name}__pm" 2>/dev/null || true
    $asladmin delete-jdbc-connection-pool "$pool_name" 2>/dev/null || true
    
    # Créer pool Oracle
    log_info "Création pool de connexion Oracle..."
    $asladmin create-jdbc-connection-pool \
        --restype javax.sql.DataSource \
        --datasourceclassname oracle.jdbc.pool.OracleDataSource \
        --property "URL=jdbc\\:oracle\\:thin\\:@//${ORACLE_HOST}\\:${ORACLE_PORT}/${ORACLE_SERVICE}:User=${ORACLE_USER}:Password=${ORACLE_PASSWORD}" \
        "$pool_name"
    
    # Test connexion
    log_info "Test de la connexion Oracle..."
    if ! $asladmin ping-connection-pool "$pool_name"; then
        log_error "Impossible de se connecter à Oracle"
        log_error "Vérifiez: Host=$ORACLE_HOST, Port=$ORACLE_PORT, Service=$ORACLE_SERVICE"
        return 1
    fi
    
    # Créer ressources JDBC (double pour Payara)
    $asladmin create-jdbc-resource --connectionpoolid "$pool_name" "$jndi_name"
    $asladmin create-jdbc-resource --connectionpoolid "$pool_name" "${jndi_name}__pm"
    
    log_success "Datasource Oracle configuré"
    return 0
}

deploy_application() {
    log_info "Déploiement de l'application..."
    
    local asladmin="$PAYARA_HOME/bin/asladmin"
    local war_path="$PROJECT_DIR/target/$WAR_NAME"
    local app_name="nsia-project-management"
    
    if [ ! -f "$war_path" ]; then
        log_error "WAR non trouvé: $war_path"
        return 1
    fi
    
    # Désinstaller version existante
    $asladmin undeploy "$app_name" 2>/dev/null || true
    
    # Déployer nouvelle version
    log_info "Déploiement du WAR..."
    $asladmin deploy \
        --name "$app_name" \
        --contextroot "/nsia" \
        --force=true \
        "$war_path"
    
    log_success "Application déployée"
    return 0
}

verify_deployment() {
    log_info "Vérification du déploiement..."
    
    local base_url="http://localhost:8080/nsia"
    local attempts=0
    
    while [ $attempts -lt 20 ]; do
        if curl -s -f "$base_url/actuator/health" > /dev/null 2>&1; then
            log_success "✅ Application accessible à $base_url"
            return 0
        fi
        sleep 3
        attempts=$((attempts + 1))
    done
    
    log_warn "Application peut ne pas être complètement disponible"
    log_info "Vérifiez les logs: $PAYARA_HOME/glassfish/domains/$DOMAIN_NAME/logs/server.log"
    return 0
}

# Traitement des arguments
while [[ $# -gt 0 ]]; do
    case $1 in
        --oracle-host) ORACLE_HOST="$2"; shift 2 ;;
        --oracle-port) ORACLE_PORT="$2"; shift 2 ;;
        --oracle-service) ORACLE_SERVICE="$2"; shift 2 ;;
        --oracle-user) ORACLE_USER="$2"; shift 2 ;;
        --oracle-password) ORACLE_PASSWORD="$2"; shift 2 ;;
        --payara-home) PAYARA_HOME="$2"; shift 2 ;;
        --help) show_usage; exit 0 ;;
        *) log_error "Option inconnue: $1"; show_usage; exit 1 ;;
    esac
done

# Vérification mot de passe Oracle
if [ -z "$ORACLE_PASSWORD" ]; then
    log_error "Mot de passe Oracle requis: --oracle-password ou variable ORACLE_PASSWORD"
    exit 1
fi

# ================================================================
# EXÉCUTION PRINCIPALE
# ================================================================

echo -e "${GREEN}╔════════════════════════════════════════════════════════════════════════════════╗${NC}"
echo -e "${GREEN}║                    DÉPLOIEMENT SERVEUR TEST RHEL                              ║${NC}"
echo -e "${GREEN}╚════════════════════════════════════════════════════════════════════════════════╝${NC}"

log_info "Configuration:"
log_info "  - Oracle: ${ORACLE_HOST}:${ORACLE_PORT}/${ORACLE_SERVICE}"
log_info "  - Utilisateur: ${ORACLE_USER}"
log_info "  - Payara: ${PAYARA_HOME}"

# Exécution séquentielle
check_prerequisites
build_application  
setup_payara
setup_oracle_datasource
deploy_application
verify_deployment

echo -e "${GREEN}╔════════════════════════════════════════════════════════════════════════════════╗${NC}"
echo -e "${GREEN}║                          DÉPLOIEMENT TERMINÉ                                   ║${NC}"
echo -e "${GREEN}╚════════════════════════════════════════════════════════════════════════════════╝${NC}"

log_success "🎉 Application NSIA déployée avec succès!"
log_info "🌐 Application: http://$(hostname):8080/nsia"
log_info "🔧 Admin Payara: http://$(hostname):4848"
log_info "📋 Logs: $PAYARA_HOME/glassfish/domains/$DOMAIN_NAME/logs/server.log"

echo ""
log_info "Pour arrêter l'application: $PAYARA_HOME/bin/asladmin stop-domain $DOMAIN_NAME"