# 🚀 Planificateur-GestionPRO

## 📋 À Propos
Système complet de gestion de projets développé avec **Java 11 + Spring Boot** et **Next.js**, optimisé pour MySQL (local) et Oracle Database (production) avec déploiement sur serveurs RHEL.

## ⚡ Démarrage Ultra-Rapide

### 💻 Windows + XAMPP (Recommandé)
```powershell
# Configuration initiale (une seule fois)
.\start-app.ps1 -Action setup

# Démarrage de l'application
.\start-app.ps1

# Accès: http://localhost:3000
```

### 🧪 Mode Développement
```powershell
# Développement avec données de test (sans base de données)
.\start-app.ps1 -DevMode

# XAMPP seulement
.\start-app.ps1 -XamppOnly
```

### 🐧 Déploiement Serveur RHEL
```bash
# Configuration serveur
sudo ./scripts/linux/setup_rhel_server.sh

# Déploiement
./scripts/linux/deploy_test_server.sh --oracle-password VOTRE_PASSWORD
```

## 📁 Structure Optimisée

```
📁 Planificateur-GestionPRO/
├── 🚀 start-app.ps1              # Script principal (Windows)
├── 📁 scripts/
│   ├── 📁 windows/               # Scripts XAMPP optimisés
│   ├── 📁 linux/                 # Scripts RHEL/Oracle
│   └── 📁 sql/                   # Scripts base de données
├── 📁 docs/                      # Documentation complète  
├── 📁 src/                       # Backend Java Spring Boot
├── 📁 project-management-frontend/  # Frontend Next.js
└── 📄 pom.xml                   # Configuration Maven
```

## ✨ Fonctionnalités

- 📊 **Tableaux de bord** avec KPIs temps réel
- 📋 **Gestion de projets** complète (CRUD)
- 👥 **Gestion d'équipes** et rôles
- 📈 **Planification** avancée avec dépendances
- 📁 **Gestion documentaire** avec versioning
- 💰 **Suivi budgétaire** temps réel
- 📊 **Exports** multi-formats (PDF/Excel/CSV)
- 🔒 **Sécurité** JWT + audit trail complet

## 🔧 Stack Technique

- **Backend**: Java 11, Spring Boot 3.2.5, JPA/Hibernate
- **Frontend**: Next.js 14, React 18, TypeScript, Tailwind CSS
- **Base de données**: MySQL 8.0 (local), Oracle 12c+ (production)
- **Serveur**: XAMPP (dev), Payara Server 6.x (prod)
- **Déploiement**: Windows + XAMPP, RHEL/CentOS + Oracle

## 📖 Documentation

- 🚀 **[Guide de démarrage](docs/GUIDE_DEMARRAGE.md)** - Configuration et lancement
- 🏗️ **[Architecture](docs/ARCHITECTURE.md)** - Structure technique détaillée
- 💡 **Scripts intégrés avec aide** : `.\start-app.ps1 -Help`

## 🎯 Statut du Projet

✅ **100% TERMINÉ** - Prêt pour utilisation  
✅ **Code optimisé** - Structure propre et maintenable  
✅ **Scripts unifiés** - Démarrage en une commande  
✅ **Documentation complète** - Guides détaillés  

## 🏆 Commandes Essentielles

```powershell
# Configuration initiale
.\start-app.ps1 -Action setup

# Démarrage normal  
.\start-app.ps1

# Développement avec mocks
.\start-app.ps1 -DevMode

# Arrêt des services
.\start-app.ps1 -Action stop

# Aide détaillée
.\start-app.ps1 -Help
```

## 📞 Accès Application

- **Interface utilisateur** : http://localhost:3000
- **API Backend** : http://localhost:8080/nsia  
- **phpMyAdmin** : http://localhost/phpmyadmin
- **Connexion test** : admin@nsia.ci / admin

---

**💡 Tip** : Consultez `docs/GUIDE_DEMARRAGE.md` pour une configuration détaillée