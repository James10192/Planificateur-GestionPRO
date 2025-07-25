"use client";

import { useState, useEffect } from "react";
import { format } from "date-fns";
import { fr } from "date-fns/locale";
import {
  CheckIcon,
  PencilIcon,
  EyeIcon,
  ClockIcon,
  ExclamationTriangleIcon,
} from "@heroicons/react/24/outline";
import { TaskData, tasksData } from "@/lib/mocks/dashboard";
import { getDataFromMockOrApi } from "@/lib/mocks";
import { endpoints } from "@/lib/api";

const TaskRow = ({ task }: { task: TaskData }) => {
  const getStatusBadge = () => {
    switch (task.status) {
      case "completed":
        return (
          <span className="flex items-center text-xs px-2 py-1 rounded-full bg-success/10 text-success">
            <CheckIcon className="w-3 h-3 mr-1" />
            Terminé
          </span>
        );
      case "in_progress":
        return (
          <span className="flex items-center text-xs px-2 py-1 rounded-full bg-info/10 text-info">
            <ClockIcon className="w-3 h-3 mr-1" />
            En cours
          </span>
        );
      case "pending":
        return (
          <span className="flex items-center text-xs px-2 py-1 rounded-full bg-warning/10 text-warning">
            <ClockIcon className="w-3 h-3 mr-1" />
            En attente
          </span>
        );
      default:
        return null;
    }
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case "completed":
        return "bg-success";
      case "in_progress":
        return "bg-info";
      case "pending":
        return "bg-warning";
      default:
        return "bg-neutral";
    }
  };

  const getPriorityColor = (priority: string) => {
    switch (priority) {
      case "high":
        return "text-error";
      case "medium":
        return "text-warning";
      case "low":
        return "text-success";
      default:
        return "text-neutral-medium";
    }
  };

  return (
    <tr className="hover:bg-neutral-lightest">
      <td className="py-3 px-4">
        <div className="flex items-center">
          <div
            className={`h-2 w-2 rounded-full mr-2 ${getStatusColor(
              task.status
            )}`}
          ></div>
          <div>
            <p className="font-medium">{task.title}</p>
            <p className="text-xs text-neutral-medium">{task.project}</p>
          </div>
        </div>
      </td>
      <td className="py-3 px-4">{getStatusBadge()}</td>
      <td className="py-3 px-4">
        <span
          className={`text-sm font-medium ${getPriorityColor(task.priority)}`}
        >
          {task.priority === "high"
            ? "Haute"
            : task.priority === "medium"
            ? "Moyenne"
            : "Basse"}
        </span>
      </td>
      <td className="py-3 px-4 text-sm">
        {format(task.dueDate, "dd MMM yyyy", { locale: fr })}
      </td>
      <td className="py-3 px-4">
        <div className="w-full bg-neutral-light rounded-full h-2">
          <div
            className={`h-2 rounded-full ${getStatusColor(task.status)}`}
            style={{ width: `${task.progress}%` }}
          ></div>
        </div>
        <p className="text-xs text-center mt-1">{task.progress}%</p>
      </td>
      <td className="py-3 px-4">
        <div className="flex space-x-2 justify-end">
          <button className="p-1 rounded-full bg-neutral-lightest hover:bg-neutral-light text-neutral-medium">
            <EyeIcon className="w-4 h-4" />
          </button>
          <button className="p-1 rounded-full bg-neutral-lightest hover:bg-neutral-light text-neutral-medium">
            <PencilIcon className="w-4 h-4" />
          </button>
          <button className="p-1 rounded-full bg-success/10 hover:bg-success/20 text-success">
            <CheckIcon className="w-4 h-4" />
          </button>
        </div>
      </td>
    </tr>
  );
};

const MyTasks = () => {
  const [tasks, setTasks] = useState<TaskData[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    async function fetchTasks() {
      try {
        setLoading(true);
        const data = await getDataFromMockOrApi(
          tasksData,
          // Remplacer par l'endpoint réel quand il sera disponible
          () => endpoints.dashboard.getTasksByUser(1) // 1 est un ID utilisateur fictif pour les mocks
        );
        setTasks(data);
      } catch (err) {
        console.error("Failed to fetch tasks:", err);
        setError("Impossible de charger les tâches");
      } finally {
        setLoading(false);
      }
    }

    fetchTasks();
  }, []);

  if (loading) {
    return (
      <div className="card overflow-hidden">
        <div className="flex justify-between items-center mb-6">
          <h3 className="text-lg font-semibold">Mes Tâches</h3>
        </div>
        <div className="flex justify-center py-10">
          <div className="h-8 w-8 animate-spin rounded-full border-4 border-primary border-t-transparent"></div>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="card overflow-hidden">
        <div className="flex justify-between items-center mb-6">
          <h3 className="text-lg font-semibold">Mes Tâches</h3>
        </div>
        <div className="py-6 text-center text-error">{error}</div>
      </div>
    );
  }

  return (
    <div className="card overflow-hidden">
      <div className="flex justify-between items-center mb-6">
        <h3 className="text-lg font-semibold">Mes Tâches</h3>
        <div className="flex space-x-2">
          <button className="btn-secondary text-xs py-1">
            + Nouvelle tâche
          </button>
          <button className="text-sm text-primary font-medium hover:underline">
            Voir tout
          </button>
        </div>
      </div>
      <div className="overflow-x-auto -mx-6">
        <table className="w-full min-w-full">
          <thead className="bg-neutral-lightest border-y border-neutral-light">
            <tr>
              <th className="py-3 px-4 text-left text-xs font-medium text-neutral-medium uppercase tracking-wider">
                Tâche
              </th>
              <th className="py-3 px-4 text-left text-xs font-medium text-neutral-medium uppercase tracking-wider">
                Statut
              </th>
              <th className="py-3 px-4 text-left text-xs font-medium text-neutral-medium uppercase tracking-wider">
                Priorité
              </th>
              <th className="py-3 px-4 text-left text-xs font-medium text-neutral-medium uppercase tracking-wider">
                Échéance
              </th>
              <th className="py-3 px-4 text-left text-xs font-medium text-neutral-medium uppercase tracking-wider">
                Progression
              </th>
              <th className="py-3 px-4 text-right text-xs font-medium text-neutral-medium uppercase tracking-wider">
                Actions
              </th>
            </tr>
          </thead>
          <tbody className="divide-y divide-neutral-light">
            {tasks.map((task) => (
              <TaskRow key={task.id} task={task} />
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default MyTasks;
