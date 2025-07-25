# ========================================
# Script de démarrage complet XAMPP + Application
# Système de Gestion de Projets NSIA
# ========================================

param(
    [switch]$SkipXamppStart,
    [switch]$OnlyBackend,
    [switch]$OnlyFrontend,
    [switch]$OpenBrowser = $true
)

Write-Host "🚀 Démarrage du système de gestion de projets NSIA" -ForegroundColor Green
Write-Host "=================================================" -ForegroundColor Green

# Configuration des variables d'environnement Java/Maven
Write-Host "📋 Configuration de l'environnement Java..." -ForegroundColor Yellow

# Définir JAVA_HOME
$env:JAVA_HOME = "C:\Program Files (x86)\Amazon Corretto\jdk11.0.27_6"

# Définir M2_HOME
$env:M2_HOME = "C:\Program Files\apache-maven-3.9.6"

# Définir MAVEN_HOME
$env:MAVEN_HOME = $env:M2_HOME

# Ajouter Maven au PATH
$env:PATH = $env:PATH + ";$env:M2_HOME\bin"

# Vérification des prérequis
Write-Host "🔍 Vérification des prérequis..." -ForegroundColor Yellow

# Vérifier Java
if (-not (Test-Path $env:JAVA_HOME)) {
    Write-Host "❌ ERREUR: Java JDK 11 non trouvé à $env:JAVA_HOME" -ForegroundColor Red
    Write-Host "Veuillez installer Amazon Corretto JDK 11 ou ajuster le chemin" -ForegroundColor Red
    exit 1
}

# Vérifier Maven
if (-not (Test-Path $env:M2_HOME)) {
    Write-Host "❌ ERREUR: Maven non trouvé à $env:M2_HOME" -ForegroundColor Red
    Write-Host "Veuillez installer Apache Maven ou ajuster le chemin" -ForegroundColor Red
    exit 1
}

# Vérifier Node.js pour le frontend
$nodeVersion = node --version 2>$null
if (-not $nodeVersion) {
    Write-Host "❌ ERREUR: Node.js n'est pas installé" -ForegroundColor Red
    Write-Host "Veuillez installer Node.js depuis https://nodejs.org/" -ForegroundColor Red
    exit 1
}

Write-Host "✅ Java JDK: $((& "$env:JAVA_HOME\bin\java" -version 2>&1)[0])" -ForegroundColor Green
Write-Host "✅ Maven: $((& "$env:M2_HOME\bin\mvn" --version 2>&1)[0])" -ForegroundColor Green
Write-Host "✅ Node.js: $nodeVersion" -ForegroundColor Green

# Démarrage de XAMPP (si nécessaire)
if (-not $SkipXamppStart) {
    Write-Host "🔧 Vérification de XAMPP..." -ForegroundColor Yellow
    
    # Chemins possibles de XAMPP
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
    
    if ($xamppPath) {
        Write-Host "✅ XAMPP trouvé à: $xamppPath" -ForegroundColor Green
        Write-Host "🚀 Lancement du panneau de contrôle XAMPP..." -ForegroundColor Yellow
        Start-Process $xamppPath
        
        Write-Host "⚠️  IMPORTANT:" -ForegroundColor Yellow
        Write-Host "   1. Démarrez Apache et MySQL depuis le panneau XAMPP" -ForegroundColor White
        Write-Host "   2. Ouvrez phpMyAdmin (http://localhost/phpmyadmin)" -ForegroundColor White
        Write-Host "   3. Importez le script: scripts/sql/create_mysql_database.sql" -ForegroundColor White
        Write-Host ""
        Write-Host "Appuyez sur ENTRÉE une fois XAMPP démarré et la base créée..." -ForegroundColor Cyan
        Read-Host
    } else {
        Write-Host "⚠️  XAMPP non trouvé automatiquement" -ForegroundColor Yellow
        Write-Host "Veuillez démarrer XAMPP manuellement:" -ForegroundColor White
        Write-Host "   1. Démarrer Apache et MySQL" -ForegroundColor White
        Write-Host "   2. Importer scripts/sql/create_mysql_database.sql via phpMyAdmin" -ForegroundColor White
        Write-Host ""
        Read-Host "Appuyez sur ENTRÉE une fois XAMPP prêt"
    }
}

