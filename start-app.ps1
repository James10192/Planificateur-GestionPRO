# ========================================
# 🚀 Planificateur-GestionPRO - Démarrage Rapide
# Script unifié pour démarrer l'application complète
# ========================================

param(
    [switch]$Help,
    [switch]$XamppOnly,
    [switch]$DevMode,
    [ValidateSet("setup", "start", "stop")]
    [string]$Action = "start"
)

# Configuration des couleurs
$Colors = @{
    Success = "Green"
    Warning = "Yellow" 
    Error = "Red"
    Info = "Cyan"
    Header = "Magenta"
}

function Write-ColoredOutput($Message, $Type = "Info") {
    Write-Host $Message -ForegroundColor $Colors[$Type]
}

function Show-Header {
    Write-Host @"
╔════════════════════════════════════════════════════════════════════════════════╗
║                     🚀 PLANIFICATEUR-GESTIONPRO                              ║
║                   Système de Gestion de Projets NSIA                         ║
╚════════════════════════════════════════════════════════════════════════════════╝
"@ -ForegroundColor $Colors.Header
}

function Show-Help {
    Show-Header
    Write-Host @"

📋 UTILISATION:
  .\start-app.ps1 [OPTIONS]

🔧 ACTIONS DISPONIBLES:
  -Action setup     Configuration initiale (XAMPP + Base de données)
  -Action start     Démarrer l'application complète (défaut)
  -Action stop      Arrêter tous les services

⚙️  OPTIONS:
  -XamppOnly        Démarrer seulement XAMPP/MySQL
  -DevMode          Mode développement avec données de test
  -Help             Afficher cette aide

📖 EXEMPLES:
  .\start-app.ps1                    # Démarrage normal
  .\start-app.ps1 -Action setup      # Configuration initiale
  .\start-app.ps1 -XamppOnly         # XAMPP seulement
  .\start-app.ps1 -DevMode           # Mode développement

🌐 ACCÈS APRÈS DÉMARRAGE:
  Frontend:    http://localhost:3000
  Backend API: http://localhost:8080/nsia
  phpMyAdmin:  http://localhost/phpmyadmin

👤 CONNEXION TEST:
  Email:       admin@nsia.ci
  Password:    admin

"@
}

function Test-Prerequisites {
    Write-ColoredOutput "🔍 Vérification des prérequis..." "Info"
    
    $missing = @()
    
    # Java 11
    $javaPath = "C:\Program Files (x86)\Amazon Corretto\jdk11.0.27_6"
    if (-not (Test-Path "$javaPath\bin\java.exe")) {
        $missing += "Java 11 (Amazon Corretto)"
    }
    
    # Maven
    $mavenPath = "C:\Program Files\apache-maven-3.9.6"
    if (-not (Test-Path "$mavenPath\bin\mvn.cmd")) {
        $missing += "Apache Maven 3.9.6"
    }
    
    # Node.js
    try {
        $null = node --version 2>$null
        $null = npm --version 2>$null
    } catch {
        $missing += "Node.js"
    }
    
    if ($missing.Count -gt 0) {
        Write-ColoredOutput "❌ Prérequis manquants:" "Error"
        $missing | ForEach-Object { Write-ColoredOutput "   - $_" "Error" }
        Write-ColoredOutput "📥 Consultez docs/GUIDE_DEMARRAGE.md pour l'installation" "Warning"
        return $false
    }
    
    Write-ColoredOutput "✅ Tous les prérequis sont installés" "Success"
    return $true
}

function Setup-Environment {
    Write-ColoredOutput "⚙️ Configuration de l'environnement..." "Info"
    
    # Configuration Java/Maven
    $env:JAVA_HOME = "C:\Program Files (x86)\Amazon Corretto\jdk11.0.27_6"
    $env:M2_HOME = "C:\Program Files\apache-maven-3.9.6"
    $env:MAVEN_HOME = $env:M2_HOME
    $env:PATH = "$env:JAVA_HOME\bin;$env:M2_HOME\bin;$env:PATH"
    
    Write-ColoredOutput "✅ Variables d'environnement configurées" "Success"
}

