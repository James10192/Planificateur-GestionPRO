# ================================================================
# SCRIPT DE TEST LOCAL WINDOWS - SYSTÈME NSIA
# Lance l'application en local pour visualiser et tester
# ================================================================

param(
    [switch]$Help = $false
)

# Configuration Java 11
$env:JAVA_HOME = "C:\Program Files (x86)\Amazon Corretto\jdk11.0.27_6"
$env:M2_HOME = "C:\Program Files\apache-maven-3.9.6"
$env:PATH = "$env:JAVA_HOME\bin;$env:M2_HOME\bin;$env:PATH"

function Write-Status($Message, $Type = "INFO") {
    $colors = @{ "INFO" = "Cyan"; "SUCCESS" = "Green"; "ERROR" = "Red"; "WARNING" = "Yellow" }
    Write-Host "[$Type] $Message" -ForegroundColor $colors[$Type]
}

function Show-Help {
    Write-Host @"
╔════════════════════════════════════════════════════════════════════════════════╗
║                      TEST LOCAL WINDOWS - SYSTÈME NSIA                        ║
╚════════════════════════════════════════════════════════════════════════════════╝

OBJECTIF:
  Lance l'application en mode développement local pour voir et tester l'interface

UTILISATION:
  .\test_local.ps1

PRÉREQUIS:
  - Java 11 installé
  - Maven installé  
  - Node.js installé

CE QUE FAIT CE SCRIPT:
  1. Vérifie les outils installés
  2. Lance le backend Spring Boot en mode dev
  3. Lance le frontend Next.js avec mocks
  4. Ouvre l'application dans le navigateur

ACCÈS:
  - Frontend: http://localhost:3000
  - Backend API: http://localhost:8080

"@
}

function Test-Prerequisites {
    Write-Status "Vérification des prérequis pour test local..." "INFO"
    
    # Java
    if (-Not (Test-Path "$env:JAVA_HOME\bin\java.exe")) {
        Write-Status "Java 11 non trouvé. Vérifiez l'installation." "ERROR"
        return $false
    }
    
    # Maven  
    if (-Not (Test-Path "$env:M2_HOME\bin\mvn.cmd")) {
        Write-Status "Maven non trouvé. Vérifiez l'installation." "ERROR"
        return $false
    }
    
    # Node.js
    try {
        $null = node --version
        $null = npm --version
    } catch {
        Write-Status "Node.js/npm non trouvé. Installez Node.js." "ERROR"
        return $false
    }
    
    Write-Status "✅ Tous les outils sont installés" "SUCCESS"
    return $true
}

function Start-Backend {
    Write-Status "Démarrage du backend Spring Boot..." "INFO"
    
    # Compilation rapide
    & "$env:M2_HOME\bin\mvn.cmd" compile -P dev -q
    if ($LASTEXITCODE -ne 0) {
        Write-Status "Erreur de compilation du backend" "ERROR"
        return $false
    }
    
    Write-Status "Backend compilé. Démarrage du serveur..." "SUCCESS"
    Write-Status "Backend disponible sur: http://localhost:8080" "INFO"
    
    # Démarrer en arrière-plan
    Start-Process powershell -ArgumentList "-Command", "cd '$PWD'; & '$env:M2_HOME\bin\mvn.cmd' spring-boot:run -P dev" -WindowStyle Minimized
    
    return $true
}

function Start-Frontend {
    Write-Status "Préparation du frontend Next.js..." "INFO"
    
    $frontendDir = ".\project-management-frontend"
    if (-Not (Test-Path $frontendDir)) {
        Write-Status "Dossier frontend non trouvé" "ERROR"
        return $false
    }
    
    Push-Location $frontendDir
    
    # Installation des dépendances si nécessaire
    if (-Not (Test-Path "node_modules")) {
        Write-Status "Installation des dépendances npm..." "INFO"
        npm install --silent
    }
    
    Write-Status "Démarrage du serveur de développement frontend..." "SUCCESS"
    Write-Status "Frontend disponible sur: http://localhost:3000" "INFO"
    Write-Status "Mode: Développement avec données de démonstration" "INFO"
    
    # Démarrer le serveur de développement
    Start-Process powershell -ArgumentList "-Command", "cd '$PWD'; npm run dev" -WindowStyle Normal
    
    Pop-Location
    return $true
}

function Open-Application {
    Write-Status "Attente du démarrage des serveurs..." "INFO"
    Start-Sleep -Seconds 15
    
    Write-Status "Ouverture de l'application dans le navigateur..." "SUCCESS"
    Start-Process "http://localhost:3000"
}

# ================================================================
# EXÉCUTION PRINCIPALE
# ================================================================

if ($Help) {
    Show-Help
    exit 0
}

Write-Host @"
╔════════════════════════════════════════════════════════════════════════════════╗
║                      TEST LOCAL - SYSTÈME NSIA                                ║
║                   Lancement en mode développement                             ║
╚════════════════════════════════════════════════════════════════════════════════╝
"@

# Vérifications
if (-Not (Test-Prerequisites)) {
    Write-Status "Prérequis non satisfaits. Arrêt." "ERROR"
    exit 1
}

# Démarrage backend
if (-Not (Start-Backend)) {
    Write-Status "Impossible de démarrer le backend" "ERROR"
    exit 1
}

# Démarrage frontend
if (-Not (Start-Frontend)) {
    Write-Status "Impossible de démarrer le frontend" "ERROR"
    exit 1
}

# Ouverture automatique
Open-Application

Write-Host @"

╔════════════════════════════════════════════════════════════════════════════════╗
║                          APPLICATION LANCÉE                                   ║
╚════════════════════════════════════════════════════════════════════════════════╝

🌐 Frontend (Interface): http://localhost:3000
🔧 Backend (API): http://localhost:8080

📋 FONCTIONNALITÉS DISPONIBLES:
  - Tableau de bord avec statistiques
  - Gestion des projets (création, modification)
  - Équipes et planning
  - KPIs et rapports
  - Export de données

💡 DONNÉES DE DÉMONSTRATION:
  L'application utilise des données fictives pour la démonstration.
  Toutes les fonctionnalités sont testables sans base de données.

🛑 POUR ARRÊTER:
  Fermez les fenêtres PowerShell ouvertes ou Ctrl+C dans chaque terminal

📞 En cas de problème, vérifiez que les ports 3000 et 8080 sont libres.

"@