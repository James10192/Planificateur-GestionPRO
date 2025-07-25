"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
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

export default function CreateProjectPage() {
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);
  const router = useRouter();

  const handleSubmit = async (projectData: Partial<Project>) => {
    setLoading(true);
    setError(null);

    try {
      await endpoints.projects.create(projectData);
      router.push("/projects");
    } catch (err) {
      console.error("Failed to create project:", err);
      setError(getErrorMessage(err));
    } finally {
      setLoading(false);
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
          onClick={() => router.back()}
          className="flex items-center gap-1"
        >
          <ArrowLeftIcon className="h-4 w-4" />
          Retour
        </Button>
        <h1 className="text-2xl font-bold">Nouveau Projet</h1>
      </div>

      {error && (
        <div className="bg-error/10 border border-error/30 rounded-md p-4 flex items-start gap-2 text-error">
          <ExclamationCircleIcon className="h-5 w-5 mt-0.5 flex-shrink-0" />
          <div>
            <p className="font-medium">Erreur lors de la création du projet</p>
            <p className="text-sm">{error}</p>
          </div>
        </div>
      )}

      <Card>
        <CardHeader>
          <CardTitle>Informations du projet</CardTitle>
        </CardHeader>
        <CardContent>
          <ProjectForm onSubmit={handleSubmit} onError={handleError} />
        </CardContent>
      </Card>
    </div>
  );
}