# Test de connexion MySQL
Write-Host "🔌 Test de connexion à MySQL..." -ForegroundColor Yellow
try {
    $result = mysql -u root -e "SHOW DATABASES;" 2>$null
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ Connexion MySQL réussie" -ForegroundColor Green
    } else {
        Write-Host "⚠️  MySQL non accessible via ligne de commande (normal avec XAMPP)" -ForegroundColor Yellow
    }
} catch {
    Write-Host "⚠️  Test MySQL ignoré (commande mysql non disponible)" -ForegroundColor Yellow
}

# Démarrage du backend Spring Boot
if (-not $OnlyFrontend) {
    Write-Host "🔧 Compilation et démarrage du backend..." -ForegroundColor Yellow
    
    try {
        # Compilation Maven
        Write-Host "📦 Compilation Maven..." -ForegroundColor Cyan
        & "$env:M2_HOME\bin\mvn" clean compile -P local-mysql -q
        
        if ($LASTEXITCODE -ne 0) {
            Write-Host "❌ Erreur lors de la compilation Maven" -ForegroundColor Red
            exit 1
        }
        
        Write-Host "✅ Compilation réussie" -ForegroundColor Green
        
        # Démarrage du backend en arrière-plan
        Write-Host "🚀 Démarrage du backend Spring Boot..." -ForegroundColor Cyan
        Write-Host "Port: http://localhost:8080/nsia" -ForegroundColor White
        
        $backendJob = Start-Job -ScriptBlock {
            param($mavenPath, $workDir)
            Set-Location $workDir
            & "$mavenPath\bin\mvn" spring-boot:run -P local-mysql
        } -ArgumentList $env:M2_HOME, $PWD
        
        Write-Host "✅ Backend démarré (Job ID: $($backendJob.Id))" -ForegroundColor Green
        
        # Attendre que le backend soit prêt
        Write-Host "⏳ Attente du démarrage du backend..." -ForegroundColor Yellow
        Start-Sleep -Seconds 10
        
        # Test du backend
        try {
            $response = Invoke-WebRequest -Uri "http://localhost:8080/nsia/actuator/health" -TimeoutSec 5 -ErrorAction SilentlyContinue
            if ($response.StatusCode -eq 200) {
                Write-Host "✅ Backend opérationnel" -ForegroundColor Green
            } else {
                Write-Host "⚠️  Backend en cours de démarrage..." -ForegroundColor Yellow
            }
        } catch {
            Write-Host "⚠️  Backend encore en démarrage (normal)" -ForegroundColor Yellow
        }
        
    } catch {
        Write-Host "❌ Erreur lors du démarrage du backend: $_" -ForegroundColor Red
        exit 1
    }
}

# Démarrage du frontend Next.js
if (-not $OnlyBackend) {
    Write-Host "🎨 Démarrage du frontend Next.js..." -ForegroundColor Yellow
    
    try {
        Set-Location "project-management-frontend"
        
        # Installation des dépendances si nécessaire
        if (-not (Test-Path "node_modules")) {
            Write-Host "📦 Installation des dépendances npm..." -ForegroundColor Cyan
            npm install
            
            if ($LASTEXITCODE -ne 0) {
                Write-Host "❌ Erreur lors de l'installation npm" -ForegroundColor Red
                Set-Location ".."
                exit 1
            }
        }
        
        Write-Host "✅ Dépendances npm prêtes" -ForegroundColor Green
        
        # Configuration pour utiliser le vrai backend
        $env:NEXT_PUBLIC_USE_MOCKS = "false"
        $env:NEXT_PUBLIC_API_BASE_URL = "http://localhost:8080/nsia"
        
        Write-Host "🚀 Démarrage du serveur Next.js..." -ForegroundColor Cyan
        Write-Host "Port: http://localhost:3000" -ForegroundColor White
        
        $frontendJob = Start-Job -ScriptBlock {
            param($workDir)
            Set-Location "$workDir\project-management-frontend"
            $env:NEXT_PUBLIC_USE_MOCKS = "false"
            $env:NEXT_PUBLIC_API_BASE_URL = "http://localhost:8080/nsia"
            npm run dev
        } -ArgumentList $PWD.Path.Replace("\project-management-frontend", "")
        
        Write-Host "✅ Frontend démarré (Job ID: $($frontendJob.Id))" -ForegroundColor Green
        
        Set-Location ".."
        
    } catch {
        Write-Host "❌ Erreur lors du démarrage du frontend: $_" -ForegroundColor Red
        Set-Location ".."
        exit 1
    }
}

