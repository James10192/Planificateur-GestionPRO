"use client";

import React, { useState } from "react";
import { Button } from "@/components/ui/Button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/Card";
import { Badge } from "@/components/ui/Badge";
import { formatDate, formatPercent, getInitials } from "@/lib/utils";
import { PlusIcon, FunnelIcon } from "@heroicons/react/24/outline";
import type { Project, Action } from "@/types/project";

interface ProjectActionsProps {
  project: Project;
  isLoading?: boolean;
}

export default function ProjectActions({
  project,
  isLoading = false,
}: ProjectActionsProps) {
  const [filterStatus, setFilterStatus] = useState<string | null>(null);

  // Mock data - would come from API
  const actions: Action[] = [
    {
      id: 1,
      planningId: 1,
      title: "Définir les exigences fonctionnelles",
      statusId: 2,
      userId: 1,
      startDate: "2023-03-01",
      endDate: "2023-03-15",
      progress: 100,
      status: { id: 2, name: "Terminé" },
      user: {
        id: 1,
        firstName: "Jean",
        lastName: "Dupont",
        email: "jean.dupont@example.com",
        directionId: 1,
      },
    },
    {
      id: 2,
      planningId: 1,
      title: "Concevoir l'architecture technique",
      statusId: 2,
      userId: 2,
      startDate: "2023-03-10",
      endDate: "2023-03-25",
      progress: 100,
      status: { id: 2, name: "Terminé" },
      user: {
        id: 2,
        firstName: "Marie",
        lastName: "Lambert",
        email: "marie.lambert@example.com",
        directionId: 2,
      },
    },
    {
      id: 3,
      planningId: 2,
      title: "Développer le module d'authentification",
      statusId: 1,
      userId: 3,
      startDate: "2023-03-20",
      endDate: "2023-04-10",
      progress: 75,
      status: { id: 1, name: "En cours" },
      user: {
        id: 3,
        firstName: "Paul",
        lastName: "Martin",
        email: "paul.martin@example.com",
        directionId: 1,
      },
    },
    {
      id: 4,
      planningId: 2,
      title: "Tester les fonctionnalités de base",
      statusId: 3,
      userId: 4,
      startDate: "2023-04-01",
      endDate: "2023-04-15",
      progress: 0,
      status: { id: 3, name: "A faire" },
      user: {
        id: 4,
        firstName: "Sophie",
        lastName: "Durand",
        email: "sophie.durand@example.com",
        directionId: 3,
      },
    },
  ];

  // Filter actions based on status
  const filteredActions = filterStatus
    ? actions.filter((action) => action.status?.name === filterStatus)
    : actions;

  // Get unique statuses for filter
  const statuses = [
    ...Array.from(new Set(actions.map((action) => action.status?.name))),
  ].filter(Boolean) as string[];

  // Function to determine badge style based on status
  const getStatusBadgeVariant = (statusName?: string) => {
    if (!statusName) return "gray";

    switch (statusName.toLowerCase()) {
      case "en cours":
        return "blue";
      case "terminé":
        return "green";
      case "a faire":
        return "gray";
      case "en retard":
        return "red";
      case "suspendu":
        return "yellow";
      default:
        return "gray";
    }
  };

  if (isLoading) {
    return (
      <Card className="animate-pulse">
        <CardHeader className="pb-2">
          <div className="flex items-center justify-between">
            <div className="h-6 bg-neutral-200 rounded w-1/3"></div>
            <div className="h-8 bg-neutral-200 rounded w-24"></div>
          </div>
        </CardHeader>
        <CardContent>
          <div className="h-8 bg-neutral-200 rounded w-full mb-4"></div>
          <div className="space-y-4">
            {[...Array(4)].map((_, i) => (
              <div key={i} className="h-24 bg-neutral-200 rounded w-full"></div>
            ))}
          </div>
        </CardContent>
      </Card>
    );
  }

  return (
    <Card>
      <CardHeader className="pb-2">
        <div className="flex items-center justify-between">
          <CardTitle>Actions</CardTitle>
          <Button
            size="sm"
            variant="outline"
            className="flex items-center gap-1"
          >
            <PlusIcon className="h-4 w-4" />
            Nouvelle action
          </Button>
        </div>
      </CardHeader>
      <CardContent>
        {/* Filters */}
        <div className="flex flex-wrap gap-2 mb-4">
          <Button
            size="sm"
            variant={filterStatus === null ? "default" : "ghost"}
            onClick={() => setFilterStatus(null)}
          >
            Toutes
          </Button>
          {statuses.map((status) => (
            <Button
              key={status}
              size="sm"
              variant={filterStatus === status ? "default" : "ghost"}
              onClick={() => setFilterStatus(status)}
            >
              {status}
            </Button>
          ))}
          <Button
            size="sm"
            variant="ghost"
            className="ml-auto flex items-center gap-1"
          >
            <FunnelIcon className="h-3.5 w-3.5" />
            Plus de filtres
          </Button>
        </div>

        {/* Actions list */}
        <div className="space-y-4">
          {filteredActions.length > 0 ? (
            filteredActions.map((action) => (
              <div key={action.id} className="border rounded-lg p-4">
                <div className="flex items-start justify-between mb-3">
                  <div>
                    <h4 className="font-medium">{action.title}</h4>
                    <div className="flex items-center gap-3 text-sm text-neutral-500 mt-1">
                      <span>
                        {formatDate(action.startDate, "dd/MM/yy")} -{" "}
                        {formatDate(action.endDate, "dd/MM/yy")}
                      </span>
                      <Badge
                        variant={
                          getStatusBadgeVariant(action.status?.name) as
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
                        {action.status?.name || "-"}
                      </Badge>
                    </div>
                  </div>
                  <div className="flex items-center gap-1 text-sm">
                    <div className="w-6 h-6 rounded-full bg-primary/10 text-primary flex items-center justify-center font-medium text-xs">
                      {getInitials(
                        action.user?.firstName,
                        action.user?.lastName
                      )}
                    </div>
                    <span className="text-neutral-500">
                      {action.user?.firstName} {action.user?.lastName}
                    </span>
                  </div>
                </div>

                <div className="flex items-center gap-2">
                  <div className="w-full h-1.5 bg-neutral-200 rounded-full overflow-hidden">
                    <div
                      className="h-full bg-primary rounded-full"
                      style={{ width: `${action.progress}%` }}
                    />
                  </div>
                  <span className="text-xs font-medium min-w-[40px] text-right">
                    {formatPercent(action.progress)}
                  </span>
                </div>
              </div>
            ))
          ) : (
            <div className="text-center py-8">
              {filterStatus ? (
                <p className="text-neutral-500">
                  Aucune action avec le statut "{filterStatus}"
                </p>
              ) : (
                <>
                  <p className="text-neutral-500 mb-2">Aucune action définie</p>
                  <Button
                    variant="outline"
                    size="sm"
                    className="flex items-center gap-1 mx-auto"
                  >
                    <PlusIcon className="h-4 w-4" />
                    Ajouter une action
                  </Button>
                </>
              )}
            </div>
          )}
        </div>
      </CardContent>
    </Card>
  );
}
