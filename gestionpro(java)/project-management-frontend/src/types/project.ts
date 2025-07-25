export interface Project {
  id: number;
  name: string;
  description?: string;
  typeId: number;
  type?: ProjectType;
  statusId: number;
  status?: Status;
  priorityId: number;
  priority?: Priority;
  directionId: number;
  direction?: Direction;
  teamId?: number;
  team?: Team;
  startDate: string; // ISO date string format
  endDate?: string;
  actualEndDate?: string;
}

export interface ProjectType {
  id: number;
  name: string;
}

export interface Status {
  id: number;
  name: string;
}

export interface Priority {
  id: number;
  name: string;
}

export interface Direction {
  id: number;
  code: string;
  name: string;
}

export interface PortfolioPhase {
  id: number;
  name: string;
  percentage: number;
}

export interface TeamRole {
  id: number;
  name: string;
}

export interface User {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  phone?: string;
  directionId: number;
  direction?: Direction;
  function?: string;
}

export interface Team {
  id: number;
  name: string;
  members?: TeamMember[];
}

export interface TeamMember {
  teamId: number;
  userId: number;
  roleId: number;
  user?: User;
  role?: TeamRole;
}

export interface Planning {
  id: number;
  projectId: number;
  phaseId: number;
  project?: Project;
  phase?: PortfolioPhase;
}

export interface Action {
  id: number;
  planningId: number;
  title: string;
  statusId: number;
  userId: number;
  startDate: string;
  endDate?: string;
  actualEndDate?: string;
  progress: number;
  status?: Status;
  user?: User;
  planning?: Planning;
}

export interface SubAction {
  id: number;
  actionId: number;
  statusId: number;
  startDate: string;
  endDate?: string;
  actualEndDate?: string;
  status?: Status;
  action?: Action;
}

export interface ActionDependency {
  id: number;
  actionId: number;
  dependsOnActionId: number;
  action?: Action;
  dependsOnAction?: Action;
}

export interface Document {
  id: number;
  projectId: number;
  title: string;
  version: string;
  statusId: number;
  path?: string;
  uploadedById?: number;
  uploadDate: string;
  status?: Status;
  uploadedBy?: User;
  project?: Project;
}

export interface ProjectBudget {
  id: number;
  projectId: number;
  initialBudget: number;
  consumedBudget: number;
  project?: Project;
}
