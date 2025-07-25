"use client";

import { useState, useEffect } from "react";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
  ChartOptions,
} from "chart.js";
import { Bar } from "react-chartjs-2";
import { BudgetData, budgetData } from "@/lib/mocks/dashboard";
import { getDataFromMockOrApi } from "@/lib/mocks";
import { endpoints } from "@/lib/api";
import { formatCurrency } from "@/lib/utils";

// Register ChartJS components
ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend
);

const BudgetChart = () => {
  const [chartData, setChartData] = useState<BudgetData[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    async function fetchBudgetData() {
      try {
        setLoading(true);
        // En production, ceci appellerait l'API réelle
        const data = await getDataFromMockOrApi(
          budgetData,
          // Remplacer par l'endpoint réel quand il sera disponible
          () => endpoints.dashboard.getSummary()
        );
        setChartData(data);
      } catch (err) {
        console.error("Failed to fetch budget data:", err);
        setError("Impossible de charger les données budgétaires");
      } finally {
        setLoading(false);
      }
    }

    fetchBudgetData();
  }, []);

  const chartOptions: ChartOptions<"bar"> = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: "top",
        labels: {
          font: {
            family: "Inter, sans-serif",
            size: 12,
          },
          usePointStyle: true,
          padding: 20,
        },
      },
      title: {
        display: false,
      },
      tooltip: {
        callbacks: {
          label: function (context) {
            const label = context.dataset.label || "";
            const value = context.parsed.y;
            return `${label}: ${formatCurrency(value)}`;
          },
        },
      },
    },
    scales: {
      x: {
        grid: {
          display: false,
        },
        ticks: {
          font: {
            family: "Inter, sans-serif",
          },
        },
      },
      y: {
        grid: {
          color: (context) => {
            return "rgba(0, 0, 0, 0.1)";
          },
          display: true,
        },
        ticks: {
          font: {
            family: "Inter, sans-serif",
          },
          callback: function (value) {
            return formatCurrency(value as number).replace("€", "") + " €";
          },
        },
      },
    },
  };

  if (loading) {
    return (
      <div className="card">
        <div className="p-6">
          <h3 className="text-lg font-semibold mb-6">Budget par mois</h3>
          <div className="h-64 flex items-center justify-center">
            <div className="h-8 w-8 animate-spin rounded-full border-4 border-primary border-t-transparent"></div>
          </div>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="card">
        <div className="p-6">
          <h3 className="text-lg font-semibold mb-6">Budget par mois</h3>
          <div className="h-64 flex items-center justify-center">
            <p className="text-error">{error}</p>
          </div>
        </div>
      </div>
    );
  }

  const data = {
    labels: chartData.map((item) => item.month),
    datasets: [
      {
        label: "Budget prévu",
        data: chartData.map((item) => item.planned),
        backgroundColor: "rgba(59, 130, 246, 0.8)", // blue
        borderRadius: 4,
      },
      {
        label: "Budget consommé",
        data: chartData.map((item) => item.actual),
        backgroundColor: "rgba(249, 115, 22, 0.8)", // orange
        borderRadius: 4,
      },
    ],
  };

  return (
    <div className="card">
      <div className="p-6">
        <h3 className="text-lg font-semibold mb-6">Budget par mois</h3>
        <div className="h-64">
          <Bar options={chartOptions} data={data} />
        </div>
      </div>
    </div>
  );
};

export default BudgetChart;
