"use client";

import React from "react";
import { useState, useEffect } from "react";
import { Pie } from "react-chartjs-2";
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from "chart.js";
import { projectStatusData } from "@/lib/mocks/dashboard";
import { getDataFromMockOrApi } from "@/lib/mocks";
import { endpoints } from "@/lib/api";

// Register required Chart.js components
ChartJS.register(ArcElement, Tooltip, Legend);

const ProjectSummary = () => {
  const [statusData, setStatusData] = useState(projectStatusData);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    async function fetchStatusData() {
      try {
        setLoading(true);
        // En production, ceci appellerait l'API réelle
        const data = await getDataFromMockOrApi(
          projectStatusData,
          // Remplacer par l'endpoint réel quand il sera disponible
          () => endpoints.dashboard.getSummary()
        );
        setStatusData(data);
      } catch (err) {
        console.error("Failed to fetch project status data:", err);
        setError("Impossible de charger les données de statut des projets");
      } finally {
        setLoading(false);
      }
    }

    fetchStatusData();
  }, []);

  const chartData = {
    labels: statusData.map((item) => item.name),
    datasets: [
      {
        data: statusData.map((item) => item.count),
        backgroundColor: statusData.map((item) => item.color),
        borderColor: Array(statusData.length).fill("#ffffff"),
        borderWidth: 2,
      },
    ],
  };

  const chartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: "bottom" as const,
        labels: {
          usePointStyle: true,
          pointStyle: "circle",
          padding: 20,
          font: {
            size: 12,
          },
        },
      },
      tooltip: {
        backgroundColor: "#ffffff",
        titleColor: "#2D3748",
        bodyColor: "#2D3748",
        borderColor: "#E2E8F0",
        borderWidth: 1,
        padding: 12,
        bodyFont: {
          size: 14,
        },
        titleFont: {
          size: 14,
          weight: "bold" as const,
        },
        callbacks: {
          label: function (context: any) {
            const total = context.dataset.data.reduce(
              (a: number, b: number) => a + b,
              0
            );
            const value = context.raw;
            const percentage = Math.round((value / total) * 100);
            return `${context.label}: ${value} (${percentage}%)`;
          },
        },
      },
    },
  };

  if (loading) {
    return (
      <div className="card">
        <div className="flex justify-between items-center mb-6">
          <h3 className="text-lg font-semibold">
            Vue d&apos;ensemble des projets
          </h3>
        </div>
        <div className="h-64 flex items-center justify-center">
          <div className="h-8 w-8 animate-spin rounded-full border-4 border-primary border-t-transparent"></div>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="card">
        <div className="flex justify-between items-center mb-6">
          <h3 className="text-lg font-semibold">
            Vue d&apos;ensemble des projets
          </h3>
        </div>
        <div className="h-64 flex items-center justify-center">
          <p className="text-error">{error}</p>
        </div>
      </div>
    );
  }

  return (
    <div className="card">
      <div className="flex justify-between items-center mb-6">
        <h3 className="text-lg font-semibold">
          Vue d&apos;ensemble des projets
        </h3>
        <button className="text-sm text-primary font-medium hover:underline">
          Voir tout
        </button>
      </div>
      <div className="h-64">
        <Pie data={chartData} options={chartOptions} />
      </div>
      <div className="grid grid-cols-3 gap-4 mt-6">
        {statusData.slice(0, 3).map((status, index) => (
          <div key={index} className="text-center">
            <p className="text-2xl font-bold" style={{ color: status.color }}>
              {status.count}
            </p>
            <p className="text-sm text-neutral-medium">{status.name}</p>
          </div>
        ))}
      </div>
    </div>
  );
};

export default ProjectSummary;
