# 🏗️ Architecture du Système

## 📊 Vue d'Ensemble

Le **Planificateur-GestionPRO** est une application web full-stack moderne basée sur une architecture microservices avec séparation frontend/backend.

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Frontend      │    │    Backend      │    │   Base de       │
│   Next.js       │◄──►│   Spring Boot   │◄──►│   Données       │
│   (Port 3000)   │    │   (Port 8080)   │    │  MySQL/Oracle   │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

## 🎯 Technologies Utilisées

### Frontend
- **Next.js 14** : Framework React avec SSR
- **React 18** : Bibliothèque UI moderne
- **TypeScript** : Typage statique
- **Tailwind CSS** : Framework CSS utilitaire
- **Recharts** : Graphiques et visualisations

### Backend  
- **Java 11** : Langage de programmation
- **Spring Boot 3.2.5** : Framework d'application
- **Spring Data JPA** : Couche d'accès aux données
- **Spring Security** : Authentification et autorisation
- **Maven** : Gestionnaire de dépendances

### Base de Données
- **MySQL 8.0** : Développement local (XAMPP)
- **Oracle 12c+** : Production (serveurs RHEL)
- **H2** : Tests unitaires

### Serveurs
- **Payara Server 6.x** : Serveur d'application Jakarta EE
- **Apache HTTP Server** : Serveur web (XAMPP)

## 📁 Structure du Code

### Backend (`/src/main/java/com/projectmanagement/`)

```
📁 controller/           # Endpoints REST API
├── ProjectController    # Gestion des projets
├── UserController      # Gestion des utilisateurs  
├── DashboardController # Tableaux de bord
└── ...

📁 service/             # Logique métier
├── ProjectService     # Services projets
├── UserService       # Services utilisateurs
└── ...

📁 repository/         # Couche d'accès données
├── ProjectRepository  # Repository projets
├── UserRepository    # Repository utilisateurs
└── ...

📁 entity/            # Entités JPA
├── Project          # Entité projet
├── User            # Entité utilisateur
└── ...

📁 dto/              # Objets de transfert
├── ProjectDTO      # DTO projet
├── UserDTO        # DTO utilisateur
└── ...

📁 config/          # Configuration
├── JpaConfig      # Configuration JPA
├── SecurityConfig # Configuration sécurité
└── ...
```

### Frontend (`/project-management-frontend/src/`)

```
📁 app/                    # Pages Next.js App Router
├── dashboard/            # Tableau de bord
├── projects/            # Gestion projets
└── layout.tsx          # Layout principal

📁 components/           # Composants réutilisables
├── ui/                 # Composants UI de base
├── layout/            # Composants de mise en page
├── projects/         # Composants projets
└── dashboard/       # Composants tableau de bord

📁 lib/                # Utilitaires et services
├── api.ts            # Client API
├── utils.ts         # Fonctions utilitaires
└── mocks/          # Données de démonstration

📁 types/             # Types TypeScript
├── project.ts       # Types projets
├── user.ts         # Types utilisateurs
└── ...
```

## 🔄 Flux de Données

### 1. Authentification
```
User Login → JWT Token → Stockage Local → Headers API
```

### 2. Gestion des Projets
```
Frontend Form → API Call → Service Layer → Repository → Database
                  ↓
Database → Repository → Service → Controller → JSON Response → Frontend
```

### 3. Tableaux de Bord
```
Dashboard Load → Multiple API Calls → Aggregated Data → Charts & KPIs
```

## 🗄️ Modèle de Données

### Entités Principales

```sql
-- Projets
tbpro (Projects)
├── idpro (PK)
├── nompro (Nom)
├── despro (Description)
├── idtyp → ptyp (Type)
├── idsta → psta (Statut)
├── idprio → pprio (Priorité)
└── ...

-- Utilisateurs  
tbutil (Users)
├── idutil (PK)
├── nom, prenom
├── email (UNIQUE)
├── motpasse (Hash)
└── role (ADMIN/MANAGER/USER)

-- Équipes
tbequipro (Project Teams)
├── idpro → tbpro
├── idutil → tbutil
└── roleequipe
```

## 🔒 Sécurité

### Authentification
- **JWT Tokens** : Authentification stateless
- **Hachage BCrypt** : Mots de passe sécurisés
- **Sessions** : Gestion automatique côté client

### Autorisation
- **Rôles** : ADMIN, MANAGER, USER
- **Permissions** : Basées sur les rôles et projets
- **CORS** : Configuration restrictive

### Audit
- **Audit Trail** : Traçabilité des actions
- **Logs** : Journalisation détaillée
- **Timestamps** : Création/modification automatique

## 🚀 Déploiement

### Environnements

| Environnement | Base de Données | Serveur | URL |
|---------------|----------------|---------|-----|
| **Développement** | MySQL (XAMPP) | Embedded Tomcat | http://localhost:8080 |
| **Test** | Oracle 12c | Payara Server | http://test-server:8080 |
| **Production** | Oracle 19c | Payara Cluster | https://nsia-projects.ci |

### Profils Maven
- **local-mysql** : Développement local
- **oracle-test** : Serveur de test  
- **oracle-prod** : Production

## 📈 Performance

### Optimisations Backend
- **Connection Pooling** : HikariCP
- **Caching** : Spring Cache + Redis
- **Pagination** : JPA Pageable
- **Lazy Loading** : Relations JPA

### Optimisations Frontend
- **Code Splitting** : Next.js automatique
- **SSR/SSG** : Rendu côté serveur
- **Image Optimization** : Next.js Image
- **Bundle Analysis** : Webpack Bundle Analyzer

## 🔍 Monitoring

### Métriques
- **Spring Boot Actuator** : Métriques JVM/Application
- **Micrometer** : Métriques personnalisées
- **Health Checks** : Vérifications automatiques

### Logs
- **Logback** : Framework de logging
- **Structured Logging** : Format JSON
- **Log Aggregation** : Centralisation possible

## 🧪 Tests

### Backend
```bash
# Tests unitaires
mvn test

# Tests d'intégration  
mvn verify

# Couverture de code
mvn clean test jacoco:report
```

### Frontend
```bash
# Tests Jest
npm test

# Tests E2E (Playwright)
npm run test:e2e

# Lint/Format
npm run lint
```

## 🔧 Configuration

### Profiles d'Application
- **application.properties** : Configuration de base
- **application-local-mysql.yml** : MySQL local
- **application-oracle-test.yml** : Oracle test
- **application-oracle-prod.yml** : Oracle production

### Variables d'Environnement
- **JAVA_HOME** : Chemin Java 11
- **SPRING_PROFILES_ACTIVE** : Profil actif
- **DATABASE_URL** : URL base de données
- **JWT_SECRET** : Clé secrète JWT