# Résumé et instructions finales
Write-Host ""
Write-Host "🎉 APPLICATION DÉMARRÉE AVEC SUCCÈS!" -ForegroundColor Green
Write-Host "====================================" -ForegroundColor Green
Write-Host ""
Write-Host "📱 ACCÈS À L'APPLICATION:" -ForegroundColor Cyan
Write-Host "   Frontend: http://localhost:3000" -ForegroundColor White
Write-Host "   Backend:  http://localhost:8080/nsia" -ForegroundColor White
Write-Host "   API Doc:  http://localhost:8080/nsia/swagger-ui.html" -ForegroundColor White
Write-Host ""
Write-Host "🗄️  BASE DE DONNÉES:" -ForegroundColor Cyan
Write-Host "   phpMyAdmin: http://localhost/phpmyadmin" -ForegroundColor White
Write-Host "   Database:   nsia_project_db" -ForegroundColor White
Write-Host "   User:       root (pas de mot de passe)" -ForegroundColor White
Write-Host ""
Write-Host "👤 CONNEXION TEST:" -ForegroundColor Cyan
Write-Host "   Email:    admin@nsia.ci" -ForegroundColor White
Write-Host "   Password: admin" -ForegroundColor White
Write-Host ""

# Ouverture automatique du navigateur
if ($OpenBrowser -and -not $OnlyBackend) {
    Write-Host "🌐 Ouverture du navigateur..." -ForegroundColor Yellow
    Start-Sleep -Seconds 3
    Start-Process "http://localhost:3000"
}

Write-Host "⚠️  INSTRUCTIONS:" -ForegroundColor Yellow
Write-Host "   - Gardez cette fenêtre ouverte pour maintenir l'application" -ForegroundColor White
Write-Host "   - Appuyez sur Ctrl+C pour arrêter l'application" -ForegroundColor White
Write-Host "   - Logs en temps réel ci-dessous" -ForegroundColor White
Write-Host ""

# Affichage des logs en temps réel
Write-Host "📊 MONITORING DES SERVICES..." -ForegroundColor Green
Write-Host "=============================" -ForegroundColor Green

try {
    while ($true) {
        Start-Sleep -Seconds 5
        
        # Status des jobs
        if (-not $OnlyFrontend -and $backendJob) {
            $backendStatus = Get-Job $backendJob.Id
            Write-Host "Backend: $($backendStatus.State)" -ForegroundColor $(if ($backendStatus.State -eq "Running") { "Green" } else { "Red" })
        }
        
        if (-not $OnlyBackend -and $frontendJob) {
            $frontendStatus = Get-Job $frontendJob.Id
            Write-Host "Frontend: $($frontendStatus.State)" -ForegroundColor $(if ($frontendStatus.State -eq "Running") { "Green" } else { "Red" })
        }
        
        Write-Host "Temps: $(Get-Date -Format 'HH:mm:ss')" -ForegroundColor Gray
        Write-Host "---"
    }
} finally {
    # Nettoyage lors de l'arrêt
    Write-Host ""
    Write-Host "🛑 Arrêt de l'application..." -ForegroundColor Yellow
    
    if ($backendJob) {
        Stop-Job $backendJob -ErrorAction SilentlyContinue
        Remove-Job $backendJob -ErrorAction SilentlyContinue
        Write-Host "✅ Backend arrêté" -ForegroundColor Green
    }
    
    if ($frontendJob) {
        Stop-Job $frontendJob -ErrorAction SilentlyContinue
        Remove-Job $frontendJob -ErrorAction SilentlyContinue
        Write-Host "✅ Frontend arrêté" -ForegroundColor Green
    }
    
    Write-Host "👋 Au revoir!" -ForegroundColor Green
}