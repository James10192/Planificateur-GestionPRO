# PROJECT_MEMORY - PROJECT_Management Frontend

## Project Overview

- Name: PROJECT_Management Frontend
- Description: Interface utilisateur du système de gestion de projet NSIA
- Version: 0.1.0
- Author: NSIA Team

## Project Structure

```
project-management-frontend/
├── src/
│   ├── app/                      # Application routes (Next.js App Router)
│   │   ├── layout.tsx            # Main layout
│   │   ├── page.tsx              # Home page (redirect to dashboard)
│   │   ├── dashboard/            # Dashboard page
│   │   ├── projects/             # Projects pages
│   │   │   ├── page.tsx          # Project list page
│   │   │   ├── create/           # Project creation page
│   │   │   └── [id]/             # Project detail pages (dynamic route)
│   │   │       ├── page.tsx      # Project detail page
│   │   │       ├── edit/         # Project edit page
│   │   │       └── delete/       # Project delete confirmation page
│   │   ├── actions/              # Actions management pages
│   │   ├── plannings/            # Planning management pages
│   │   └── parameters/           # Parameters management pages
│   ├── components/               # Reusable components
│   │   ├── ui/                   # UI components (buttons, inputs, etc.)
│   │   ├── layout/               # Layout components
│   │   ├── dashboard/            # Dashboard-specific components
│   │   └── projects/             # Project-specific components
│   ├── lib/                      # Utility functions and API services
│   ├── types/                    # TypeScript type definitions
│   └── styles/                   # Global styles
├── public/                       # Static assets
├── next.config.js                # Next.js configuration
├── tailwind.config.js            # Tailwind CSS configuration
└── package.json                  # Dependencies and scripts
```

## Technologies

- Next.js 14
- React 18
- TypeScript
- Tailwind CSS
- Heroicons
- Axios for API calls
- React Hook Form (for forms)
- TanStack Query (formerly React Query)
- date-fns (for date formatting)
- Recharts (for charts and data visualization)

## UI Components

The frontend includes reusable UI components that follow consistent design patterns:

- **Button**: Supports multiple variants (default, outline, ghost, destructive), sizes (sm, md, lg), and states (loading, disabled)
- **Card**: Container component with headers, footers, and flexible content areas
- **Badge**: Used for status indicators with variant colors (blue, green, yellow, red, gray)
- **Table**: Responsive table component with headers, rows, and cells
- **Input**: Text input component with support for icons, validation, and different states
- **Select**: Dropdown select component that matches Input styles
- **Tabs**: Accessible tab interface with keyboard navigation and animated indicator

## Project Pages

The project management section includes the following pages:

1. **Project List Page** (`/projects`)

   - Displays a searchable, filterable list of all projects
   - Includes project cards with key information (name, status, progress)
   - Quick actions for creating new projects

2. **Project Creation Page** (`/projects/create`)

   - Form for creating new projects
   - Input validation and error handling
   - Fields for all required project information

3. **Project Detail Page** (`/projects/[id]`)

   - Tab-based interface with sections for:
     - Project Info: Basic project details and metadata
     - Team: Team member management and roles
     - Planning: Project phases and timeline
     - Actions: Tasks and sub-tasks with progress tracking
     - Documents: Document management with upload/download
     - Budget: Budget tracking and visualization

4. **Project Edit Page** (`/projects/[id]/edit`)

   - Form for editing existing project information
   - Populated with current project data
   - Input validation and error handling

5. **Project Delete Confirmation** (`/projects/[id]/delete`)
   - Confirmation interface for project deletion
   - Warnings about cascade deletion effects
   - Requires explicit confirmation

## Project Components

Components specific to project management functionality:

- **ProjectInfo**: Displays project overview information and metadata
- **ProjectTeam**: Shows team members with roles and contact information
- **ProjectPlanning**: Visualizes project phases with progress indicators
- **ProjectActions**: Lists project actions/tasks with filtering and progress tracking
- **ProjectDocuments**: Document management with search and download capabilities
- **ProjectBudget**: Budget visualization with consumption metrics and status indicators
- **ProjectForm**: Reusable form component for project creation and editing

## API Integration

The frontend communicates with the backend using the following pattern:

- **API Service Layer**: `lib/api.ts` provides a structured interface to backend endpoints
- **Resource-Based Organization**: Endpoints are grouped by resource (projects, actions, etc.)
- **Error Handling**: Consistent error handling and messaging
- **Mock Support**: Development mode includes mock data support
- **Authentication**: JWT token support for authenticated requests

## State Management

- **React Hooks**: Local component state with useState and useEffect
- **Context API**: For shared state that spans multiple components
- **API Caching**: Data fetching with caching and revalidation

## Styling Approach

- **Tailwind CSS**: Utility-first CSS framework for consistent styling
- **Component Variants**: Consistent component variants with composition pattern
- **Responsive Design**: Mobile-first approach with breakpoints for larger screens
- **Accessibility**: WCAG AA compliance with proper contrast, focus states, and ARIA attributes
- **Animation**: Subtle animations for loading states and transitions

## Error Handling

The frontend implements comprehensive error handling:

- **Form Validation**: Client-side validation with visual feedback
- **API Errors**: Structured error handling for API requests
- **Loading States**: Skeleton loaders and loading indicators
- **Empty States**: User-friendly empty state displays with actions
- **404 Handling**: Graceful handling of missing resources

## Implementation Progress

### Completed

- ✅ Project structure and configuration
- ✅ UI component library (Button, Card, Badge, Table, Input, Select, Tabs)
- ✅ Dashboard page with project summaries and quick stats
- ✅ Project list page with search and filtering
- ✅ Project creation page with validation
- ✅ Project detail page with tab-based interface
- ✅ Project edit page with form validation
- ✅ Project delete confirmation page

### In Progress

- ⏳ Parameters management pages
- ⏳ MapStruct mappers for entity-DTO conversion

### Pending

- ⏳ Planning management pages
- ⏳ Action management pages
- ⏳ JWT authentication integration

## Future Enhancements

Planned improvements for the frontend:

- **Real-time Updates**: WebSocket integration for collaborative features
- **Offline Support**: Service worker for offline capabilities
- **Advanced Filtering**: More sophisticated filtering and search capabilities
- **Performance Optimization**: Code splitting and performance monitoring
- **User Preferences**: Saved user preferences for views and filters
- **Multi-language Support**: Internationalization framework

## Recent Implementations

Recent significant improvements:

1. **Project Detail Page Implementation**

   - Created a comprehensive tab-based interface for project details
   - Implemented six distinct tab panels for different aspects of project management
   - Added responsive design elements for all viewports

2. **Project Edit Functionality**

   - Implemented edit form with pre-populated fields
   - Added validation and error handling
   - Created intuitive navigation between view and edit modes

3. **UI Component Library Expansion**

   - Added Tabs component with accessibility features
   - Enhanced Badge component with contextual variants
   - Improved Card components for consistent content display

4. **Delete Confirmation Modal**
   - Added safeguards against accidental deletion
   - Implemented clear warning messages about deletion consequences
   - Created dedicated deletion confirmation page
