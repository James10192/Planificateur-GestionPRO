"use client";

import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import {
  ChevronRightIcon,
  ExclamationCircleIcon,
} from "@heroicons/react/24/outline";
import {
  Table,
  TableHeader,
  TableBody,
  TableRow,
  TableHead,
  TableCell,
} from "@/components/ui/Table";
import { Badge } from "@/components/ui/Badge";
import { Button } from "@/components/ui/Button";
import { Card } from "@/components/ui/Card";
import { formatDate } from "@/lib/utils";
import { endpoints } from "@/lib/api";
import type { Project } from "@/types/project";

interface ProjectListProps {
  searchTerm?: string;
}

export default function ProjectList({ searchTerm = "" }: ProjectListProps) {
  const [projects, setProjects] = useState<Project[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const router = useRouter();

  useEffect(() => {
    async function fetchProjects() {
      try {
        setLoading(true);
        const response = await endpoints.projects.getAll();
        setProjects(response.data);
        setError(null);
      } catch (err) {
        console.error("Failed to fetch projects:", err);
        setError(
          "Impossible de charger les projets. Veuillez réessayer plus tard."
        );
      } finally {
        setLoading(false);
      }
    }

    fetchProjects();
  }, []);

  const filteredProjects = projects.filter((project) =>
    project.name.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const getStatusVariant = (statusName: string) => {
    switch (statusName?.toLowerCase()) {
      case "en cours":
        return "inProgress";
      case "terminé":
        return "completed";
      case "annulé":
        return "cancelled";
      case "non démarré":
        return "pending";
      default:
        return "default";
    }
  };

  const getPriorityVariant = (priorityName: string) => {
    switch (priorityName?.toLowerCase()) {
      case "haute":
        return "highPriority";
      case "moyenne":
        return "mediumPriority";
      case "basse":
        return "lowPriority";
      default:
        return "default";
    }
  };

  if (loading) {
    return (
      <Card className="p-6">
        <div className="flex items-center justify-center py-10">
          <div className="h-10 w-10 animate-spin rounded-full border-4 border-primary border-t-transparent"></div>
        </div>
      </Card>
    );
  }

  if (error) {
    return (
      <Card className="p-6">
        <div className="flex flex-col items-center justify-center py-10 text-center">
          <ExclamationCircleIcon className="h-12 w-12 text-error mb-2" />
          <p className="text-error font-medium mb-4">{error}</p>
          <Button onClick={() => window.location.reload()}>Réessayer</Button>
        </div>
      </Card>
    );
  }

  if (filteredProjects.length === 0) {
    return (
      <Card className="p-6">
        <div className="flex flex-col items-center justify-center py-10 text-center">
          <p className="text-muted-foreground mb-4">
            {searchTerm
              ? "Aucun projet ne correspond à votre recherche."
              : "Aucun projet n'a été trouvé."}
          </p>
          <Button onClick={() => router.push("/projects/create")}>
            Créer un projet
          </Button>
        </div>
      </Card>
    );
  }

  return (
    <Card>
      <Table>
        <TableHeader>
          <TableRow>
            <TableHead>Nom</TableHead>
            <TableHead>Type</TableHead>
            <TableHead>Statut</TableHead>
            <TableHead>Priorité</TableHead>
            <TableHead>Direction</TableHead>
            <TableHead>Date début</TableHead>
            <TableHead>Date fin</TableHead>
            <TableHead className="text-right">Actions</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {filteredProjects.map((project) => (
            <TableRow
              key={project.id}
              className="cursor-pointer"
              onClick={() => router.push(`/projects/${project.id}`)}
            >
              <TableCell className="font-medium">{project.name}</TableCell>
              <TableCell>{project.type?.name || "-"}</TableCell>
              <TableCell>
                {project.status && (
                  <Badge variant={getStatusVariant(project.status.name)}>
                    {project.status.name}
                  </Badge>
                )}
              </TableCell>
              <TableCell>
                {project.priority && (
                  <Badge variant={getPriorityVariant(project.priority.name)}>
                    {project.priority.name}
                  </Badge>
                )}
              </TableCell>
              <TableCell>{project.direction?.name || "-"}</TableCell>
              <TableCell>
                {formatDate(project.startDate, "dd/MM/yyyy")}
              </TableCell>
              <TableCell>{formatDate(project.endDate, "dd/MM/yyyy")}</TableCell>
              <TableCell className="text-right">
                <Button
                  variant="ghost"
                  size="sm"
                  onClick={(e) => {
                    e.stopPropagation();
                    router.push(`/projects/${project.id}`);
                  }}
                >
                  <ChevronRightIcon className="h-4 w-4" />
                </Button>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </Card>
  );
}
