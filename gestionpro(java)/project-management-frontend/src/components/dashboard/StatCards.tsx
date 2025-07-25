"use client";

import { useState, useEffect } from "react";
import {
  ArrowTrendingUpIcon,
  ArrowTrendingDownIcon,
} from "@heroicons/react/24/solid";
import {
  ClockIcon,
  CheckCircleIcon,
  ExclamationCircleIcon,
  CurrencyEuroIcon,
  FolderIcon,
} from "@heroicons/react/24/outline";
import { StatCardData, statCardsData } from "@/lib/mocks/dashboard";
import { getDataFromMockOrApi } from "@/lib/mocks";
import { endpoints } from "@/lib/api";

interface StatCardProps {
  title: string;
  value: string | number;
  icon: React.ElementType;
  changePercent?: number;
  bgColor: string;
  textColor: string;
}

const StatCard = ({
  title,
  value,
  icon: Icon,
  changePercent,
  bgColor,
  textColor,
}: StatCardProps) => {
  return (
    <div className={`card p-5 ${bgColor}`}>
      <div className="flex justify-between items-start">
        <div>
          <p className={`text-sm font-medium ${textColor}`}>{title}</p>
          <h3 className={`text-2xl font-bold mt-1 ${textColor}`}>{value}</h3>
        </div>
        <div className={`p-2 rounded-lg bg-white/20`}>
          <Icon className={`h-6 w-6 ${textColor}`} />
        </div>
      </div>
      {changePercent !== undefined && (
        <div className="mt-4 flex items-center">
          {changePercent >= 0 ? (
            <ArrowTrendingUpIcon className="h-4 w-4 text-success mr-1" />
          ) : (
            <ArrowTrendingDownIcon className="h-4 w-4 text-error mr-1" />
          )}
          <span
            className={`text-xs font-medium ${
              changePercent >= 0 ? "text-success" : "text-error"
            }`}
          >
            {Math.abs(changePercent)}%{" "}
            {changePercent >= 0 ? "increase" : "decrease"}
          </span>
          <span className="text-xs text-neutral-medium ml-1">
            since last month
          </span>
        </div>
      )}
    </div>
  );
};

const StatCards = () => {
  const [stats, setStats] = useState<StatCardData[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    async function fetchStats() {
      try {
        setLoading(true);
        const data = await getDataFromMockOrApi(
          statCardsData,
          endpoints.dashboard.getSummary
        );
        setStats(data);
      } catch (err) {
        console.error("Failed to fetch dashboard summary:", err);
        setError("Impossible de charger les statistiques");
      } finally {
        setLoading(false);
      }
    }

    fetchStats();
  }, []);

  // Map stat type to icon
  const getIcon = (index: number): React.ElementType => {
    const icons: React.ElementType[] = [
      FolderIcon,
      ClockIcon,
      CheckCircleIcon,
      CurrencyEuroIcon,
    ];
    return icons[index % icons.length];
  };

  if (loading) {
    return (
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
        {[1, 2, 3, 4].map((placeholder) => (
          <div
            key={placeholder}
            className="card p-5 bg-neutral-100 animate-pulse"
          >
            <div className="h-16"></div>
          </div>
        ))}
      </div>
    );
  }

  if (error) {
    return (
      <div className="card p-5">
        <p className="text-error">{error}</p>
      </div>
    );
  }

  return (
    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
      {stats.map((stat, index) => (
        <StatCard key={index} {...stat} icon={getIcon(index)} />
      ))}
    </div>
  );
};

export default StatCards;