function Setup-Xampp {
    Write-ColoredOutput "🔧 Configuration XAMPP..." "Info"
    
    # Recherche de XAMPP
    $xamppPaths = @(
        "C:\xampp\xampp-control.exe",
        "C:\Program Files\XAMPP\xampp-control.exe",
        "C:\Program Files (x86)\XAMPP\xampp-control.exe"
    )
    
    $xamppPath = $null
    foreach ($path in $xamppPaths) {
        if (Test-Path $path) {
            $xamppPath = $path
            break
        }
    }
    
    if (-not $xamppPath) {
        Write-ColoredOutput "❌ XAMPP non trouvé. Installez-le depuis https://www.apachefriends.org/" "Error"
        return $false
    }
    
    Write-ColoredOutput "✅ XAMPP trouvé: $xamppPath" "Success"
    Write-ColoredOutput "🚀 Lancement du panneau XAMPP..." "Info"
    Start-Process $xamppPath
    
    Write-ColoredOutput @"
📋 INSTRUCTIONS XAMPP:
   1. Démarrez Apache et MySQL dans le panneau XAMPP
   2. Ouvrez phpMyAdmin: http://localhost/phpmyadmin
   3. Importez le script: scripts/sql/create_mysql_database.sql
"@ "Warning"
    
    Read-Host "Appuyez sur ENTRÉE une fois XAMPP configuré et la base créée"
    return $true
}

function Start-Backend {
    Write-ColoredOutput "🔧 Démarrage du backend Spring Boot..." "Info"
    
    try {
        # Compilation
        Write-ColoredOutput "📦 Compilation du projet..." "Info"
        & "$env:M2_HOME\bin\mvn.cmd" clean compile -P local-mysql -q
        
        if ($LASTEXITCODE -ne 0) {
            Write-ColoredOutput "❌ Erreur de compilation" "Error"
            return $false
        }
        
        Write-ColoredOutput "✅ Compilation réussie" "Success"
        
        # Démarrage en arrière-plan
        Write-ColoredOutput "🚀 Démarrage du serveur backend..." "Info"
        $backendJob = Start-Job -ScriptBlock {
            param($mavenPath, $workDir)
            Set-Location $workDir
            & "$mavenPath\bin\mvn.cmd" spring-boot:run -P local-mysql
        } -ArgumentList $env:M2_HOME, $PWD
        
        Write-ColoredOutput "✅ Backend démarré (Job ID: $($backendJob.Id))" "Success"
        Write-ColoredOutput "🌐 API disponible sur: http://localhost:8080/nsia" "Info"
        
        return $true
    } catch {
        Write-ColoredOutput "❌ Erreur backend: $_" "Error"
        return $false
    }
}

function Start-Frontend {
    Write-ColoredOutput "🎨 Démarrage du frontend Next.js..." "Info"
    
    try {
        Set-Location "project-management-frontend"
        
        # Installation des dépendances
        if (-not (Test-Path "node_modules")) {
            Write-ColoredOutput "📦 Installation des dépendances npm..." "Info"
            npm install --silent
        }
        
        # Configuration environnement
        if ($DevMode) {
            $env:NEXT_PUBLIC_USE_MOCKS = "true"
            Write-ColoredOutput "🧪 Mode développement avec données de test" "Warning"
        } else {
            $env:NEXT_PUBLIC_USE_MOCKS = "false"
            $env:NEXT_PUBLIC_API_BASE_URL = "http://localhost:8080/nsia"
        }
        
        # Démarrage
        Write-ColoredOutput "🚀 Démarrage du serveur frontend..." "Info"
        $frontendJob = Start-Job -ScriptBlock {
            param($workDir, $useMocks, $apiUrl)
            Set-Location "$workDir\project-management-frontend"
            $env:NEXT_PUBLIC_USE_MOCKS = $useMocks
            if ($apiUrl) { $env:NEXT_PUBLIC_API_BASE_URL = $apiUrl }
            npm run dev
        } -ArgumentList $PWD.Path.Replace("\project-management-frontend", ""), $(if ($DevMode) { "true" } else { "false" }), $(if (-not $DevMode) { "http://localhost:8080/nsia" } else { $null })
        
        Set-Location ".."
        
        Write-ColoredOutput "✅ Frontend démarré (Job ID: $($frontendJob.Id))" "Success"
        Write-ColoredOutput "🌐 Interface disponible sur: http://localhost:3000" "Info"
        
        return $true
    } catch {
        Write-ColoredOutput "❌ Erreur frontend: $_" "Error"
        Set-Location ".."
        return $false
    }
}

