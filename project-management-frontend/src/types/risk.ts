import { BaseModel } from "@/types/common";
import { User, Project, Status, Action } from "@/types/project";

/**
 * Risk categories enum
 */
export enum RiskCategory {
  TECHNICAL = "TECHNICAL",
  ORGANIZATIONAL = "ORGANIZATIONAL",
  FINANCIAL = "FINANCIAL",
  LEGAL = "LEGAL",
  REGULATORY = "REGULATORY",
  HUMAN = "HUMAN",
  FUNCTIONAL = "FUNCTIONAL",
  SCHEDULE = "SCHEDULE",
  SECURITY = "SECURITY",
  OPERATIONAL = "OPERATIONAL",
  OTHER = "OTHER",
}

/**
 * Risk category display names mapping
 */
export const RiskCategoryLabels: Record<RiskCategory, string> = {
  [RiskCategory.TECHNICAL]: "Technique",
  [RiskCategory.ORGANIZATIONAL]: "Organisationnel",
  [RiskCategory.FINANCIAL]: "Financier",
  [RiskCategory.LEGAL]: "Juridique",
  [RiskCategory.REGULATORY]: "Réglementaire",
  [RiskCategory.HUMAN]: "Humain",
  [RiskCategory.FUNCTIONAL]: "Fonctionnel",
  [RiskCategory.SCHEDULE]: "Planning",
  [RiskCategory.SECURITY]: "Sécurité",
  [RiskCategory.OPERATIONAL]: "Opérationnel",
  [RiskCategory.OTHER]: "Autre",
};

/**
 * Risk interface
 */
export interface Risk extends BaseModel {
  id: number;
  title: string;
  description?: string;
  projectId: number;
  project?: Project;
  actionId?: number;
  action?: Action;
  statusId: number;
  status?: Status;
  probability: number; // 0-100
  impact: number; // 0-100
  criticality: number; // calculated: probability * impact / 100
  category: RiskCategory;
  identificationDate: string; // ISO date string
  resolutionDate?: string; // ISO date string
  mitigationMeasures?: string;
  responsibleId?: number;
  responsible?: User;
}

/**
 * Risk DTO for forms and API interactions
 */
export interface RiskFormData {
  title: string;
  description?: string;
  projectId: number;
  actionId?: number;
  statusId: number;
  probability: number;
  impact: number;
  category: RiskCategory;
  mitigationMeasures?: string;
  responsibleId?: number;
}
