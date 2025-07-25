import axios from "axios";

// Environnement configuration
// Par défaut, on utilise les mocks en développement
// En production, cela devrait être configuré via les variables d'environnement
const USE_MOCKS =
  process.env.NODE_ENV === "development" ||
  process.env.NEXT_PUBLIC_USE_MOCKS === "true";
const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL || "/api/v1";

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

// Add response interceptor for handling errors
api.interceptors.response.use(
  (response) => response,
  (error) => {
    // Handle errors here (e.g., global toast notifications)
    console.error("API Error:", error.response?.data || error.message);
    return Promise.reject(error);
  }
);

// API endpoints grouped by resource
export const endpoints = {
  projects: {
    getAll: () => api.get("/projects"),
    getById: (id: number) => api.get(`/projects/${id}`),
    create: (data: any) => api.post("/projects", data),
    update: (id: number, data: any) => api.put(`/projects/${id}`, data),
    delete: (id: number) => api.delete(`/projects/${id}`),
    getActions: (id: number) => api.get(`/projects/${id}/actions`),
  },
  actions: {
    getAll: () => api.get("/actions"),
    getById: (id: number) => api.get(`/actions/${id}`),
    create: (data: any) => api.post("/actions", data),
    update: (id: number, data: any) => api.put(`/actions/${id}`, data),
    delete: (id: number) => api.delete(`/actions/${id}`),
    getSubActions: (id: number) => api.get(`/actions/${id}/sub-actions`),
  },
  plannings: {
    getAll: () => api.get("/plannings"),
    getById: (id: number) => api.get(`/plannings/${id}`),
    create: (data: any) => api.post("/plannings", data),
    update: (id: number, data: any) => api.put(`/plannings/${id}`, data),
    delete: (id: number) => api.delete(`/plannings/${id}`),
  },
  parameters: {
    getProjectTypes: () => api.get("/parameters/project-types"),
    getStatuses: () => api.get("/parameters/statuses"),
    getPriorities: () => api.get("/parameters/priorities"),
    getDirections: () => api.get("/parameters/directions"),
    getPortfolioPhases: () => api.get("/parameters/portfolio-phases"),
    getTeamRoles: () => api.get("/parameters/team-roles"),
  },
  users: {
    getAll: () => api.get("/users"),
    getById: (id: number) => api.get(`/users/${id}`),
    create: (data: any) => api.post("/users", data),
    update: (id: number, data: any) => api.put(`/users/${id}`, data),
    delete: (id: number) => api.delete(`/users/${id}`),
  },
  teams: {
    getAll: () => api.get("/teams"),
    getById: (id: number) => api.get(`/teams/${id}`),
    create: (data: any) => api.post("/teams", data),
    update: (id: number, data: any) => api.put(`/teams/${id}`, data),
    delete: (id: number) => api.delete(`/teams/${id}`),
    getMembers: (id: number) => api.get(`/teams/${id}/members`),
  },
  dashboard: {
    getSummary: () => api.get("/dashboard/summary"),
    getRecentActivities: () => api.get("/dashboard/activities"),
    getTasksByUser: (userId: number) =>
      api.get(`/dashboard/tasks/user/${userId}`),
  },
  kpis: {
    getMetrics: () => api.get("/kpis/metrics"),
    getMetricById: (id: number) => api.get(`/kpis/metrics/${id}`),
    createMetric: (data: any) => api.post("/kpis/metrics", data),
    updateMetric: (id: number, data: any) =>
      api.put(`/kpis/metrics/${id}`, data),
    deleteMetric: (id: number) => api.delete(`/kpis/metrics/${id}`),

    getProjectKpis: (projectId: number) =>
      api.get(`/kpis/projects/${projectId}`),
    getProjectKpiHistory: (projectId: number, metricId: number) =>
      api.get(`/kpis/projects/${projectId}/metrics/${metricId}/history`),
    recordKpiValue: (projectId: number, metricId: number, data: any) =>
      api.post(`/kpis/projects/${projectId}/metrics/${metricId}`, data),
    calculateKpi: (projectId: number, metricId: number) =>
      api.post(`/kpis/projects/${projectId}/metrics/${metricId}/calculate`),
    exportKpiData: (projectId: number, format: string) =>
      api.get(`/kpis/projects/${projectId}/export?format=${format}`, {
        responseType: "blob",
      }),
  },
};

// Expose the api instance and configuration
export const apiConfig = {
  USE_MOCKS,
  API_BASE_URL,
};

console.log("[API Config] Using mocks:", USE_MOCKS);

export default api;
