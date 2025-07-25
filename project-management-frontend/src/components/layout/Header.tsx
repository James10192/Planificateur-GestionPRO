import React from "react";
import {
  MagnifyingGlassIcon,
  BellIcon,
  UserCircleIcon,
} from "@heroicons/react/24/outline";

const Header = () => {
  // This would typically come from an auth context or API
  const user = {
    name: "John Doe",
    role: "Project Manager",
    avatar: null,
  };

  // Date filter options
  const dateFilters = [
    { id: "today", label: "Today" },
    { id: "week", label: "This Week" },
    { id: "month", label: "This Month" },
    { id: "reports", label: "Reports" },
  ];

  return (
    <header className="bg-white border-b border-neutral-light px-6 py-4">
      <div className="flex items-center justify-between">
        {/* Search bar */}
        <div className="relative w-96">
          <input
            type="text"
            placeholder="Rechercher..."
            className="form-input pl-10"
          />
          <MagnifyingGlassIcon className="h-5 w-5 absolute left-3 top-1/2 transform -translate-y-1/2 text-neutral-medium" />
        </div>

        {/* Date filters */}
        <div className="flex space-x-3">
          {dateFilters.map((filter) => (
            <button
              key={filter.id}
              className={`px-4 py-2 text-sm rounded-lg ${
                filter.id === "today"
                  ? "bg-primary text-white"
                  : "bg-white text-neutral-dark border border-neutral-light hover:bg-neutral-lightest"
              }`}
            >
              {filter.label}
            </button>
          ))}
        </div>

        {/* User profile and notifications */}
        <div className="flex items-center space-x-4">
          <button className="relative">
            <BellIcon className="h-6 w-6 text-neutral-medium hover:text-primary" />
            <span className="absolute -top-1 -right-1 h-4 w-4 rounded-full bg-error text-white text-xs flex items-center justify-center">
              2
            </span>
          </button>
          <div className="flex items-center space-x-3">
            {user.avatar ? (
              <img
                src={user.avatar}
                alt={user.name}
                className="h-8 w-8 rounded-full"
              />
            ) : (
              <UserCircleIcon className="h-8 w-8 text-neutral-medium" />
            )}
            <div className="hidden sm:block">
              <p className="text-sm font-medium">{user.name}</p>
              <p className="text-xs text-neutral-medium">{user.role}</p>
            </div>
          </div>
        </div>
      </div>
    </header>
  );
};

export default Header;
