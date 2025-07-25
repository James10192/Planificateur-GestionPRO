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
import { Badge } from "@/components/ui/Badge";
import { formatDate } from "@/lib/utils";
import {
  PlusIcon,
  ArrowDownTrayIcon,
  DocumentIcon,
} from "@heroicons/react/24/outline";
import type { Project, Document } from "@/types/project";

interface ProjectDocumentsProps {
  project: Project;
  isLoading?: boolean;
}

export default function ProjectDocuments({
  project,
  isLoading = false,
}: ProjectDocumentsProps) {
  const [searchTerm, setSearchTerm] = useState("");

  // Mock data - would come from API
  const documents: Document[] = [
    {
      id: 1,
      projectId: project.id,
      title: "Cahier des charges",
      version: "1.2",
      statusId: 2,
      path: "/files/cahier-des-charges-v1.2.pdf",
      uploadedById: 1,
      uploadDate: "2023-02-15",
      status: { id: 2, name: "Validé" },
      uploadedBy: {
        id: 1,
        firstName: "Jean",
        lastName: "Dupont",
        email: "jean.dupont@example.com",
        directionId: 1,
      },
    },
    {
      id: 2,
      projectId: project.id,
      title: "Spécifications techniques",
      version: "2.0",
      statusId: 2,
      path: "/files/specs-tech-v2.0.docx",
      uploadedById: 2,
      uploadDate: "2023-03-10",
      status: { id: 2, name: "Validé" },
      uploadedBy: {
        id: 2,
        firstName: "Marie",
        lastName: "Lambert",
        email: "marie.lambert@example.com",
        directionId: 2,
      },
    },
    {
      id: 3,
      projectId: project.id,
      title: "Planning de développement",
      version: "1.0",
      statusId: 1,
      path: "/files/planning-dev-v1.0.xlsx",
      uploadedById: 3,
      uploadDate: "2023-03-20",
      status: { id: 1, name: "En revue" },
      uploadedBy: {
        id: 3,
        firstName: "Paul",
        lastName: "Martin",
        email: "paul.martin@example.com",
        directionId: 1,
      },
    },
  ];

  // Filter documents based on search term
  const filteredDocuments = documents.filter(
    (doc) =>
      doc.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
      doc.version.toLowerCase().includes(searchTerm.toLowerCase()) ||
      doc.status?.name.toLowerCase().includes(searchTerm.toLowerCase())
  );

  // Function to determine badge style based on document status
  const getStatusBadgeVariant = (statusName?: string) => {
    if (!statusName) return "gray";

    switch (statusName.toLowerCase()) {
      case "validé":
        return "green";
      case "en revue":
        return "blue";
      case "brouillon":
        return "gray";
      case "rejeté":
        return "red";
      case "obsolète":
        return "yellow";
      default:
        return "gray";
    }
  };

  // Function to determine document type icon based on file extension
  const getDocumentTypeIcon = (path?: string) => {
    if (!path) return <DocumentIcon className="h-6 w-6 text-neutral-400" />;

    const extension = path.split(".").pop()?.toLowerCase();

    switch (extension) {
      case "pdf":
        return <DocumentIcon className="h-6 w-6 text-red-500" />;
      case "doc":
      case "docx":
        return <DocumentIcon className="h-6 w-6 text-blue-500" />;
      case "xls":
      case "xlsx":
        return <DocumentIcon className="h-6 w-6 text-green-500" />;
      case "ppt":
      case "pptx":
        return <DocumentIcon className="h-6 w-6 text-orange-500" />;
      default:
        return <DocumentIcon className="h-6 w-6 text-neutral-400" />;
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
          <div className="h-10 bg-neutral-200 rounded w-full mb-4"></div>
          <div className="space-y-2">
            {[...Array(3)].map((_, i) => (
              <div key={i} className="h-14 bg-neutral-200 rounded w-full"></div>
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
          <CardTitle>Documents</CardTitle>
          <Button
            size="sm"
            variant="outline"
            className="flex items-center gap-1"
          >
            <PlusIcon className="h-4 w-4" />
            Téléverser un document
          </Button>
        </div>
      </CardHeader>
      <CardContent>
        {/* Search input */}
        <div className="relative mb-4">
          <input
            type="text"
            placeholder="Rechercher un document..."
            className="w-full rounded-md border border-neutral-300 px-4 py-2 text-sm"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </div>

        {/* Document list */}
        {filteredDocuments.length > 0 ? (
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead className="w-12"></TableHead>
                <TableHead>Titre</TableHead>
                <TableHead>Version</TableHead>
                <TableHead>Statut</TableHead>
                <TableHead>Téléversé par</TableHead>
                <TableHead>Date</TableHead>
                <TableHead className="w-12"></TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {filteredDocuments.map((doc) => (
                <TableRow key={doc.id}>
                  <TableCell className="py-2">
                    {getDocumentTypeIcon(doc.path)}
                  </TableCell>
                  <TableCell className="font-medium py-2">
                    {doc.title}
                  </TableCell>
                  <TableCell className="py-2">{doc.version}</TableCell>
                  <TableCell className="py-2">
                    <Badge
                      variant={
                        getStatusBadgeVariant(doc.status?.name) as
                          | "default"
                          | "destructive"
                          | "outline"
                          | "secondary"
                          | "success"
                          | "warning"
                          | "info"
                          | null
                          | undefined
                      }
                    >
                      {doc.status?.name || "-"}
                    </Badge>
                  </TableCell>
                  <TableCell className="py-2">
                    {doc.uploadedBy?.firstName} {doc.uploadedBy?.lastName}
                  </TableCell>
                  <TableCell className="py-2 text-neutral-500">
                    {formatDate(doc.uploadDate, "dd MMM yyyy")}
                  </TableCell>
                  <TableCell className="py-2">
                    <Button size="icon" variant="ghost" className="h-8 w-8">
                      <ArrowDownTrayIcon className="h-4 w-4" />
                    </Button>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        ) : (
          <div className="text-center py-12">
            {searchTerm ? (
              <p className="text-neutral-500">
                Aucun document ne correspond à votre recherche "{searchTerm}"
              </p>
            ) : (
              <>
                <p className="text-neutral-500 mb-3">
                  Aucun document associé au projet
                </p>
                <Button
                  variant="outline"
                  size="sm"
                  className="flex items-center gap-1 mx-auto"
                >
                  <PlusIcon className="h-4 w-4" />
                  Téléverser un document
                </Button>
              </>
            )}
          </div>
        )}
      </CardContent>
    </Card>
  );
}
