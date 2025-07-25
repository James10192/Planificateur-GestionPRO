# ================================================================
# CONFIGURATION ENVIRONNEMENT WINDOWS - SYSTÈME NSIA
# Configure Java 11 et Maven pour le projet
# ================================================================

Write-Host @"
╔════════════════════════════════════════════════════════════════════════════════╗
║                    CONFIGURATION ENVIRONNEMENT NSIA                           ║
╚════════════════════════════════════════════════════════════════════════════════╝
"@

# Chemins de configuration
$javaPath = "C:\Program Files (x86)\Amazon Corretto\jdk11.0.27_6"
$mavenPath = "C:\Program Files\apache-maven-3.9.6"

Write-Host "Configuration de Java 11 et Maven..." -ForegroundColor Cyan

# Vérifications
if (-Not (Test-Path $javaPath)) {
    Write-Host "❌ Java 11 non trouvé dans: $javaPath" -ForegroundColor Red
    Write-Host "Installez Amazon Corretto 11 ou ajustez le chemin" -ForegroundColor Yellow
    exit 1
}

if (-Not (Test-Path $mavenPath)) {
    Write-Host "❌ Maven non trouvé dans: $mavenPath" -ForegroundColor Red  
    Write-Host "Installez Maven 3.9.6 ou ajustez le chemin" -ForegroundColor Yellow
    exit 1
}

# Configuration variables d'environnement
try {
    [Environment]::SetEnvironmentVariable("JAVA_HOME", $javaPath, "User")
    [Environment]::SetEnvironmentVariable("M2_HOME", $mavenPath, "User")
    [Environment]::SetEnvironmentVariable("MAVEN_HOME", $mavenPath, "User")
    
    # Mise à jour PATH
    $currentPath = [Environment]::GetEnvironmentVariable("PATH", "User")
    $cleanPath = ($currentPath -split ";" | Where-Object { 
        $_ -notlike "*java*" -and $_ -notlike "*jdk*" -and $_ -notlike "*maven*" 
    }) -join ";"
    
    $newPath = "$javaPath\bin;$mavenPath\bin;$cleanPath"
    [Environment]::SetEnvironmentVariable("PATH", $newPath, "User")
    
    Write-Host "✅ Variables d'environnement configurées" -ForegroundColor Green
} catch {
    Write-Host "❌ Erreur configuration: $_" -ForegroundColor Red
    exit 1
}

# Configuration session actuelle
$env:JAVA_HOME = $javaPath
$env:M2_HOME = $mavenPath
$env:PATH = "$javaPath\bin;$mavenPath\bin;$env:PATH"

# Vérification
Write-Host "`n=== VÉRIFICATION ===" -ForegroundColor Green

try {
    $javaVersion = & "$javaPath\bin\java.exe" -version 2>&1 | Select-Object -First 1
    Write-Host "✅ Java: $javaVersion" -ForegroundColor Green
} catch {
    Write-Host "❌ Erreur Java" -ForegroundColor Red
}

try {
    $mavenVersion = & "$mavenPath\bin\mvn.cmd" --version 2>&1 | Select-Object -First 1
    Write-Host "✅ Maven: $mavenVersion" -ForegroundColor Green
} catch {
    Write-Host "❌ Erreur Maven" -ForegroundColor Red
}

Write-Host @"

╔════════════════════════════════════════════════════════════════════════════════╗
║                        CONFIGURATION TERMINÉE                                 ║
╚════════════════════════════════════════════════════════════════════════════════╝

✅ Environment configuré pour le développement local

PROCHAINES ÉTAPES:
1. Redémarrez votre terminal/IDE
2. Lancez: .\test_local.ps1

VARIABLES CONFIGURÉES:
- JAVA_HOME: $javaPath
- M2_HOME: $mavenPath
- PATH: Mis à jour avec Java et Maven

"@