"use client";

import { useState, useEffect } from "react";
import { format } from "date-fns";
import { fr } from "date-fns/locale";
import {
  ClockIcon,
  CheckCircleIcon,
  ExclamationTriangleIcon,
  ArrowPathIcon,
  FolderIcon,
  DocumentTextIcon,
} from "@heroicons/react/24/outline";
import { ActivityData, recentActivitiesData } from "@/lib/mocks/dashboard";
import { getDataFromMockOrApi } from "@/lib/mocks";
import { endpoints } from "@/lib/api";

const Activity = ({ activity }: { activity: ActivityData }) => {
  const getIcon = () => {
    switch (activity.type) {
      case "task":
        return <DocumentTextIcon className="h-5 w-5 text-info" />;
      case "project":
        return <FolderIcon className="h-5 w-5 text-primary" />;
      case "document":
        return <DocumentTextIcon className="h-5 w-5 text-accent" />;
      default:
        return <DocumentTextIcon className="h-5 w-5 text-info" />;
    }
  };

  const getStatusIcon = () => {
    switch (activity.status) {
      case "completed":
        return <CheckCircleIcon className="h-5 w-5 text-success" />;
      case "in_progress":
        return <ClockIcon className="h-5 w-5 text-info" />;
      case "delayed":
        return <ExclamationTriangleIcon className="h-5 w-5 text-error" />;
      case "updated":
        return <ArrowPathIcon className="h-5 w-5 text-warning" />;
      default:
        return <ClockIcon className="h-5 w-5 text-info" />;
    }
  };

  const getStatusText = () => {
    switch (activity.status) {
      case "completed":
        return "Terminé";
      case "in_progress":
        return "En cours";
      case "delayed":
        return "En retard";
      case "updated":
        return "Mis à jour";
      default:
        return "En cours";
    }
  };

  return (
    <div className="flex items-start space-x-4 mb-6 last:mb-0">
      <div className="bg-neutral-lightest p-2 rounded-lg">{getIcon()}</div>
      <div className="flex-1">
        <div className="flex justify-between">
          <h4 className="font-medium text-neutral-dark">{activity.title}</h4>
          <div className="flex items-center space-x-1">
            {getStatusIcon()}
            <span className="text-xs">{getStatusText()}</span>
          </div>
        </div>
        {activity.project && (
          <p className="text-sm text-neutral-medium mt-1">
            Projet: {activity.project}
          </p>
        )}
        <div className="flex justify-between mt-2">
          <p className="text-xs text-neutral-medium">Par {activity.user}</p>
          <p className="text-xs text-neutral-medium">
            {format(activity.date, "PPp", { locale: fr })}
          </p>
        </div>
      </div>
    </div>
  );
};

const RecentActivities = () => {
  const [activities, setActivities] = useState<ActivityData[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    async function fetchActivities() {
      try {
        setLoading(true);
        const data = await getDataFromMockOrApi(
          recentActivitiesData,
          endpoints.dashboard.getRecentActivities
        );
        setActivities(data);
      } catch (err) {
        console.error("Failed to fetch activities:", err);
        setError("Impossible de charger les activités récentes");
      } finally {
        setLoading(false);
      }
    }

    fetchActivities();
  }, []);

  if (loading) {
    return (
      <div className="card">
        <div className="flex justify-between items-center mb-6">
          <h3 className="text-lg font-semibold">Activités récentes</h3>
        </div>
        <div className="flex justify-center items-center py-8">
          <div className="h-8 w-8 animate-spin rounded-full border-4 border-primary border-t-transparent"></div>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="card">
        <div className="flex justify-between items-center mb-6">
          <h3 className="text-lg font-semibold">Activités récentes</h3>
        </div>
        <div className="py-6 text-center text-error">{error}</div>
      </div>
    );
  }

  return (
    <div className="card">
      <div className="flex justify-between items-center mb-6">
        <h3 className="text-lg font-semibold">Activités récentes</h3>
        <button className="text-sm text-primary font-medium hover:underline">
          Voir tout
        </button>
      </div>
      <div className="divide-y divide-neutral-light">
        {activities.map((activity) => (
          <div key={activity.id} className="py-4 first:pt-0 last:pb-0">
            <Activity activity={activity} />
          </div>
        ))}
      </div>
    </div>
  );
};

export default RecentActivities;
