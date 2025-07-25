# Project Management Frontend

Interface utilisateur pour le système de gestion de projets, développée avec Next.js, React et Tailwind CSS.

## Configuration requise

- Node.js 16.x ou supérieur
- npm 8.x ou supérieur

## Installation

```bash
# Installer les dépendances
npm install

# Installer cross-env pour les scripts avec variables d'environnement
npm install --save-dev cross-env
```

## Lancement de l'application

### Développement avec données simulées (mocks)

La configuration par défaut en mode développement utilise des données simulées pour permettre le développement sans dépendre du backend.

```bash
# Lancer l'application en mode développement avec mocks (par défaut)
npm run dev

# Ou explicitement avec mocks
npm run dev:mock
```

### Développement connecté au backend

Si vous souhaitez vous connecter au backend Java Spring Boot réel:

```bash
# Désactiver les mocks manuellement
# Le backend doit être en cours d'exécution sur http://localhost:8080
# (modifiable dans src/lib/api.ts)
npm run dev
```

## Architecture

### Structure des dossiers

```
project-management-frontend/
├── src/
│   ├── app/                   # Pages de l'application (Next.js App Router)
│   ├── components/            # Composants React réutilisables
│   │   ├── dashboard/         # Composants spécifiques au dashboard
│   │   ├── layout/            # Composants de mise en page
│   │   ├── projects/          # Composants liés aux projets
│   │   └── ui/                # Composants UI réutilisables (boutons, cartes, etc.)
│   ├── lib/                   # Bibliothèques et utilitaires
│   │   ├── api.ts             # Configuration de l'API
│   │   ├── utils.ts           # Fonctions utilitaires
│   │   └── mocks/             # Données simulées pour le développement
│   └── types/                 # Définitions de types TypeScript
├── public/                    # Fichiers statiques
└── ...
```

### Système de mocks

L'application utilise un système de mocks pour faciliter le développement sans dépendre du backend:

- Les mocks sont activés par défaut en mode développement
- Les données simulées sont dans `src/lib/mocks/`
- Chaque composant utilise la fonction `getDataFromMockOrApi()` pour récupérer les données

#### Configuration des mocks

Les mocks sont configurés dans `src/lib/api.ts`:

```typescript
// Par défaut, on utilise les mocks en développement
const USE_MOCKS =
  process.env.NODE_ENV === "development" ||
  process.env.NEXT_PUBLIC_USE_MOCKS === "true";
```

Pour désactiver les mocks, modifiez cette constante ou utilisez la variable d'environnement `NEXT_PUBLIC_USE_MOCKS=false`.

## Personnalisation

### Thème et styles

L'application utilise Tailwind CSS pour les styles. La configuration se trouve dans:

- `tailwind.config.js`: Configuration des couleurs, espacements, etc.
- `src/styles/globals.css`: Styles globaux

## Déploiement

Pour construire l'application pour la production:

```bash
# Construction avec mocks désactivés (pour la production)
npm run build

# Construction avec mocks activés (pour des démos)
npm run build:mock

# Démarrer l'application en mode production
npm start
```

## API Backend

L'API backend est configurée dans `src/lib/api.ts` et utilise Axios pour les requêtes HTTP.

La connexion au backend se fait par défaut sur `http://localhost:8080/api/v1`.
