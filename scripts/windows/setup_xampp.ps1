# ========================================
# Script de configuration XAMPP rapide
# Système de Gestion de Projets NSIA
# ========================================

Write-Host "🔧 Configuration XAMPP pour le projet NSIA" -ForegroundColor Green
Write-Host "===========================================" -ForegroundColor Green

# Fonction pour trouver XAMPP
function Find-XamppPath {
    $xamppPaths = @(
        "C:\xampp",
        "C:\Program Files\XAMPP",
        "C:\Program Files (x86)\XAMPP"
    )
    
    foreach ($path in $xamppPaths) {
        if (Test-Path "$path\xampp-control.exe") {
            return $path
        }
    }
    return $null
}

# Recherche de XAMPP
$xamppPath = Find-XamppPath

if (-not $xamppPath) {
    Write-Host "❌ XAMPP non trouvé!" -ForegroundColor Red
    Write-Host "Veuillez installer XAMPP depuis: https://www.apachefriends.org/" -ForegroundColor Yellow
    Write-Host "Puis relancez ce script." -ForegroundColor Yellow
    exit 1
}

Write-Host "✅ XAMPP trouvé: $xamppPath" -ForegroundColor Green

# Configuration rapide
Write-Host ""
Write-Host "🚀 Instructions de configuration:" -ForegroundColor Cyan
Write-Host "=================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "1. 📱 Lancement du panneau XAMPP..." -ForegroundColor Yellow
Start-Process "$xamppPath\xampp-control.exe"

Write-Host ""
Write-Host "2. ⚡ Dans le panneau XAMPP:" -ForegroundColor Yellow
Write-Host "   - Cliquez sur 'Start' pour Apache" -ForegroundColor White
Write-Host "   - Cliquez sur 'Start' pour MySQL" -ForegroundColor White
Write-Host "   - Attendez que les deux soient 'Running' (vert)" -ForegroundColor White

Write-Host ""
Write-Host "3. 🗄️  Configuration de la base de données:" -ForegroundColor Yellow
Write-Host "   - Ouvrez votre navigateur" -ForegroundColor White
Write-Host "   - Allez sur: http://localhost/phpmyadmin" -ForegroundColor White
Write-Host "   - Cliquez sur 'Importer' dans le menu" -ForegroundColor White
Write-Host "   - Sélectionnez le fichier: scripts/sql/create_mysql_database.sql" -ForegroundColor White
Write-Host "   - Cliquez sur 'Exécuter'" -ForegroundColor White

Write-Host ""
Write-Host "4. ✅ Vérification:" -ForegroundColor Yellow
Write-Host "   - Vous devriez voir la base 'nsia_project_db' créée" -ForegroundColor White
Write-Host "   - Avec plusieurs tables (tbpro, tbutil, pdir, etc.)" -ForegroundColor White

Write-Host ""
Write-Host "5. 🎯 Démarrage de l'application:" -ForegroundColor Yellow
Write-Host "   - Lancez: .\scripts\windows\start_xampp_app.ps1" -ForegroundColor White

Write-Host ""
Write-Host "📋 CHEMINS IMPORTANTS:" -ForegroundColor Cyan
Write-Host "   XAMPP:        $xamppPath" -ForegroundColor White
Write-Host "   phpMyAdmin:   http://localhost/phpmyadmin" -ForegroundColor White
Write-Host "   Script SQL:   $(Get-Location)\scripts\sql\create_mysql_database.sql" -ForegroundColor White

Write-Host ""
Write-Host "⚠️  NOTES IMPORTANTES:" -ForegroundColor Yellow
Write-Host "   - Utilisateur MySQL: root (sans mot de passe)" -ForegroundColor White
Write-Host "   - Base de données: nsia_project_db" -ForegroundColor White
Write-Host "   - Port Apache: 80" -ForegroundColor White
Write-Host "   - Port MySQL: 3306" -ForegroundColor White

Write-Host ""
Write-Host "❓ EN CAS DE PROBLÈME:" -ForegroundColor Red
Write-Host "   - Vérifiez qu'aucun autre service n'utilise les ports 80 et 3306" -ForegroundColor White
Write-Host "   - Désactivez Skype ou autres services sur le port 80" -ForegroundColor White
Write-Host "   - Relancez XAMPP en tant qu'administrateur si nécessaire" -ForegroundColor White

Write-Host ""
Read-Host "Appuyez sur ENTRÉE une fois XAMPP configuré et la base créée"

# Test de connexion
Write-Host "🔌 Test de la configuration..." -ForegroundColor Yellow

try {
    $response = Invoke-WebRequest -Uri "http://localhost/phpmyadmin" -TimeoutSec 5 -ErrorAction Stop
    if ($response.StatusCode -eq 200) {
        Write-Host "✅ Apache fonctionne" -ForegroundColor Green
    }
} catch {
    Write-Host "❌ Apache non accessible" -ForegroundColor Red
}

Write-Host ""
Write-Host "🎉 Configuration terminée!" -ForegroundColor Green
Write-Host "Vous pouvez maintenant lancer: .\scripts\windows\start_xampp_app.ps1" -ForegroundColor Cyan