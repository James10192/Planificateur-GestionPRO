"use client";

import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import { Input } from "@/components/ui/Input";
import { Button } from "@/components/ui/Button";
import { Select } from "@/components/ui/Select";
import { endpoints } from "@/lib/api";
import { getErrorMessage } from "@/lib/utils";
import type {
  Project,
  ProjectType,
  Status,
  Priority,
  Direction,
  Team,
} from "@/types/project";

interface ProjectFormProps {
  project?: Partial<Project>;
  onSubmit: (project: Partial<Project>) => void;
  onError: (error: string) => void;
}

export default function ProjectForm({
  project,
  onSubmit,
  onError,
}: ProjectFormProps) {
  const router = useRouter();
  const [loading, setLoading] = useState(false);
  const [formData, setFormData] = useState<Partial<Project>>(
    project || {
      name: "",
      description: "",
      typeId: 0,
      statusId: 0,
      priorityId: 0,
      directionId: 0,
      teamId: 0,
      startDate: new Date().toISOString().split("T")[0],
      endDate: "",
    }
  );

  // Form options
  const [projectTypes, setProjectTypes] = useState<ProjectType[]>([]);
  const [statuses, setStatuses] = useState<Status[]>([]);
  const [priorities, setPriorities] = useState<Priority[]>([]);
  const [directions, setDirections] = useState<Direction[]>([]);
  const [teams, setTeams] = useState<Team[]>([]);

  // Form errors
  const [errors, setErrors] = useState<Record<string, string>>({});

  // Load reference data
  useEffect(() => {
    async function fetchReferenceData() {
      try {
        const [
          typesResponse,
          statusesResponse,
          prioritiesResponse,
          directionsResponse,
          teamsResponse,
        ] = await Promise.all([
          endpoints.parameters.getProjectTypes(),
          endpoints.parameters.getStatuses(),
          endpoints.parameters.getPriorities(),
          endpoints.parameters.getDirections(),
          endpoints.teams.getAll(),
        ]);

        setProjectTypes(typesResponse.data);
        setStatuses(statusesResponse.data);
        setPriorities(prioritiesResponse.data);
        setDirections(directionsResponse.data);
        setTeams(teamsResponse.data);
      } catch (error) {
        console.error("Error loading reference data:", error);
        onError(
          "Erreur lors du chargement des données de référence. Veuillez rafraîchir la page."
        );
      }
    }

    fetchReferenceData();
  }, [onError]);

  const handleInputChange = (
    e: React.ChangeEvent<
      HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement
    >
  ) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));

    // Clear the error for this field if it exists
    if (errors[name]) {
      setErrors((prev) => {
        const newErrors = { ...prev };
        delete newErrors[name];
        return newErrors;
      });
    }
  };

  const validateForm = () => {
    const newErrors: Record<string, string> = {};

    if (!formData.name?.trim()) {
      newErrors.name = "Le nom du projet est requis";
    }

    if (!formData.typeId) {
      newErrors.typeId = "Le type de projet est requis";
    }

    if (!formData.statusId) {
      newErrors.statusId = "Le statut est requis";
    }

    if (!formData.priorityId) {
      newErrors.priorityId = "La priorité est requise";
    }

    if (!formData.directionId) {
      newErrors.directionId = "La direction est requise";
    }

    if (!formData.startDate) {
      newErrors.startDate = "La date de début est requise";
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!validateForm()) {
      return;
    }

    setLoading(true);

    try {
      const parsedFormData = {
        ...formData,
        typeId: Number(formData.typeId),
        statusId: Number(formData.statusId),
        priorityId: Number(formData.priorityId),
        directionId: Number(formData.directionId),
        teamId:
          formData.teamId && Number(formData.teamId) > 0
            ? Number(formData.teamId)
            : undefined,
      };

      onSubmit(parsedFormData);
    } catch (error) {
      console.error("Error saving project:", error);
      onError(getErrorMessage(error));
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-6">
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div className="space-y-2">
          <label htmlFor="name" className="text-sm font-medium">
            Nom du projet <span className="text-error">*</span>
          </label>
          <Input
            id="name"
            name="name"
            value={formData.name}
            onChange={handleInputChange}
            error={!!errors.name}
          />
          {errors.name && <p className="text-sm text-error">{errors.name}</p>}
        </div>

        <div className="space-y-2">
          <label htmlFor="typeId" className="text-sm font-medium">
            Type de projet <span className="text-error">*</span>
          </label>
          <Select
            id="typeId"
            name="typeId"
            value={formData.typeId || ""}
            onChange={handleInputChange}
            options={projectTypes.map((type) => ({
              value: type.id,
              label: type.name,
            }))}
            error={!!errors.typeId}
          />
          {errors.typeId && (
            <p className="text-sm text-error">{errors.typeId}</p>
          )}
        </div>

        <div className="space-y-2">
          <label htmlFor="statusId" className="text-sm font-medium">
            Statut <span className="text-error">*</span>
          </label>
          <Select
            id="statusId"
            name="statusId"
            value={formData.statusId || ""}
            onChange={handleInputChange}
            options={statuses.map((status) => ({
              value: status.id,
              label: status.name,
            }))}
            error={!!errors.statusId}
          />
          {errors.statusId && (
            <p className="text-sm text-error">{errors.statusId}</p>
          )}
        </div>

        <div className="space-y-2">
          <label htmlFor="priorityId" className="text-sm font-medium">
            Priorité <span className="text-error">*</span>
          </label>
          <Select
            id="priorityId"
            name="priorityId"
            value={formData.priorityId || ""}
            onChange={handleInputChange}
            options={priorities.map((priority) => ({
              value: priority.id,
              label: priority.name,
            }))}
            error={!!errors.priorityId}
          />
          {errors.priorityId && (
            <p className="text-sm text-error">{errors.priorityId}</p>
          )}
        </div>

        <div className="space-y-2">
          <label htmlFor="directionId" className="text-sm font-medium">
            Direction <span className="text-error">*</span>
          </label>
          <Select
            id="directionId"
            name="directionId"
            value={formData.directionId || ""}
            onChange={handleInputChange}
            options={directions.map((direction) => ({
              value: direction.id,
              label: `${direction.code} - ${direction.name}`,
            }))}
            error={!!errors.directionId}
          />
          {errors.directionId && (
            <p className="text-sm text-error">{errors.directionId}</p>
          )}
        </div>

        <div className="space-y-2">
          <label htmlFor="teamId" className="text-sm font-medium">
            Équipe
          </label>
          <Select
            id="teamId"
            name="teamId"
            value={formData.teamId || ""}
            onChange={handleInputChange}
            options={teams.map((team) => ({
              value: team.id,
              label: team.name,
            }))}
          />
        </div>

        <div className="space-y-2">
          <label htmlFor="startDate" className="text-sm font-medium">
            Date de début <span className="text-error">*</span>
          </label>
          <Input
            id="startDate"
            name="startDate"
            type="date"
            value={formData.startDate ? formData.startDate.split("T")[0] : ""}
            onChange={handleInputChange}
            error={!!errors.startDate}
          />
          {errors.startDate && (
            <p className="text-sm text-error">{errors.startDate}</p>
          )}
        </div>

        <div className="space-y-2">
          <label htmlFor="endDate" className="text-sm font-medium">
            Date de fin prévue
          </label>
          <Input
            id="endDate"
            name="endDate"
            type="date"
            value={formData.endDate ? formData.endDate.split("T")[0] : ""}
            onChange={handleInputChange}
          />
        </div>
      </div>

      <div className="space-y-2 col-span-full">
        <label htmlFor="description" className="text-sm font-medium">
          Description
        </label>
        <textarea
          id="description"
          name="description"
          value={formData.description || ""}
          onChange={handleInputChange}
          rows={4}
          className="w-full rounded-md border border-input bg-background px-3 py-2 text-sm focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring"
        />
      </div>

      <div className="flex justify-end gap-3">
        <Button type="button" variant="outline" onClick={() => router.back()}>
          Annuler
        </Button>
        <Button type="submit" loading={loading}>
          {project?.id ? "Mettre à jour" : "Créer"}
        </Button>
      </div>
    </form>
  );
}
