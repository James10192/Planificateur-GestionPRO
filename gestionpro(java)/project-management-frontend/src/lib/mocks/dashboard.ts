/**
 * Dashboard mock data
 * This file contains mock data for the dashboard components
 * In production, these should be replaced with data from the API
 */

import { formatDate } from "../utils";

// Types
export interface StatCardData {
  title: string;
  value: string | number;
  changePercent?: number;
  bgColor: string;
  textColor: string;
}

export interface ActivityData {
  id: number;
  type: "task" | "project" | "document";
  title: string;
  status: "completed" | "in_progress" | "delayed" | "updated";
  date: Date;
  user: string;
  project?: string;
}

export interface TaskData {
  id: number;
  title: string;
  project: string;
  priority: "high" | "medium" | "low";
  dueDate: Date;
  status: "pending" | "in_progress" | "completed";
  progress: number;
}

export interface ProjectStatusData {
  name: string;
  count: number;
  color: string;
}

export interface BudgetData {
  month: string;
  planned: number;
  actual: number;
}

// Mock Data
export const statCardsData: StatCardData[] = [
  {
    title: "Total Projets",
    value: 24,
    changePercent: 12.5,
    bgColor: "bg-primary",
    textColor: "text-white",
  },
  {
    title: "En cours",
    value: 15,
    changePercent: 8.2,
    bgColor: "bg-info",
    textColor: "text-white",
  },
  {
    title: "Terminés",
    value: 8,
    changePercent: 24.3,
    bgColor: "bg-success",
    textColor: "text-white",
  },
  {
    title: "Budget Total",
    value: "€ 1.2M",
    changePercent: -3.5,
    bgColor: "bg-accent",
    textColor: "text-white",
  },
];

export const recentActivitiesData: ActivityData[] = [
  {
    id: 1,
    type: "task",
    title: "Mise à jour de la documentation API",
    status: "completed",
    date: new Date(2023, 10, 5, 14, 30),
    user: "Laurent Dupont",
    project: "Modernisation Système",
  },
  {
    id: 2,
    type: "project",
    title: "Lancement projet Mobile Banking",
    status: "in_progress",
    date: new Date(2023, 10, 5, 10, 15),
    user: "Marie Leclerc",
  },
  {
    id: 3,
    type: "document",
    title: "Cahier des charges v2.1",
    status: "updated",
    date: new Date(2023, 10, 4, 16, 45),
    user: "Thomas Bernard",
    project: "Sécurisation Données",
  },
  {
    id: 4,
    type: "task",
    title: "Test d'intégration paiement",
    status: "delayed",
    date: new Date(2023, 10, 4, 11, 20),
    user: "Sophie Martin",
    project: "Mobile Banking",
  },
];

export const tasksData: TaskData[] = [
  {
    id: 1,
    title: "Finaliser les spécifications techniques",
    project: "Système CRM",
    priority: "high",
    dueDate: new Date(2023, 10, 10),
    status: "in_progress",
    progress: 75,
  },
  {
    id: 2,
    title: "Intégration API paiement",
    project: "Mobile Banking",
    priority: "high",
    dueDate: new Date(2023, 10, 8),
    status: "pending",
    progress: 20,
  },
  {
    id: 3,
    title: "Révision architecture de sécurité",
    project: "Sécurisation Données",
    priority: "medium",
    dueDate: new Date(2023, 10, 15),
    status: "in_progress",
    progress: 45,
  },
  {
    id: 4,
    title: "Tests de performance",
    project: "Modernisation Système",
    priority: "low",
    dueDate: new Date(2023, 10, 20),
    status: "pending",
    progress: 0,
  },
  {
    id: 5,
    title: "Documentation utilisateur",
    project: "Système CRM",
    priority: "medium",
    dueDate: new Date(2023, 10, 12),
    status: "completed",
    progress: 100,
  },
];

export const projectStatusData: ProjectStatusData[] = [
  { name: "En cours", count: 15, color: "#3b82f6" },
  { name: "Terminés", count: 8, color: "#10b981" },
  { name: "Non démarrés", count: 3, color: "#6b7280" },
  { name: "En retard", count: 2, color: "#ef4444" },
];

export const budgetData: BudgetData[] = [
  { month: "Jan", planned: 50000, actual: 48000 },
  { month: "Fév", planned: 60000, actual: 65000 },
  { month: "Mar", planned: 70000, actual: 68000 },
  { month: "Avr", planned: 80000, actual: 75000 },
  { month: "Mai", planned: 65000, actual: 60000 },
  { month: "Juin", planned: 90000, actual: 100000 },
];

// Helper function to get mock data with a delay to simulate API call
export const getMockData = async <T>(data: T, delay = 500): Promise<T> => {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve(data);
    }, delay);
  });
};
