"use client";

import React from "react";
import Link from "next/link";
import { usePathname } from "next/navigation";
import {
  HomeIcon,
  FolderIcon,
  ClipboardDocumentListIcon,
  CalendarIcon,
  Cog6ToothIcon,
  ChartBarIcon,
} from "@heroicons/react/24/outline";

const Sidebar = () => {
  const pathname = usePathname();

  const menuItems = [
    {
      name: "Dashboard",
      href: "/dashboard",
      icon: HomeIcon,
    },
    {
      name: "Projets",
      href: "/projects",
      icon: FolderIcon,
    },
    {
      name: "Actions",
      href: "/actions",
      icon: ClipboardDocumentListIcon,
    },
    {
      name: "Plannings",
      href: "/plannings",
      icon: CalendarIcon,
    },
    {
      name: "Paramètres",
      href: "/parameters",
      icon: Cog6ToothIcon,
    },
    {
      name: "Rapports",
      href: "/reports",
      icon: ChartBarIcon,
    },
  ];

  return (
    <aside className="bg-primary w-64 min-h-screen text-white flex flex-col">
      {/* Logo */}
      <div className="p-6 border-b border-primary-light">
        <h1 className="text-xl font-bold">Gestion Projets</h1>
      </div>

      {/* Navigation */}
      <nav className="flex-1 pt-6">
        <ul className="space-y-1">
          {menuItems.map((item) => {
            const isActive =
              pathname === item.href || pathname?.startsWith(`${item.href}/`);
            return (
              <li key={item.name}>
                <Link href={item.href}>
                  <div
                    className={`flex items-center px-6 py-3 text-sm ${
                      isActive
                        ? "bg-primary-light border-l-4 border-accent"
                        : "hover:bg-primary-light"
                    }`}
                  >
                    <item.icon className="h-5 w-5 mr-3" />
                    {item.name}
                  </div>
                </Link>
              </li>
            );
          })}
        </ul>
      </nav>

      {/* Version info */}
      <div className="p-4 text-xs text-primary-lighter border-t border-primary-light mt-auto">
        <p>Version 1.0.0</p>
      </div>
    </aside>
  );
};

export default Sidebar;
