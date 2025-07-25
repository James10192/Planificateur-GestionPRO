"use client";

import { useState, useEffect } from "react";
import {
  ArrowPathIcon,
  DocumentArrowDownIcon,
} from "@heroicons/react/24/outline";
import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/Card";
import { Button } from "@/components/ui/Button";
import { Badge } from "@/components/ui/Badge";
import {
  Table,
  TableHeader,
  TableBody,
  TableRow,
  TableHead,
  TableCell,
} from "@/components/ui/Table";
import { endpoints } from "@/lib/api";
import { getDataFromMockOrApi, getMockKpiValuesForProject } from "@/lib/mocks";
import { getErrorMessage, formatDate } from "@/lib/utils";
import { KpiValue, KpiStatus } from "@/types/kpi";
import type { Project } from "@/types/project";

interface ProjectKpisProps {
  project: Project;
  onKpiSelect?: (kpi: KpiValue) => void;
}

const KpiStatusColors: Record<KpiStatus, string> = {
  [KpiStatus.Critical]: "bg-error",
  [KpiStatus.Warning]: "bg-warning",
  [KpiStatus.Good]: "bg-success",
  [KpiStatus.Unknown]: "bg-neutral-400",
};

export default function ProjectKpis({
  project,
  onKpiSelect,
}: ProjectKpisProps) {
  const [kpis, setKpis] = useState<KpiValue[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [refreshing, setRefreshing] = useState(false);

  useEffect(() => {
    fetchKpis();
  }, [project.id]);

  const fetchKpis = async () => {
    try {
      setLoading(true);
      setError(null);

      const kpiData = await getDataFromMockOrApi(
        getMockKpiValuesForProject(project.id, project.name),
        () => endpoints.kpis.getProjectKpis(project.id)
      );

      setKpis(kpiData);
    } catch (err) {
      console.error("Error fetching KPIs:", err);
      setError(getErrorMessage(err));
    } finally {
      setLoading(false);
    }
  };

  const refreshKpis = async () => {
    try {
      setRefreshing(true);
      setError(null);
      await fetchKpis();
    } catch (err) {
      console.error("Error refreshing KPIs:", err);
      setError(getErrorMessage(err));
    } finally {
      setRefreshing(false);
    }
  };

  const exportKpis = async (format: string) => {
    try {
      const response = await endpoints.kpis.exportKpiData(project.id, format);

      // Create a blob and download
      const blob = new Blob([response.data], {
        type:
          format === "csv"
            ? "text/csv"
            : format === "excel"
            ? "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            : "application/pdf",
      });

      const url = window.URL.createObjectURL(blob);
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute(
        "download",
        `kpi-report-${project.name.replace(/\s+/g, "-")}.${format}`
      );
      document.body.appendChild(link);
      link.click();
      link.remove();
    } catch (err) {
      console.error("Error exporting KPIs:", err);
      setError(getErrorMessage(err));
    }
  };

  const getKpiStatus = (kpi: KpiValue): KpiStatus => {
    if (kpi.criticalThresholdBreached) {
      return KpiStatus.Critical;
    } else if (kpi.warningThresholdBreached) {
      return KpiStatus.Warning;
    } else {
      return KpiStatus.Good;
    }
  };

  const renderKpiValue = (kpi: KpiValue) => {
    const status = getKpiStatus(kpi);

    return (
      <div className="flex items-center justify-between">
        <div className="flex items-center">
          <div
            className={`w-3 h-3 rounded-full mr-2 ${KpiStatusColors[status]}`}
          ></div>
          <span className="font-medium text-lg">
            {kpi.value.toFixed(1)}
            {kpi.metricCode === "COMPLETION_RATE" ||
            kpi.metricCode === "BUDGET_UTILIZATION" ||
            kpi.metricCode === "TASK_COMPLETION"
              ? "%"
              : ""}
            {kpi.metricCode === "DEADLINE_PROXIMITY" ? " jours" : ""}
          </span>
        </div>
      </div>
    );
  };

  return (
    <Card>
      <CardHeader className="flex flex-row items-center justify-between pb-2">
        <CardTitle>Indicateurs clés de performance (KPIs)</CardTitle>
        <div className="flex gap-2">
          <Button
            variant="outline"
            size="sm"
            onClick={() => exportKpis("excel")}
            className="flex items-center gap-1"
          >
            <DocumentArrowDownIcon className="h-4 w-4" />
            Excel
          </Button>
          <Button
            variant="outline"
            size="sm"
            onClick={refreshKpis}
            disabled={refreshing}
            className="flex items-center gap-1"
          >
            <ArrowPathIcon
              className={`h-4 w-4 ${refreshing ? "animate-spin" : ""}`}
            />
            Rafraîchir
          </Button>
        </div>
      </CardHeader>
      <CardContent>
        {loading ? (
          <div className="animate-pulse space-y-4">
            {[...Array(4)].map((_, i) => (
              <div key={i} className="h-14 bg-neutral-200 rounded"></div>
            ))}
          </div>
        ) : error ? (
          <div className="text-error">Erreur: {error}</div>
        ) : kpis.length === 0 ? (
          <div className="text-center py-8 text-neutral-500">
            Aucun KPI disponible pour ce projet
          </div>
        ) : (
          <div className="space-y-4">
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
              {kpis.map((kpi) => (
                <Card
                  key={kpi.id}
                  className={`border border-neutral-200 ${
                    onKpiSelect
                      ? "hover:border-primary cursor-pointer transition-colors"
                      : ""
                  }`}
                  onClick={() => onKpiSelect && onKpiSelect(kpi)}
                >
                  <CardContent className="p-4">
                    <div className="flex flex-col space-y-2">
                      <div className="text-sm font-medium text-neutral-500">
                        {kpi.metricName}
                      </div>
                      {renderKpiValue(kpi)}
                      <div className="text-xs text-neutral-400">
                        Mis à jour: {formatDate(new Date(kpi.measurementDate))}
                      </div>
                    </div>
                  </CardContent>
                </Card>
              ))}
            </div>

            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Indicateur</TableHead>
                  <TableHead>Valeur</TableHead>
                  <TableHead>État</TableHead>
                  <TableHead>Date de mesure</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {kpis.map((kpi) => (
                  <TableRow
                    key={kpi.id || `${kpi.projectId}-${kpi.metricId}`}
                    className={
                      onKpiSelect ? "hover:bg-neutral-50 cursor-pointer" : ""
                    }
                    onClick={() => onKpiSelect && onKpiSelect(kpi)}
                  >
                    <TableCell className="font-medium">
                      {kpi.metricName}
                    </TableCell>
                    <TableCell>
                      {kpi.value.toFixed(1)}
                      {kpi.metricCode === "COMPLETION_RATE" ||
                      kpi.metricCode === "BUDGET_UTILIZATION" ||
                      kpi.metricCode === "TASK_COMPLETION"
                        ? "%"
                        : ""}
                      {kpi.metricCode === "DEADLINE_PROXIMITY" ? " jours" : ""}
                    </TableCell>
                    <TableCell>
                      <Badge
                        variant={
                          getKpiStatus(kpi) === KpiStatus.Critical
                            ? "destructive"
                            : getKpiStatus(kpi) === KpiStatus.Warning
                            ? "warning"
                            : "success"
                        }
                      >
                        {getKpiStatus(kpi) === KpiStatus.Critical
                          ? "Critique"
                          : getKpiStatus(kpi) === KpiStatus.Warning
                          ? "Attention"
                          : "Bon"}
                      </Badge>
                    </TableCell>
                    <TableCell className="text-neutral-500">
                      {formatDate(new Date(kpi.measurementDate))}
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </div>
        )}
      </CardContent>
    </Card>
  );
}
