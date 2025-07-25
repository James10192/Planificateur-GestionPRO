"use client";

import { useState } from "react";
import Link from "next/link";
import { useRouter } from "next/navigation";
import {
  MagnifyingGlassIcon,
  FunnelIcon,
  PlusIcon,
} from "@heroicons/react/24/outline";
import { Button } from "@/components/ui/Button";
import { Input } from "@/components/ui/Input";
import ProjectList from "@/components/projects/ProjectList";

export default function ProjectsPage() {
  const [searchTerm, setSearchTerm] = useState("");
  const router = useRouter();

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-bold">Projets</h1>
        <Button
          onClick={() => router.push("/projects/create")}
          className="flex items-center gap-1"
        >
          <PlusIcon className="h-4 w-4" />
          Nouveau Projet
        </Button>
      </div>

      <div className="flex flex-col md:flex-row gap-4 items-center justify-between">
        <div className="w-full md:w-1/2">
          <Input
            type="text"
            placeholder="Rechercher un projet..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            icon={<MagnifyingGlassIcon className="h-5 w-5" />}
          />
        </div>
        <div className="flex gap-2 w-full md:w-auto">
          <Button variant="outline" className="flex items-center gap-1">
            <FunnelIcon className="h-4 w-4" />
            Filtres
          </Button>
        </div>
      </div>

      <ProjectList searchTerm={searchTerm} />
    </div>
  );
}
