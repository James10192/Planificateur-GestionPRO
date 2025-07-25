"use client";

import { useState, useEffect } from "react";
import { XMarkIcon, ArrowPathIcon } from "@heroicons/react/24/outline";
import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/Card";
import { Button } from "@/components/ui/Button";
import {
  Table,
  TableHeader,
  TableBody,
  TableRow,
  TableHead,
  TableCell,
} from "@/components/ui/Table";
import { endpoints } from "@/lib/api";
import { getDataFromMockOrApi, getMockKpiHistory } from "@/lib/mocks";
import { getErrorMessage, formatDate, formatDateTime } from "@/lib/utils";
import { KpiValue } from "@/types/kpi";
import type { Project } from "@/types/project";

interface KpiDetailProps {
  project: Project;
  kpiValue: KpiValue;
  onClose: () => void;
}

export default function KpiDetail({
  project,
  kpiValue,
  onClose,
}: KpiDetailProps) {
  const [history, setHistory] = useState<KpiValue[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [refreshing, setRefreshing] = useState(false);

  useEffect(() => {
    fetchKpiHistory();
  }, [kpiValue.metricId]);

  const fetchKpiHistory = async () => {
    try {
      setLoading(true);
      setError(null);

      const historyData = await getDataFromMockOrApi(
        getMockKpiHistory(
          project.id,
          kpiValue.metricId,
          project.name,
          kpiValue.metricName
        ),
        () => endpoints.kpis.getProjectKpiHistory(project.id, kpiValue.metricId)
      );

      setHistory(historyData);
    } catch (err) {
      console.error("Error fetching KPI history:", err);
      setError(getErrorMessage(err));
    } finally {
      setLoading(false);
    }
  };

  const refreshHistory = async () => {
    try {
      setRefreshing(true);
      setError(null);
      await fetchKpiHistory();
    } catch (err) {
      console.error("Error refreshing KPI history:", err);
      setError(getErrorMessage(err));
    } finally {
      setRefreshing(false);
    }
  };

  const renderHistoryChart = () => {
    if (history.length === 0) return null;

    // Sort by date
    const sortedHistory = [...history].sort(
      (a, b) =>
        new Date(a.measurementDate).getTime() -
        new Date(b.measurementDate).getTime()
    );

    // Calculate chart dimensions
    const chartHeight = 200;
    const chartWidth = "100%";
    const padding = 40;

    // Find min and max values
    const values = sortedHistory.map((h) => h.value);
    const minValue = Math.min(...values);
    const maxValue = Math.max(...values);
    const valueRange = maxValue - minValue;

    // Calculate point positions
    const points = sortedHistory.map((h, index) => {
      const x = `${(index / (sortedHistory.length - 1)) * 100}%`;
      const normalizedValue =
        valueRange === 0 ? 0.5 : (h.value - minValue) / valueRange;
      const y =
        chartHeight - (normalizedValue * (chartHeight - padding * 2) + padding);
      return { x, y, value: h.value, date: h.measurementDate };
    });

    // Create SVG path
    let path = "";
    points.forEach((point, index) => {
      if (index === 0) {
        path += `M ${point.x} ${point.y}`;
      } else {
        path += ` L ${point.x} ${point.y}`;
      }
    });

    return (
      <div className="mt-6 mb-8">
        <h3 className="text-sm font-medium mb-4">Évolution</h3>
        <div className="relative" style={{ height: `${chartHeight}px` }}>
          <svg
            width={chartWidth}
            height={chartHeight}
            className="overflow-visible"
          >
            {/* Y-axis labels */}
            <text x="0" y={padding} className="text-xs fill-neutral-500">
              {maxValue.toFixed(1)}
              {kpiValue.metricCode === "COMPLETION_RATE" ||
              kpiValue.metricCode === "BUDGET_UTILIZATION" ||
              kpiValue.metricCode === "TASK_COMPLETION"
                ? "%"
                : ""}
            </text>
            <text
              x="0"
              y={chartHeight - padding}
              className="text-xs fill-neutral-500"
            >
              {minValue.toFixed(1)}
              {kpiValue.metricCode === "COMPLETION_RATE" ||
              kpiValue.metricCode === "BUDGET_UTILIZATION" ||
              kpiValue.metricCode === "TASK_COMPLETION"
                ? "%"
                : ""}
            </text>

            {/* Line chart */}
            <path
              d={path}
              fill="none"
              stroke="currentColor"
              strokeWidth="2"
              className="text-primary"
            />

            {/* Data points */}
            {points.map((point, index) => (
              <g key={index}>
                <circle
                  cx={point.x}
                  cy={point.y}
                  r="4"
                  className="fill-primary"
                />
              </g>
            ))}
          </svg>

          {/* X-axis labels */}
          <div className="flex justify-between mt-2">
            {points.length > 0 && (
              <>
                <span className="text-xs text-neutral-500">
                  {formatDate(new Date(points[0].date))}
                </span>
                {points.length > 2 && (
                  <span className="text-xs text-neutral-500">
                    {formatDate(
                      new Date(points[Math.floor(points.length / 2)].date)
                    )}
                  </span>
                )}
                <span className="text-xs text-neutral-500">
                  {formatDate(new Date(points[points.length - 1].date))}
                </span>
              </>
            )}
          </div>
        </div>
      </div>
    );
  };

  return (
    <Card className="w-full">
      <CardHeader className="flex flex-row items-center justify-between pb-2">
        <CardTitle>{kpiValue.metricName}</CardTitle>
        <div className="flex gap-2">
          <Button
            variant="outline"
            size="sm"
            onClick={refreshHistory}
            disabled={refreshing}
            className="flex items-center gap-1"
          >
            <ArrowPathIcon
              className={`h-4 w-4 ${refreshing ? "animate-spin" : ""}`}
            />
            Rafraîchir
          </Button>
          <Button
            variant="ghost"
            size="sm"
            onClick={onClose}
            className="flex items-center gap-1"
          >
            <XMarkIcon className="h-4 w-4" />
            Fermer
          </Button>
        </div>
      </CardHeader>
      <CardContent>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
          <div className="bg-neutral-100 p-4 rounded">
            <div className="text-sm font-medium text-neutral-500">
              Valeur actuelle
            </div>
            <div className="font-semibold text-xl">
              {kpiValue.value.toFixed(1)}
              {kpiValue.metricCode === "COMPLETION_RATE" ||
              kpiValue.metricCode === "BUDGET_UTILIZATION" ||
              kpiValue.metricCode === "TASK_COMPLETION"
                ? "%"
                : ""}
              {kpiValue.metricCode === "DEADLINE_PROXIMITY" ? " jours" : ""}
            </div>
          </div>

          <div className="bg-neutral-100 p-4 rounded">
            <div className="text-sm font-medium text-neutral-500">
              Date de mesure
            </div>
            <div className="font-semibold">
              {formatDateTime(new Date(kpiValue.measurementDate))}
            </div>
          </div>

          <div className="bg-neutral-100 p-4 rounded">
            <div className="text-sm font-medium text-neutral-500">Statut</div>
            <div className="font-semibold">
              {kpiValue.criticalThresholdBreached
                ? "Critique"
                : kpiValue.warningThresholdBreached
                ? "Attention"
                : "Bon"}
            </div>
          </div>
        </div>

        {loading ? (
          <div className="animate-pulse space-y-4">
            <div className="h-40 bg-neutral-200 rounded"></div>
            <div className="h-40 bg-neutral-200 rounded"></div>
          </div>
        ) : error ? (
          <div className="text-error">Erreur: {error}</div>
        ) : history.length === 0 ? (
          <div className="text-center py-8 text-neutral-500">
            Aucun historique disponible pour cet indicateur
          </div>
        ) : (
          <>
            {renderHistoryChart()}

            <h3 className="text-sm font-medium mb-4">Historique des mesures</h3>
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Date</TableHead>
                  <TableHead>Valeur</TableHead>
                  <TableHead>Commentaire</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {[...history]
                  .sort(
                    (a, b) =>
                      new Date(b.measurementDate).getTime() -
                      new Date(a.measurementDate).getTime()
                  )
                  .map((h) => (
                    <TableRow key={h.id || h.measurementDate}>
                      <TableCell>
                        {formatDateTime(new Date(h.measurementDate))}
                      </TableCell>
                      <TableCell className="font-medium">
                        {h.value.toFixed(1)}
                        {h.metricCode === "COMPLETION_RATE" ||
                        h.metricCode === "BUDGET_UTILIZATION" ||
                        h.metricCode === "TASK_COMPLETION"
                          ? "%"
                          : ""}
                        {h.metricCode === "DEADLINE_PROXIMITY" ? " jours" : ""}
                      </TableCell>
                      <TableCell>{h.comment || "-"}</TableCell>
                    </TableRow>
                  ))}
              </TableBody>
            </Table>
          </>
        )}
      </CardContent>
    </Card>
  );
}
