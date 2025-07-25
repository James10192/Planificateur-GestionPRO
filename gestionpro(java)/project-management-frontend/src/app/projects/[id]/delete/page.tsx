"use client";

import { useState, useEffect } from "react";
import { useParams, useRouter } from "next/navigation";
import {
  ArrowLeftIcon,
  ExclamationTriangleIcon,
  ExclamationCircleIcon,
} from "@heroicons/react/24/outline";
import { Button } from "@/components/ui/Button";
import { Card, CardContent } from "@/components/ui/Card";
import { endpoints } from "@/lib/api";
import { getErrorMessage } from "@/lib/utils";
import type { Project } from "@/types/project";

export default function DeleteProjectPage() {
  const [project, setProject] = useState<Project | null>(null);
  const [loading, setLoading] = useState(true);
  const [deleting, setDeleting] = useState(false);
  const [error, setError] = useState<string | null>(null);
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

  const handleConfirmDelete = async () => {
    setDeleting(true);
    setError(null);

    try {
      await endpoints.projects.delete(Number(id));
      router.push("/projects");
    } catch (err) {
      console.error("Failed to delete project:", err);
      setError(getErrorMessage(err));
      setDeleting(false);
    }
  };

  return (
    <div className="max-w-2xl mx-auto space-y-6">
      <div className="flex items-center gap-2">
        <Button
          variant="ghost"
          size="sm"
          onClick={() => router.push(`/projects/${id}`)}
          className="flex items-center gap-1"
          disabled={deleting}
        >
          <ArrowLeftIcon className="h-4 w-4" />
          Retour
        </Button>
        <h1 className="text-2xl font-bold">Supprimer le projet</h1>
      </div>

      {error && (
        <div className="bg-error/10 border border-error/30 rounded-md p-4 flex items-start gap-2 text-error">
          <ExclamationCircleIcon className="h-5 w-5 mt-0.5 flex-shrink-0" />
          <div>
            <p className="font-medium">
              {loading
                ? "Erreur lors du chargement du projet"
                : "Erreur lors de la suppression du projet"}
            </p>
            <p className="text-sm">{error}</p>
          </div>
        </div>
      )}

      {loading ? (
        <Card className="animate-pulse">
          <CardContent className="p-6">
            <div className="flex items-center gap-3 mb-4">
              <div className="h-8 w-8 bg-neutral-200 rounded-full"></div>
              <div className="h-6 bg-neutral-200 rounded w-3/4"></div>
            </div>
            <div className="space-y-2">
              <div className="h-4 bg-neutral-200 rounded w-full"></div>
              <div className="h-4 bg-neutral-200 rounded w-full"></div>
              <div className="h-4 bg-neutral-200 rounded w-full"></div>
            </div>
            <div className="flex justify-end gap-2 mt-6">
              <div className="h-10 bg-neutral-200 rounded w-24"></div>
              <div className="h-10 bg-neutral-200 rounded w-24"></div>
            </div>
          </CardContent>
        </Card>
      ) : project ? (
        <Card>
          <CardContent className="p-6">
            <div className="flex items-start gap-3 mb-4 text-error">
              <ExclamationTriangleIcon className="h-8 w-8 flex-shrink-0" />
              <div>
                <h2 className="text-lg font-semibold">
                  Êtes-vous sûr de vouloir supprimer ce projet ?
                </h2>
                <p className="text-neutral-500 mt-1">
                  Vous êtes sur le point de supprimer le projet{" "}
                  <span className="font-medium">{project.name}</span>. Cette
                  action est irréversible et supprimera toutes les données
                  associées au projet.
                </p>
              </div>
            </div>

            <div className="border rounded-md p-4 mb-6 bg-neutral-50">
              <h3 className="font-medium mb-2">Cette action va supprimer :</h3>
              <ul className="space-y-1 text-sm text-neutral-700">
                <li>• Toutes les informations du projet</li>
                <li>• Les plannings associés</li>
                <li>• Les actions et tâches associées</li>
                <li>• Les documents associés</li>
                <li>• Les données budgétaires</li>
              </ul>
            </div>

            <div className="flex justify-end gap-3">
              <Button
                variant="outline"
                onClick={() => router.push(`/projects/${id}`)}
                disabled={deleting}
              >
                Annuler
              </Button>
              <Button
                variant="destructive"
                onClick={handleConfirmDelete}
                disabled={deleting}
                className="flex items-center gap-1"
              >
                {deleting ? "Suppression..." : "Confirmer la suppression"}
              </Button>
            </div>
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
