#!/bin/bash

# ================================================================
# CONFIGURATION SERVEUR RHEL - SYSTÈME NSIA
# Configure l'environnement RHEL pour le déploiement
# ================================================================

set -euo pipefail

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

# Vérification root
if [ "$EUID" -ne 0 ]; then
    log_error "Ce script doit être exécuté avec sudo"
    exit 1
fi

# Détection version RHEL
if [ -f /etc/redhat-release ]; then
    RHEL_VERSION=$(grep -oE '[0-9]+' /etc/redhat-release | head -1)
    log_info "RHEL/CentOS version détectée: $RHEL_VERSION"
else
    log_error "Système non supporté. RHEL/CentOS requis."
    exit 1
fi

install_java11() {
    log_info "Installation de Java 11..."
    
    if [ "$RHEL_VERSION" -ge 8 ]; then
        dnf install -y java-11-openjdk-devel
    else
        yum install -y java-11-openjdk-devel
    fi
    
    # Configuration JAVA_HOME
    export JAVA_HOME="/usr/lib/jvm/java-11-openjdk"
    echo "export JAVA_HOME=/usr/lib/jvm/java-11-openjdk" >> /etc/environment
    
    log_success "Java 11 installé"
}

install_maven() {
    log_info "Installation de Maven 3.9.6..."
    
    local MAVEN_VERSION="3.9.6"
    local MAVEN_HOME="/opt/maven"
    
    # Téléchargement
    cd /tmp
    wget -q "https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz"
    
    # Installation
    tar -xzf "apache-maven-${MAVEN_VERSION}-bin.tar.gz"
    mkdir -p "$MAVEN_HOME"
    mv "apache-maven-${MAVEN_VERSION}"/* "$MAVEN_HOME/"
    
    # Configuration
    echo "export M2_HOME=$MAVEN_HOME" >> /etc/environment
    echo "export PATH=\$PATH:\$M2_HOME/bin" >> /etc/environment
    ln -sf "$MAVEN_HOME/bin/mvn" /usr/local/bin/mvn
    
    # Nettoyage
    rm -rf "apache-maven-${MAVEN_VERSION}"*
    
    log_success "Maven installé"
}

install_nodejs() {
    log_info "Installation de Node.js 18..."
    
    # Repository NodeSource
    curl -fsSL https://rpm.nodesource.com/setup_18.x | bash -
    
    if [ "$RHEL_VERSION" -ge 8 ]; then
        dnf install -y nodejs npm
    else
        yum install -y nodejs npm
    fi
    
    log_success "Node.js installé"
}

install_payara() {
    log_info "Installation de Payara Server 6..."
    
    local PAYARA_VERSION="6.2025.3"
    local PAYARA_HOME="/opt/payara6"
    
    # Téléchargement
    cd /tmp
    wget -q "https://repo1.maven.org/maven2/fish/payara/distributions/payara/${PAYARA_VERSION}/payara-${PAYARA_VERSION}.zip"
    
    # Installation
    unzip -q "payara-${PAYARA_VERSION}.zip"
    mkdir -p "/opt"
    mv "payara6" "$PAYARA_HOME"
    
    # Utilisateur payara
    useradd -r -s /bin/false -d "$PAYARA_HOME" payara 2>/dev/null || true
    chown -R payara:payara "$PAYARA_HOME"
    
    # Configuration
    echo "export PAYARA_HOME=$PAYARA_HOME" >> /etc/environment
    echo "export PATH=\$PATH:\$PAYARA_HOME/bin" >> /etc/environment
    ln -sf "$PAYARA_HOME/bin/asadmin" /usr/local/bin/asadmin
    
    # Nettoyage
    rm -rf "payara-${PAYARA_VERSION}"*
    
    log_success "Payara Server installé"
}

install_oracle_client() {
    log_info "Installation du client Oracle..."
    
    # Installation via package manager ou manuel
    if [ "$RHEL_VERSION" -ge 8 ]; then
        dnf install -y oracle-instantclient-basic oracle-instantclient-devel 2>/dev/null || {
            log_warn "Packages Oracle non disponibles via dnf"
            log_info "Les drivers Oracle sont inclus dans l'application"
        }
    else
        yum install -y oracle-instantclient-basic oracle-instantclient-devel 2>/dev/null || {
            log_warn "Packages Oracle non disponibles via yum"  
            log_info "Les drivers Oracle sont inclus dans l'application"
        }
    fi
}

create_payara_service() {
    log_info "Création du service systemd Payara..."
    
    cat > /etc/systemd/system/payara.service << 'EOF'
[Unit]
Description=Payara Server
After=network.target

[Service]
Type=forking
User=payara
Group=payara
Environment=JAVA_HOME=/usr/lib/jvm/java-11-openjdk
Environment=PAYARA_HOME=/opt/payara6
ExecStart=/opt/payara6/bin/asadmin start-domain domain1
ExecReload=/opt/payara6/bin/asadmin restart-domain domain1
ExecStop=/opt/payara6/bin/asadmin stop-domain domain1
TimeoutStartSec=600
TimeoutStopSec=300
Restart=on-failure
RestartSec=10

[Install]
WantedBy=multi-user.target
EOF

    systemctl daemon-reload
    systemctl enable payara
    
    log_success "Service Payara configuré"
}

configure_firewall() {
    log_info "Configuration du firewall..."
    
    if systemctl is-active --quiet firewalld; then
        # Ports de l'application
        firewall-cmd --permanent --add-port=8080/tcp  # HTTP
        firewall-cmd --permanent --add-port=8443/tcp  # HTTPS  
        firewall-cmd --permanent --add-port=4848/tcp  # Admin Payara
        firewall-cmd --reload
        
        log_success "Firewall configuré"
    else
        log_warn "Firewalld non actif - configuration manuelle requise"
    fi
}

install_system_packages() {
    log_info "Installation des packages système..."
    
    if [ "$RHEL_VERSION" -ge 8 ]; then
        dnf install -y curl wget unzip vim htop net-tools lsof
    else
        yum install -y curl wget unzip vim htop net-tools lsof
    fi
    
    log_success "Packages système installés"
}

configure_system_limits() {
    log_info "Configuration des limites système..."
    
    # Limites pour l'utilisateur payara
    cat >> /etc/security/limits.conf << 'EOF'
# Limites NSIA
payara soft nofile 65536
payara hard nofile 65536
payara soft nproc 4096
payara hard nproc 4096
EOF

    # Configuration kernel
    cat >> /etc/sysctl.conf << 'EOF'
# Configuration NSIA
vm.max_map_count=262144
net.core.somaxconn=1024
EOF

    sysctl -p
    
    log_success "Limites système configurées"
}

create_directories() {
    log_info "Création des répertoires..."
    
    mkdir -p /opt/nsia/{logs,backup,config}
    mkdir -p /var/log/nsia
    
    chown -R payara:payara /opt/nsia
    chown -R payara:payara /var/log/nsia
    
    log_success "Répertoires créés"
}

verify_installation() {
    log_info "Vérification de l'installation..."
    
    # Java
    if java -version 2>&1 | grep -q "11\."; then
        log_success "✅ Java 11 OK"
    else
        log_error "❌ Java 11 non configuré"
    fi
    
    # Maven
    if command -v mvn &> /dev/null; then
        log_success "✅ Maven OK"
    else
        log_error "❌ Maven non disponible"
    fi
    
    # Node.js
    if command -v node &> /dev/null && command -v npm &> /dev/null; then
        log_success "✅ Node.js OK"
    else
        log_error "❌ Node.js non disponible"
    fi
    
    # Payara
    if [ -x "/opt/payara6/bin/asadmin" ]; then
        log_success "✅ Payara OK"
    else
        log_error "❌ Payara non installé"
    fi
}

# ================================================================
# EXÉCUTION PRINCIPALE
# ================================================================

echo -e "${GREEN}╔════════════════════════════════════════════════════════════════════════════════╗${NC}"
echo -e "${GREEN}║                    CONFIGURATION SERVEUR RHEL                                 ║${NC}"
echo -e "${GREEN}╚════════════════════════════════════════════════════════════════════════════════╝${NC}"

log_info "Début de la configuration du serveur RHEL pour NSIA..."

install_system_packages
install_java11
install_maven
install_nodejs
install_payara
install_oracle_client
create_payara_service
configure_firewall
configure_system_limits
create_directories
verify_installation

echo -e "${GREEN}╔════════════════════════════════════════════════════════════════════════════════╗${NC}"
echo -e "${GREEN}║                      CONFIGURATION TERMINÉE                                   ║${NC}"
echo -e "${GREEN}╚════════════════════════════════════════════════════════════════════════════════╝${NC}"

log_success "🎉 Serveur RHEL configuré avec succès!"

echo ""
log_info "PROCHAINES ÉTAPES:"
log_info "1. Redémarrez le serveur: sudo reboot"
log_info "2. Copiez le projet sur le serveur"
log_info "3. Lancez le déploiement: ./deploy_test_server.sh --oracle-password VOTRE_PASSWORD"

echo ""
log_info "SERVICES INSTALLÉS:"
log_info "- Java 11: $(java -version 2>&1 | head -1)"
log_info "- Maven: $(mvn --version 2>/dev/null | head -1)"
log_info "- Node.js: $(node --version)"
log_info "- Payara: /opt/payara6"

echo ""
log_info "COMMANDES UTILES:"
log_info "- Démarrer Payara: sudo systemctl start payara"
log_info "- Arrêter Payara: sudo systemctl stop payara"
log_info "- Statut Payara: sudo systemctl status payara"
log_info "- Logs Payara: tail -f /opt/payara6/glassfish/domains/domain1/logs/server.log"