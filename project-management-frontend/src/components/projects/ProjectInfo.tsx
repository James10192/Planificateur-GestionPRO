"use client";

import React from "react";
import { Button } from "@/components/ui/Button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/Card";
import { Badge } from "@/components/ui/Badge";
import { formatDate, formatPercent } from "@/lib/utils";
import { PencilIcon } from "@heroicons/react/24/outline";
import type { Project } from "@/types/project";
import { useRouter } from "next/navigation";

interface ProjectInfoProps {
  project: Project;
  isLoading?: boolean;
}

export default function ProjectInfo({
  project,
  isLoading = false,
}: ProjectInfoProps) {
  const router = useRouter();

  // Function to determine badge style based on status
  const getStatusBadgeVariant = (statusName?: string) => {
    if (!statusName) return "gray";

    switch (statusName.toLowerCase()) {
      case "en cours":
        return "blue";
      case "terminé":
        return "green";
      case "suspendu":
        return "yellow";
      case "annulé":
        return "red";
      default:
        return "gray";
    }
  };

  // Progress calculation
  const progress = 65; // This would come from project data or be calculated

  if (isLoading) {
    return (
      <Card className="animate-pulse">
        <CardHeader className="pb-2">
          <div className="flex items-center justify-between">
            <div className="h-6 bg-neutral-200 rounded w-1/3"></div>
            <div className="h-8 bg-neutral-200 rounded w-24"></div>
          </div>
        </CardHeader>
        <CardContent className="space-y-4">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            {[...Array(6)].map((_, i) => (
              <div key={i} className="space-y-1">
                <div className="h-4 bg-neutral-200 rounded w-1/3"></div>
                <div className="h-5 bg-neutral-200 rounded w-3/4"></div>
              </div>
            ))}
          </div>
          <div className="space-y-1">
            <div className="h-4 bg-neutral-200 rounded w-1/4"></div>
            <div className="h-20 bg-neutral-200 rounded w-full"></div>
          </div>
        </CardContent>
      </Card>
    );
  }

  return (
    <Card>
      <CardHeader className="pb-2">
        <div className="flex items-center justify-between">
          <CardTitle>Informations projet</CardTitle>
          <Button
            onClick={() => router.push(`/projects/${project.id}/edit`)}
            size="sm"
            variant="outline"
            className="flex items-center gap-1"
          >
            <PencilIcon className="h-4 w-4" />
            Modifier
          </Button>
        </div>
      </CardHeader>
      <CardContent className="space-y-6">
        {/* Project status and progress */}
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
          <div className="space-y-1">
            <p className="text-sm text-neutral-500">État</p>
            <Badge
              variant={
                getStatusBadgeVariant(project.status?.name) as
                  | "default"
                  | "destructive"
                  | "outline"
                  | "secondary"
                  | "success"
                  | "warning"
                  | "info"
                  | "pending"
                  | "inProgress"
                  | "completed"
                  | "cancelled"
                  | "highPriority"
                  | "mediumPriority"
                  | "lowPriority"
                  | undefined
              }
            >
              {project.status?.name || "-"}
            </Badge>
          </div>
          <div className="space-y-1">
            <p className="text-sm text-neutral-500">Progression</p>
            <div className="flex items-center gap-2">
              <div className="w-32 h-2 bg-neutral-200 rounded-full overflow-hidden">
                <div
                  className="h-full bg-primary rounded-full"
                  style={{ width: `${progress}%` }}
                />
              </div>
              <span className="text-sm font-medium">
                {formatPercent(progress)}
              </span>
            </div>
          </div>
        </div>

        {/* Project details grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 gap-x-6 gap-y-4">
          <div className="space-y-1">
            <p className="text-sm text-neutral-500">Type</p>
            <p className="font-medium">{project.type?.name || "-"}</p>
          </div>
          <div className="space-y-1">
            <p className="text-sm text-neutral-500">Priorité</p>
            <p className="font-medium">{project.priority?.name || "-"}</p>
          </div>
          <div className="space-y-1">
            <p className="text-sm text-neutral-500">Direction</p>
            <p className="font-medium">{project.direction?.name || "-"}</p>
          </div>
          <div className="space-y-1">
            <p className="text-sm text-neutral-500">Équipe</p>
            <p className="font-medium">{project.team?.name || "-"}</p>
          </div>
          <div className="space-y-1">
            <p className="text-sm text-neutral-500">Date de début</p>
            <p className="font-medium">
              {formatDate(project.startDate) || "-"}
            </p>
          </div>
          <div className="space-y-1">
            <p className="text-sm text-neutral-500">Date de fin prévue</p>
            <p className="font-medium">{formatDate(project.endDate) || "-"}</p>
          </div>
        </div>

        {/* Project description */}
        <div className="space-y-1">
          <p className="text-sm text-neutral-500">Description</p>
          <p className="text-sm">
            {project.description || "Aucune description disponible"}
          </p>
        </div>
      </CardContent>
    </Card>
  );
}
