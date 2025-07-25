"use client";

import { useState, useEffect } from "react";
import { useParams, useRouter } from "next/navigation";
import {
  ArrowLeftIcon,
  TrashIcon,
  ExclamationCircleIcon,
} from "@heroicons/react/24/outline";
import { Button } from "@/components/ui/Button";
import { Tabs, TabPanel } from "@/components/ui/Tabs";
import ProjectInfo from "@/components/projects/ProjectInfo";
import ProjectTeam from "@/components/projects/ProjectTeam";
import ProjectPlanning from "@/components/projects/ProjectPlanning";
import ProjectActions from "@/components/projects/ProjectActions";
import ProjectDocuments from "@/components/projects/ProjectDocuments";
import ProjectBudget from "@/components/projects/ProjectBudget";
import ProjectKpis from "@/components/projects/ProjectKpis";
import KpiDetail from "@/components/projects/KpiDetail";
import { endpoints } from "@/lib/api";
import { getErrorMessage } from "@/lib/utils";
import type { Project } from "@/types/project";
import type { KpiValue } from "@/types/kpi";

export default function ProjectDetailPage() {
  const [project, setProject] = useState<Project | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [activeTab, setActiveTab] = useState("info");
  const [selectedKpi, setSelectedKpi] = useState<KpiValue | null>(null);
  const { id } = useParams<{ id: string }>();
  const router = useRouter();

  useEffect(() => {
    const fetchProject = async () => {
      try {
        const response = await endpoints.projects.getById(Number(id));
        setProject(response.data);
        setError(null);
      } catch (err) {
        console.error("Failed to fetch project:", err);
        setError(getErrorMessage(err));
      } finally {
        setLoading(false);
      }
    };

    if (id) {
      fetchProject();
    }
  }, [id]);

  const handleDeleteClick = () => {
    // Would show confirmation dialog and handle delete logic
    if (
      confirm(
        "Êtes-vous sûr de vouloir supprimer ce projet ? Cette action est irréversible."
      )
    ) {
      router.push(`/projects/${id}/delete`);
    }
  };

  const handleKpiSelect = (kpi: KpiValue) => {
    setSelectedKpi(kpi);
  };

  const handleCloseKpiDetail = () => {
    setSelectedKpi(null);
  };

  const tabs = [
    { id: "info", label: "Informations" },
    { id: "team", label: "Équipe" },
    { id: "planning", label: "Planning" },
    { id: "actions", label: "Actions" },
    { id: "documents", label: "Documents" },
    { id: "budget", label: "Budget" },
    { id: "kpis", label: "KPIs" },
  ];

  return (
    <div className="space-y-6">
      {/* Header with back button and project name */}
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-3">
          <Button
            variant="ghost"
            size="sm"
            onClick={() => router.push("/projects")}
            className="flex items-center gap-1"
          >
            <ArrowLeftIcon className="h-4 w-4" />
            Retour
          </Button>
          <h1 className="text-2xl font-bold">
            {loading ? "Chargement..." : project?.name || "Projet introuvable"}
          </h1>
        </div>
        <div className="flex gap-2">
          <Button
            variant="outline"
            size="sm"
            onClick={handleDeleteClick}
            className="flex items-center gap-1 text-error hover:bg-error/10"
            disabled={loading || !project}
          >
            <TrashIcon className="h-4 w-4" />
            Supprimer
          </Button>
        </div>
      </div>

      {/* Error alert */}
      {error && (
        <div className="bg-error/10 border border-error/30 rounded-md p-4 flex items-start gap-2 text-error">
          <ExclamationCircleIcon className="h-5 w-5 mt-0.5 flex-shrink-0" />
          <div>
            <p className="font-medium">Erreur lors du chargement du projet</p>
            <p className="text-sm">{error}</p>
          </div>
        </div>
      )}

      {/* Tab navigation */}
      {!error && (
        <>
          <Tabs
            tabs={tabs}
            defaultTab="info"
            onChange={setActiveTab}
            className="mb-6"
          />

          {/* Tab content */}
          <div className="space-y-6">
            <TabPanel id="info" activeTab={activeTab}>
              <ProjectInfo project={project as Project} isLoading={loading} />
            </TabPanel>

            <TabPanel id="team" activeTab={activeTab}>
              <ProjectTeam project={project as Project} isLoading={loading} />
            </TabPanel>

            <TabPanel id="planning" activeTab={activeTab}>
              <ProjectPlanning
                project={project as Project}
                isLoading={loading}
              />
            </TabPanel>

            <TabPanel id="actions" activeTab={activeTab}>
              <ProjectActions
                project={project as Project}
                isLoading={loading}
              />
            </TabPanel>

            <TabPanel id="documents" activeTab={activeTab}>
              <ProjectDocuments
                project={project as Project}
                isLoading={loading}
              />
            </TabPanel>

            <TabPanel id="budget" activeTab={activeTab}>
              <ProjectBudget project={project as Project} isLoading={loading} />
            </TabPanel>

            <TabPanel id="kpis" activeTab={activeTab}>
              {selectedKpi && project ? (
                <KpiDetail
                  project={project}
                  kpiValue={selectedKpi}
                  onClose={handleCloseKpiDetail}
                />
              ) : (
                <ProjectKpis
                  project={project as Project}
                  onKpiSelect={handleKpiSelect}
                />
              )}
            </TabPanel>
          </div>
        </>
      )}
    </div>
  );
}
