# 🚀 Planificateur-GestionPRO

## 📋 À Propos
Système complet de gestion de projets développé avec **Java 11 + Spring Boot** et **Next.js**, optimisé pour Oracle Database et déploiement sur serveurs RHEL.

## ⚡ Démarrage Rapide

### 💻 Test Local Windows (XAMPP)
```powershell
# 1. Configuration XAMPP (une seule fois)
.\scripts\windows\setup_xampp.ps1

# 2. Lancer l'application complète
.\scripts\windows\start_xampp_app.ps1

# Accès: http://localhost:3000
```

### 🐧 Déploiement Serveur RHEL
```bash
# 1. Configuration serveur (une seule fois)
sudo ./scripts/linux/setup_rhel_server.sh

# 2. Déploiement
./scripts/linux/deploy_test_server.sh --oracle-password VOTRE_PASSWORD

# Accès: http://serveur:8080/nsia
```

## 📁 Structure du Projet

```
📁 gestionpro(java)/
├── 📁 scripts/
│   ├── 📁 windows/           # Scripts pour test local Windows
│   └── 📁 linux/             # Scripts pour serveurs RHEL
├── 📁 docs/                  # Documentation complète
├── 📁 src/                   # Code source Java Spring Boot
├── 📁 project-management-frontend/  # Code source Next.js
└── 📄 pom.xml               # Configuration Maven
```

## ✨ Fonctionnalités

- 📊 **Tableau de bord** avec KPIs temps réel
- 📋 **Gestion de projets** complète (CRUD)
- 👥 **Gestion d'équipes** et rôles
- 📈 **Planification** multi-phases
- 📁 **Documents** avec versioning
- 💰 **Suivi budgétaire** 
- 📊 **Export** Excel/PDF/CSV
- 🔒 **Sécurité** JWT + audit trail

## 🔧 Technologies

- **Backend**: Java 11, Spring Boot 3.2.5, MySQL/Oracle Database
- **Frontend**: Next.js 14, React 18, TypeScript, Tailwind CSS
- **Serveur**: XAMPP (local), Payara Server 6.x (production)
- **OS**: Windows + XAMPP (dev), RHEL/CentOS (production)

## 📖 Documentation

- 📋 **[Guide d'utilisation](docs/GUIDE_UTILISATION.md)** - Instructions détaillées
- 👔 **[Résumé direction](docs/RESUME_EXECUTIF_DIRECTION.md)** - Vue managériale
- 📊 **[Rapport complet](docs/RAPPORT_AVANCEMENT_EXECUTIF.md)** - Détails techniques
- 🛠️ **[Guide développeur](docs/CLAUDE.md)** - Architecture technique

## 🎯 Statut du Projet

✅ **TERMINÉ** - Prêt pour déploiement  
✅ **100% Fonctionnel** - Toutes fonctionnalités implémentées  
✅ **Testé** - Scripts de déploiement validés  
✅ **Documenté** - Guides complets fournis  

## 🏆 Prochaines Étapes

1. **Configuration XAMPP** : Utilisez `.\scripts\windows\setup_xampp.ps1`
2. **Test local** : Utilisez `.\scripts\windows\start_xampp_app.ps1`
3. **Configuration Oracle serveur** : Préparez la base de données
4. **Déploiement serveur** : Suivez le guide dans `/docs/`
5. **Formation utilisateurs** : Documentation fournie

---

**📞 Support** : Consultez `/docs/GUIDE_UTILISATION.md` pour assistance