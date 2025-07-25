"use client";

import React from "react";
import StatCards from "@/components/dashboard/StatCards";
import ProjectSummary from "@/components/dashboard/ProjectSummary";
import RecentActivities from "@/components/dashboard/RecentActivities";
import BudgetChart from "@/components/dashboard/BudgetChart";
import MyTasks from "@/components/dashboard/MyTasks";

export default function DashboardPage() {
  return (
    <div className="space-y-8">
      {/* Page title */}
      <div>
        <h1 className="text-2xl font-bold">Tableau de bord</h1>
        <p className="text-neutral-medium">
          Bienvenue dans votre espace de gestion de projets
        </p>
      </div>

      {/* Stats cards */}
      <StatCards />

      {/* Main content grid */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        {/* Left column */}
        <div className="lg:col-span-2 space-y-8">
          {/* Tasks */}
          <MyTasks />

          {/* Budget chart */}
          <BudgetChart />
        </div>

        {/* Right column */}
        <div className="space-y-8">
          {/* Project summary */}
          <ProjectSummary />

          {/* Recent activities */}
          <RecentActivities />
        </div>
      </div>
    </div>
  );
}
