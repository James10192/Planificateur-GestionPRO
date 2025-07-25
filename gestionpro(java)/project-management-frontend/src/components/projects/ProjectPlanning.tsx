"use client";

import React from "react";
import { Button } from "@/components/ui/Button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/Card";
import { formatDate, formatPercent } from "@/lib/utils";
import { PlusIcon } from "@heroicons/react/24/outline";
import type { Project, Planning, PortfolioPhase } from "@/types/project";

interface ProjectPlanningProps {
  project: Project;
  isLoading?: boolean;
}

export default function ProjectPlanning({
  project,
  isLoading = false,
}: ProjectPlanningProps) {
  // Mock data - would come from API
  const plannings: Planning[] = [
    {
      id: 1,
      projectId: project.id,
      phaseId: 1,
      phase: { id: 1, name: "Initiation", percentage: 10 },
    },
    {
      id: 2,
      projectId: project.id,
      phaseId: 2,
      phase: { id: 2, name: "Planification", percentage: 20 },
    },
    {
      id: 3,
      projectId: project.id,
      phaseId: 3,
      phase: { id: 3, name: "Exécution", percentage: 50 },
    },
    {
      id: 4,
      projectId: project.id,
      phaseId: 4,
      phase: { id: 4, name: "Clôture", percentage: 20 },
    },
  ];

  // Mock phase progress data - would come from API
  const phaseProgress = [
    { phaseId: 1, progress: 100 },
    { phaseId: 2, progress: 80 },
    { phaseId: 3, progress: 30 },
    { phaseId: 4, progress: 0 },
  ];

  // Calculate total project progress based on phase weights
  const calculateTotalProgress = () => {
    let totalProgress = 0;
    let totalWeight = 0;

    for (const planning of plannings) {
      if (planning.phase) {
        const weight = planning.phase.percentage;
        const phase = phaseProgress.find((p) => p.phaseId === planning.phaseId);
        if (phase) {
          totalProgress += (phase.progress * weight) / 100;
          totalWeight += weight;
        }
      }
    }

    return totalWeight > 0 ? (totalProgress / totalWeight) * 100 : 0;
  };

  const totalProgress = calculateTotalProgress();

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
          <div className="space-y-4">
            <div className="h-8 bg-neutral-200 rounded w-3/4"></div>
            {[...Array(4)].map((_, i) => (
              <div key={i} className="h-16 bg-neutral-200 rounded w-full"></div>
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
          <CardTitle>Planning</CardTitle>
          <Button
            size="sm"
            variant="outline"
            className="flex items-center gap-1"
          >
            <PlusIcon className="h-4 w-4" />
            Ajouter une phase
          </Button>
        </div>
      </CardHeader>
      <CardContent>
        <div className="mb-6">
          <div className="flex items-center justify-between mb-1">
            <p className="text-sm font-medium">Progression globale</p>
            <p className="text-sm font-medium">
              {formatPercent(totalProgress)}
            </p>
          </div>
          <div className="w-full h-2 bg-neutral-200 rounded-full overflow-hidden">
            <div
              className="h-full bg-primary rounded-full"
              style={{ width: `${totalProgress}%` }}
            />
          </div>
        </div>

        <div className="space-y-4">
          {plannings.length > 0 ? (
            plannings.map((planning) => {
              const phase = phaseProgress.find(
                (p) => p.phaseId === planning.phaseId
              );
              const progress = phase ? phase.progress : 0;

              return (
                <div key={planning.id} className="border rounded-lg p-4">
                  <div className="flex items-center justify-between mb-3">
                    <div>
                      <h4 className="font-medium">{planning.phase?.name}</h4>
                      <p className="text-xs text-neutral-500">
                        Poids: {planning.phase?.percentage}%
                      </p>
                    </div>
                    <div className="text-right">
                      <p className="font-medium">{formatPercent(progress)}</p>
                    </div>
                  </div>
                  <div className="w-full h-1.5 bg-neutral-200 rounded-full overflow-hidden">
                    <div
                      className="h-full bg-primary rounded-full"
                      style={{ width: `${progress}%` }}
                    />
                  </div>
                </div>
              );
            })
          ) : (
            <div className="text-center py-8">
              <p className="text-neutral-500 mb-2">
                Aucune phase de planning définie
              </p>
              <Button
                variant="outline"
                size="sm"
                className="flex items-center gap-1 mx-auto"
              >
                <PlusIcon className="h-4 w-4" />
                Ajouter une phase
              </Button>
            </div>
          )}
        </div>
      </CardContent>
    </Card>
  );
}
