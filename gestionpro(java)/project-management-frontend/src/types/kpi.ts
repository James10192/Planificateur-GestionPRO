import { BaseModel } from "@/types/common";

/**
 * Represents a KPI metric definition
 */
export interface KpiMetric extends BaseModel {
  code: string;
  name: string;
  description?: string;
  unit?: string;
  thresholdWarning?: number;
  thresholdCritical?: number;
  higherIsBetter: boolean;
  calculationFormula?: string;
  updateFrequencyMinutes?: number;
  enableNotifications: boolean;
}

/**
 * Represents a KPI value measurement for a project
 */
export interface KpiValue extends BaseModel {
  metricId: number;
  metricCode: string;
  metricName: string;
  projectId: number;
  projectName: string;
  value: number;
  measurementDate: string;
  comment?: string;
  warningThresholdBreached?: boolean;
  criticalThresholdBreached?: boolean;
  notificationSent?: boolean;
}

/**
 * Status of a KPI value based on thresholds
 */
export enum KpiStatus {
  Critical = "critical",
  Warning = "warning",
  Good = "good",
  Unknown = "unknown",
}

/**
 * Types of KPI visualizations
 */
export enum KpiVisualizationType {
  Number = "number",
  Gauge = "gauge",
  Chart = "chart",
  Table = "table",
}