function Open-Application {
    Write-ColoredOutput "⏳ Attente du démarrage complet..." "Info"
    Start-Sleep -Seconds 10
    
    Write-ColoredOutput "🌐 Ouverture dans le navigateur..." "Success"
    Start-Process "http://localhost:3000"
}

function Stop-Services {
    Write-ColoredOutput "🛑 Arrêt des services..." "Warning"
    
    # Arrêter les jobs PowerShell
    Get-Job | Where-Object { $_.Command -like "*mvn*" -or $_.Command -like "*npm*" } | Stop-Job
    Get-Job | Where-Object { $_.Command -like "*mvn*" -or $_.Command -like "*npm*" } | Remove-Job
    
    Write-ColoredOutput "✅ Services arrêtés" "Success"
}

function Show-Summary {
    Write-ColoredOutput @"

🎉 APPLICATION DÉMARRÉE AVEC SUCCÈS!
═══════════════════════════════════════

🌐 ACCÈS:
   Frontend:    http://localhost:3000
   Backend:     http://localhost:8080/nsia
   phpMyAdmin:  http://localhost/phpmyadmin

👤 CONNEXION TEST:
   Email:       admin@nsia.ci
   Password:    admin

📊 FONCTIONNALITÉS:
   ✅ Tableau de bord avec KPIs
   ✅ Gestion complète des projets
   ✅ Équipes et planification
   ✅ Exports PDF/Excel/CSV
   ✅ Audit trail et sécurité

🛑 POUR ARRÊTER:
   .\start-app.ps1 -Action stop

📚 DOCUMENTATION:
   docs/GUIDE_DEMARRAGE.md
   docs/ARCHITECTURE.md

"@ "Success"
}

# ========================================
# EXÉCUTION PRINCIPALE
# ========================================

if ($Help) {
    Show-Help
    exit 0
}

Show-Header

switch ($Action.ToLower()) {
    "setup" {
        if (-not (Test-Prerequisites)) { exit 1 }
        Setup-Environment
        if (-not (Setup-Xampp)) { exit 1 }
        Write-ColoredOutput "✅ Configuration terminée. Lancez: .\start-app.ps1" "Success"
    }
    
    "start" {
        if (-not (Test-Prerequisites)) { exit 1 }
        Setup-Environment
        
        if ($XamppOnly) {
            Setup-Xampp
            exit 0
        }
        
        if (-not (Start-Backend)) { exit 1 }
        if (-not (Start-Frontend)) { exit 1 }
        
        Open-Application
        Show-Summary
        
        # Monitoring simple
        Write-ColoredOutput "📊 Monitoring des services (Ctrl+C pour arrêter)..." "Info"
        try {
            while ($true) {
                Start-Sleep -Seconds 30
                Write-ColoredOutput "✅ Services actifs - $(Get-Date -Format 'HH:mm:ss')" "Success"
            }
        } finally {
            Stop-Services
        }
    }
    
    "stop" {
        Stop-Services
    }
    
    default {
        Write-ColoredOutput "❌ Action inconnue: $Action" "Error"
        Show-Help
        exit 1
    }
}