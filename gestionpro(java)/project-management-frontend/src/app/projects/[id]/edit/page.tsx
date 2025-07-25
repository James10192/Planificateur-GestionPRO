"use client";

import { useState, useEffect } from "react";
import { useParams, useRouter } from "next/navigation";
import {
  ArrowLeftIcon,
  ExclamationCircleIcon,
} from "@heroicons/react/24/outline";
import { Button } from "@/components/ui/Button";
import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/Card";
import ProjectForm from "@/components/projects/ProjectForm";
import { endpoints } from "@/lib/api";
import { getErrorMessage } from "@/lib/utils";
import type { Project } from "@/types/project";

export default function EditProjectPage() {
  const [project, setProject] = useState<Project | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [saving, setSaving] = useState(false);
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

  const handleSubmit = async (projectData: Partial<Project>) => {
    setSaving(true);
    setError(null);

    try {
      await endpoints.projects.update(Number(id), projectData);
      router.push(`/projects/${id}`);
    } catch (err) {
      console.error("Failed to update project:", err);
      setError(getErrorMessage(err));
      setSaving(false);
    }
  };

  const handleError = (errorMessage: string) => {
    setError(errorMessage);
  };

  return (
    <div className="space-y-6">
      <div className="flex items-center gap-2">
        <Button
          variant="ghost"
          size="sm"
          onClick={() => router.push(`/projects/${id}`)}
          className="flex items-center gap-1"
        >
          <ArrowLeftIcon className="h-4 w-4" />
          Retour
        </Button>
        <h1 className="text-2xl font-bold">
          {loading
            ? "Chargement..."
            : project
            ? `Modifier: ${project.name}`
            : "Projet introuvable"}
        </h1>
      </div>

      {error && (
        <div className="bg-error/10 border border-error/30 rounded-md p-4 flex items-start gap-2 text-error">
          <ExclamationCircleIcon className="h-5 w-5 mt-0.5 flex-shrink-0" />
          <div>
            <p className="font-medium">
              {loading
                ? "Erreur lors du chargement du projet"
                : "Erreur lors de la mise à jour du projet"}
            </p>
            <p className="text-sm">{error}</p>
          </div>
        </div>
      )}

      {loading ? (
        <div className="animate-pulse space-y-6">
          <Card>
            <CardHeader>
              <div className="h-6 bg-neutral-200 rounded w-1/3"></div>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                {[...Array(6)].map((_, i) => (
                  <div key={i} className="grid grid-cols-2 gap-4">
                    <div className="h-10 bg-neutral-200 rounded"></div>
                    <div className="h-10 bg-neutral-200 rounded"></div>
                  </div>
                ))}
                <div className="h-24 bg-neutral-200 rounded w-full"></div>
                <div className="flex justify-end gap-2">
                  <div className="h-10 bg-neutral-200 rounded w-24"></div>
                  <div className="h-10 bg-neutral-200 rounded w-24"></div>
                </div>
              </div>
            </CardContent>
          </Card>
        </div>
      ) : project ? (
        <Card>
          <CardHeader>
            <CardTitle>Informations du projet</CardTitle>
          </CardHeader>
          <CardContent>
            <ProjectForm
              project={project}
              onSubmit={handleSubmit}
              onError={handleError}
            />
          </CardContent>
        </Card>
      ) : !error ? (
        <div className="text-center py-12">
          <p className="text-neutral-500">Projet introuvable</p>
          <Button
            variant="outline"
            size="sm"
            onClick={() => router.push("/projects")}
            className="mt-4"
          >
            Retour à la liste des projets
          </Button>
        </div>
      ) : null}
    </div>
  );
}
