import { KpiMetric, KpiValue } from "@/types/kpi";

// Mock KPI Metrics
export const mockKpiMetrics: KpiMetric[] = [
  {
    id: 1,
    code: "COMPLETION_RATE",
    name: "Taux d'avancement",
    description: "Pourcentage d'avancement global du projet",
    unit: "%",
    thresholdWarning: 50,
    thresholdCritical: 25,
    higherIsBetter: true,
    calculationFormula: "Calculate average progress of all actions",
    updateFrequencyMinutes: 1440, // Daily
    enableNotifications: true,
    createdAt: "2024-05-01T00:00:00Z",
    updatedAt: "2024-05-01T00:00:00Z",
    active: true,
  },
  {
    id: 2,
    code: "BUDGET_UTILIZATION",
    name: "Utilisation du budget",
    description: "Pourcentage du budget consommé par rapport au budget initial",
    unit: "%",
    thresholdWarning: 85,
    thresholdCritical: 95,
    higherIsBetter: false,
    calculationFormula: "USED_BUDGET / TOTAL_BUDGET * 100",
    updateFrequencyMinutes: 1440, // Daily
    enableNotifications: true,
    createdAt: "2024-05-01T00:00:00Z",
    updatedAt: "2024-05-01T00:00:00Z",
    active: true,
  },
  {
    id: 3,
    code: "TASK_COMPLETION",
    name: "Tâches terminées",
    description: "Pourcentage de tâches terminées",
    unit: "%",
    thresholdWarning: 40,
    thresholdCritical: 20,
    higherIsBetter: true,
    calculationFormula: "COMPLETED_TASKS / TOTAL_TASKS * 100",
    updateFrequencyMinutes: 1440, // Daily
    enableNotifications: true,
    createdAt: "2024-05-01T00:00:00Z",
    updatedAt: "2024-05-01T00:00:00Z",
    active: true,
  },
  {
    id: 4,
    code: "DEADLINE_PROXIMITY",
    name: "Proximité de l'échéance",
    description: "Jours restants avant la date d'échéance du projet",
    unit: "jours",
    thresholdWarning: 10,
    thresholdCritical: 5,
    higherIsBetter: true,
    calculationFormula: "END_DATE - CURRENT_DATE",
    updateFrequencyMinutes: 1440, // Daily
    enableNotifications: true,
    createdAt: "2024-05-01T00:00:00Z",
    updatedAt: "2024-05-01T00:00:00Z",
    active: true,
  },
  {
    id: 5,
    code: "RISK_INDEX",
    name: "Indice de risque",
    description:
      "Indice calculé basé sur les risques identifiés et leur criticité",
    unit: "indice",
    thresholdWarning: 50,
    thresholdCritical: 75,
    higherIsBetter: false,
    calculationFormula: "SUM(RISK_PROBABILITY * RISK_IMPACT) / COUNT(RISKS)",
    updateFrequencyMinutes: 1440, // Daily
    enableNotifications: true,
    createdAt: "2024-05-01T00:00:00Z",
    updatedAt: "2024-05-01T00:00:00Z",
    active: true,
  },
];

// Create mock KPI values for a specific project
export const getMockKpiValuesForProject = (
  projectId: number,
  projectName: string
): KpiValue[] => {
  return [
    {
      id: 1,
      metricId: 1,
      metricCode: "COMPLETION_RATE",
      metricName: "Taux d'avancement",
      projectId,
      projectName,
      value: 65.7,
      measurementDate: new Date().toISOString(),
      comment: "Calcul automatique",
      warningThresholdBreached: false,
      criticalThresholdBreached: false,
      notificationSent: false,
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
      active: true,
    },
    {
      id: 2,
      metricId: 2,
      metricCode: "BUDGET_UTILIZATION",
      metricName: "Utilisation du budget",
      projectId,
      projectName,
      value: 78.3,
      measurementDate: new Date().toISOString(),
      comment: "Calcul automatique",
      warningThresholdBreached: false,
      criticalThresholdBreached: false,
      notificationSent: false,
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
      active: true,
    },
    {
      id: 3,
      metricId: 3,
      metricCode: "TASK_COMPLETION",
      metricName: "Tâches terminées",
      projectId,
      projectName,
      value: 58.2,
      measurementDate: new Date().toISOString(),
      comment: "Calcul automatique",
      warningThresholdBreached: false,
      criticalThresholdBreached: false,
      notificationSent: false,
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
      active: true,
    },
    {
      id: 4,
      metricId: 4,
      metricCode: "DEADLINE_PROXIMITY",
      metricName: "Proximité de l'échéance",
      projectId,
      projectName,
      value: 15,
      measurementDate: new Date().toISOString(),
      comment: "Calcul automatique",
      warningThresholdBreached: false,
      criticalThresholdBreached: false,
      notificationSent: false,
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
      active: true,
    },
    {
      id: 5,
      metricId: 5,
      metricCode: "RISK_INDEX",
      metricName: "Indice de risque",
      projectId,
      projectName,
      value: 35.5,
      measurementDate: new Date().toISOString(),
      comment: "Calcul automatique",
      warningThresholdBreached: false,
      criticalThresholdBreached: false,
      notificationSent: false,
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
      active: true,
    },
  ];
};

// Mock KPI history for a specific project and metric
export const getMockKpiHistory = (
  projectId: number,
  metricId: number,
  projectName: string,
  metricName: string
): KpiValue[] => {
  // Generate data for the last 10 days
  const today = new Date();
  const history: KpiValue[] = [];

  for (let i = 9; i >= 0; i--) {
    const date = new Date(today);
    date.setDate(date.getDate() - i);

    // Generate a value based on the metric ID
    let value = 0;
    switch (metricId) {
      case 1: // COMPLETION_RATE
        value = 30 + i * 4 + Math.random() * 5;
        break;
      case 2: // BUDGET_UTILIZATION
        value = 20 + i * 7 + Math.random() * 3;
        break;
      case 3: // TASK_COMPLETION
        value = 25 + i * 4.5 + Math.random() * 4;
        break;
      case 4: // DEADLINE_PROXIMITY
        value = 25 - i * 1;
        break;
      case 5: // RISK_INDEX
        value = 50 - i * 2 + Math.random() * 6;
        break;
      default:
        value = 50 + Math.random() * 10;
    }

    // Cap the value between 0 and 100
    value = Math.max(0, Math.min(100, value));

    history.push({
      id: 100 + i,
      metricId,
      metricCode: mockKpiMetrics.find((m) => m.id === metricId)?.code || "",
      metricName,
      projectId,
      projectName,
      value,
      measurementDate: date.toISOString(),
      comment: "Mesure historique",
      warningThresholdBreached: false,
      criticalThresholdBreached: false,
      notificationSent: false,
      createdAt: date.toISOString(),
      updatedAt: date.toISOString(),
      active: true,
    });
  }

  return history;
};
