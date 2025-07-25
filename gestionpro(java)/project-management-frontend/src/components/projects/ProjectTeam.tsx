"use client";

import React, { useState } from "react";
import { Button } from "@/components/ui/Button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/Card";
import {
  Table,
  TableHeader,
  TableRow,
  TableHead,
  TableBody,
  TableCell,
} from "@/components/ui/Table";
import { PlusIcon, UserPlusIcon } from "@heroicons/react/24/outline";
import type { Team, TeamMember, Project } from "@/types/project";
import { getInitials } from "@/lib/utils";

interface ProjectTeamProps {
  project: Project;
  isLoading?: boolean;
}

export default function ProjectTeam({
  project,
  isLoading = false,
}: ProjectTeamProps) {
  const [showAddMember, setShowAddMember] = useState(false);

  // This would come from an API call or props
  const team = project.team;
  const members = team?.members || [];

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
          <div className="h-6 bg-neutral-200 rounded w-full mb-4"></div>
          <div className="space-y-2">
            {[...Array(4)].map((_, i) => (
              <div key={i} className="h-12 bg-neutral-200 rounded w-full"></div>
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
          <CardTitle>Équipe</CardTitle>
          <Button
            onClick={() => setShowAddMember(!showAddMember)}
            size="sm"
            variant="outline"
            className="flex items-center gap-1"
          >
            <UserPlusIcon className="h-4 w-4" />
            Ajouter un membre
          </Button>
        </div>
      </CardHeader>
      <CardContent>
        {team ? (
          <>
            <p className="text-sm mb-4">
              Équipe: <span className="font-medium">{team.name}</span>
            </p>

            {members.length > 0 ? (
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Membre</TableHead>
                    <TableHead>Rôle</TableHead>
                    <TableHead>Direction</TableHead>
                    <TableHead>Contact</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {members.map((member) => (
                    <TableRow key={`${member.teamId}-${member.userId}`}>
                      <TableCell className="py-2">
                        <div className="flex items-center gap-3">
                          <div className="w-8 h-8 rounded-full bg-primary/10 text-primary flex items-center justify-center font-medium text-sm">
                            {getInitials(
                              member.user?.firstName,
                              member.user?.lastName
                            )}
                          </div>
                          <div>
                            <p className="font-medium">
                              {member.user?.firstName} {member.user?.lastName}
                            </p>
                            <p className="text-xs text-neutral-500">
                              {member.user?.function || ""}
                            </p>
                          </div>
                        </div>
                      </TableCell>
                      <TableCell className="py-2">
                        {member.role?.name || "-"}
                      </TableCell>
                      <TableCell className="py-2">
                        {member.user?.direction?.name || "-"}
                      </TableCell>
                      <TableCell className="py-2">
                        <p>{member.user?.email}</p>
                        <p className="text-xs text-neutral-500">
                          {member.user?.phone || "-"}
                        </p>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            ) : (
              <div className="text-center py-8">
                <p className="text-neutral-500 mb-2">
                  Aucun membre dans l'équipe
                </p>
                <Button
                  variant="outline"
                  size="sm"
                  className="flex items-center gap-1 mx-auto"
                  onClick={() => setShowAddMember(true)}
                >
                  <PlusIcon className="h-4 w-4" />
                  Ajouter des membres
                </Button>
              </div>
            )}
          </>
        ) : (
          <div className="text-center py-8">
            <p className="text-neutral-500 mb-2">
              Aucune équipe associée au projet
            </p>
            <Button variant="outline" size="sm" className="mx-auto">
              Créer une équipe
            </Button>
          </div>
        )}

        {/* Add member form would go here when showAddMember is true */}
        {showAddMember && (
          <div className="mt-4 p-4 border rounded-md">
            <h4 className="text-sm font-medium mb-2">
              Ajouter un membre à l'équipe
            </h4>
            {/* Form content would go here */}
            <div className="flex justify-end gap-2 mt-4">
              <Button
                variant="outline"
                size="sm"
                onClick={() => setShowAddMember(false)}
              >
                Annuler
              </Button>
              <Button size="sm">Ajouter</Button>
            </div>
          </div>
        )}
      </CardContent>
    </Card>
  );
}
