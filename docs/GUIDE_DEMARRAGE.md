# 🚀 Guide de Démarrage Rapide

## 📋 Prérequis

### Pour l'environnement local (Windows + XAMPP)
- **Java 11** : Amazon Corretto JDK 11
- **Maven 3.9.6** : Apache Maven 
- **Node.js 18+** : Pour le frontend Next.js
- **XAMPP** : Apache + MySQL + phpMyAdmin

### Pour le serveur RHEL
- **Java 11** : OpenJDK ou Oracle JDK
- **Payara Server 6.x** : Serveur d'application
- **Oracle Database 12c+** : Base de données production

## ⚡ Démarrage Local (Windows)

### 1. Configuration XAMPP
```powershell
# 1. Configurer XAMPP et créer la base de données
.\scripts\windows\setup_xampp.ps1

# 2. Démarrer l'application complète
.\scripts\windows\start_xampp_app.ps1
```

### 2. Accès à l'application
- **Frontend** : http://localhost:3000
- **Backend API** : http://localhost:8080/nsia
- **phpMyAdmin** : http://localhost/phpmyadmin

### 3. Connexion de test
- **Email** : admin@nsia.ci  
- **Mot de passe** : admin

## 🐧 Déploiement Serveur RHEL

### 1. Configuration serveur
```bash
# Préparer l'environnement serveur
sudo ./scripts/linux/setup_rhel_server.sh
```

### 2. Déploiement application
```bash
# Déployer sur le serveur de test
./scripts/linux/deploy_test_server.sh --oracle-password VOTRE_PASSWORD
```

## 📁 Structure du Projet

```
📁 Planificateur-GestionPRO/
├── 📁 scripts/
│   ├── 📁 windows/           # Scripts Windows + XAMPP
│   ├── 📁 linux/             # Scripts RHEL + Oracle  
│   └── 📁 sql/               # Scripts base de données
├── 📁 docs/                  # Documentation
├── 📁 src/                   # Backend Java Spring Boot
├── 📁 project-management-frontend/  # Frontend Next.js
└── 📄 pom.xml               # Configuration Maven
```

## 🔧 Commandes Utiles

### Backend (Spring Boot)
```bash
# Compilation
mvn clean compile -P local-mysql

# Tests
mvn test

# Démarrage
mvn spring-boot:run -P local-mysql
```

### Frontend (Next.js)
```bash
cd project-management-frontend

# Installation
npm install

# Développement
npm run dev

# Build production
npm run build
```

## 🛠️ Résolution de Problèmes

### Port déjà utilisé
```bash
# Vérifier les ports 3000 et 8080
netstat -ano | findstr ":3000"
netstat -ano | findstr ":8080"
```

### Erreur base de données
1. Vérifiez que XAMPP/MySQL est démarré
2. Importez le script `scripts/sql/create_mysql_database.sql`
3. Vérifiez la configuration dans `application-local-mysql.yml`

### Erreur compilation Java
1. Vérifiez Java 11 : `java -version`
2. Vérifiez Maven : `mvn -version`
3. Nettoyez le cache : `mvn clean`

## 📞 Support

- **Documentation technique** : Consultez `/docs/`
- **Logs** : Vérifiez les logs dans `/logs/`
- **Configuration** : Fichiers dans `/src/main/resources/`