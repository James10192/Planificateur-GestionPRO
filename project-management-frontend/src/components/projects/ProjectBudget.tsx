"use client";

import React from "react";
import { Button } from "@/components/ui/Button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/Card";
import { formatCurrency, formatPercent } from "@/lib/utils";
import { PencilIcon } from "@heroicons/react/24/outline";
import type { Project, ProjectBudget as BudgetType } from "@/types/project";

interface ProjectBudgetProps {
  project: Project;
  isLoading?: boolean;
}

export default function ProjectBudget({
  project,
  isLoading = false,
}: ProjectBudgetProps) {
  // Mock data - would come from API
  const budget: BudgetType = {
    id: 1,
    projectId: project.id,
    initialBudget: 150000,
    consumedBudget: 87500,
  };

  // Calculate percentage used
  const percentUsed = budget
    ? (budget.consumedBudget / budget.initialBudget) * 100
    : 0;
  const remaining = budget ? budget.initialBudget - budget.consumedBudget : 0;

  // Function to determine budget status and color
  const getBudgetStatus = () => {
    if (percentUsed > 90) return { status: "Critique", color: "text-red-500" };
    if (percentUsed > 75)
      return { status: "Attention", color: "text-yellow-500" };
    return { status: "Bon", color: "text-green-500" };
  };

  const { status, color } = getBudgetStatus();

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
          <div className="space-y-6">
            <div className="h-20 bg-neutral-200 rounded w-full"></div>
            <div className="grid grid-cols-3 gap-4">
              {[...Array(3)].map((_, i) => (
                <div key={i} className="h-16 bg-neutral-200 rounded"></div>
              ))}
            </div>
            <div className="h-32 bg-neutral-200 rounded w-full"></div>
          </div>
        </CardContent>
      </Card>
    );
  }

  return (
    <Card>
      <CardHeader className="pb-2">
        <div className="flex items-center justify-between">
          <CardTitle>Budget</CardTitle>
          <Button
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
        {budget ? (
          <>
            {/* Progress bar */}
            <div>
              <div className="flex items-center justify-between mb-2">
                <p className="text-sm font-medium">Utilisation du budget</p>
                <p className={`text-sm font-medium ${color}`}>
                  {formatPercent(percentUsed)}
                </p>
              </div>
              <div className="w-full h-2 bg-neutral-200 rounded-full overflow-hidden">
                <div
                  className={`h-full rounded-full ${
                    percentUsed > 90
                      ? "bg-red-500"
                      : percentUsed > 75
                      ? "bg-yellow-500"
                      : "bg-green-500"
                  }`}
                  style={{ width: `${percentUsed}%` }}
                />
              </div>
              <p className={`text-xs mt-1 ${color}`}>Statut: {status}</p>
            </div>

            {/* Budget stats */}
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
              <div className="p-4 bg-primary/5 rounded-lg">
                <p className="text-sm text-neutral-500 mb-1">Budget initial</p>
                <p className="text-lg font-semibold">
                  {formatCurrency(budget.initialBudget)}
                </p>
              </div>
              <div className="p-4 bg-primary/5 rounded-lg">
                <p className="text-sm text-neutral-500 mb-1">Consommé</p>
                <p className="text-lg font-semibold">
                  {formatCurrency(budget.consumedBudget)}
                </p>
              </div>
              <div className="p-4 bg-primary/5 rounded-lg">
                <p className="text-sm text-neutral-500 mb-1">Restant</p>
                <p className="text-lg font-semibold">
                  {formatCurrency(remaining)}
                </p>
              </div>
            </div>

            {/* Placeholder for a budget chart */}
            <div className="aspect-[2/1] bg-neutral-50 rounded-lg border flex items-center justify-center">
              <p className="text-neutral-400">
                Graphique d'évolution du budget
              </p>
            </div>
          </>
        ) : (
          <div className="text-center py-8">
            <p className="text-neutral-500 mb-3">
              Aucun budget défini pour ce projet
            </p>
            <Button
              variant="outline"
              size="sm"
              className="flex items-center gap-1 mx-auto"
            >
              <PencilIcon className="h-4 w-4" />
              Définir un budget
            </Button>
          </div>
        )}
      </CardContent>
    </Card>
  );
}
