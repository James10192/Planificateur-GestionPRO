"use client";

import React, { useState, useEffect, useRef } from "react";
import { cn } from "@/lib/utils";

interface Tab {
  id: string;
  label: string;
  disabled?: boolean;
}

interface TabsProps {
  tabs: Tab[];
  defaultTab?: string;
  onChange?: (tabId: string) => void;
  className?: string;
  tabListClassName?: string;
  tabClassName?: string;
  activeTabClassName?: string;
  disabledTabClassName?: string;
}

export function Tabs({
  tabs,
  defaultTab,
  onChange,
  className,
  tabListClassName,
  tabClassName,
  activeTabClassName,
  disabledTabClassName,
}: TabsProps) {
  const [activeTab, setActiveTab] = useState<string>(
    defaultTab || tabs[0]?.id || ""
  );
  const tabsRef = useRef<HTMLDivElement>(null);
  const [visibleIndicator, setVisibleIndicator] = useState(false);
  const [indicatorStyle, setIndicatorStyle] = useState({
    left: "0px",
    width: "0px",
  });

  useEffect(() => {
    // Update active tab when defaultTab changes
    if (defaultTab && defaultTab !== activeTab) {
      setActiveTab(defaultTab);
    }
  }, [defaultTab, activeTab]);

  useEffect(() => {
    // Position the active indicator
    const tabElements = tabsRef.current?.querySelectorAll('[role="tab"]');
    const activeElement = tabsRef.current?.querySelector(
      `[data-tab-id="${activeTab}"]`
    );

    if (activeElement instanceof HTMLElement && tabElements?.length) {
      // Calculate position and width
      const { offsetLeft, offsetWidth } = activeElement;
      setIndicatorStyle({
        left: `${offsetLeft}px`,
        width: `${offsetWidth}px`,
      });

      // Make indicator visible after position is set
      setVisibleIndicator(true);
    }
  }, [activeTab, tabs]);

  const handleTabClick = (tabId: string) => {
    if (tabId !== activeTab) {
      setActiveTab(tabId);
      onChange?.(tabId);
    }
  };

  const handleKeyDown = (
    e: React.KeyboardEvent<HTMLDivElement>,
    tabId: string
  ) => {
    if (e.key === "Enter" || e.key === " ") {
      e.preventDefault();
      handleTabClick(tabId);
    }
  };

  return (
    <div className={cn("w-full", className)}>
      <div
        ref={tabsRef}
        className={cn(
          "relative flex border-b border-neutral-200",
          tabListClassName
        )}
        role="tablist"
        aria-orientation="horizontal"
      >
        {tabs.map((tab) => (
          <div
            key={tab.id}
            data-tab-id={tab.id}
            role="tab"
            tabIndex={tab.disabled ? -1 : activeTab === tab.id ? 0 : -1}
            aria-selected={activeTab === tab.id}
            aria-disabled={tab.disabled}
            onClick={() => !tab.disabled && handleTabClick(tab.id)}
            onKeyDown={(e) => !tab.disabled && handleKeyDown(e, tab.id)}
            className={cn(
              "px-4 py-2 font-medium cursor-pointer transition-colors",
              "focus:outline-none focus-visible:ring-2 focus-visible:ring-primary focus-visible:ring-offset-2",
              activeTab === tab.id
                ? cn("text-primary", activeTabClassName)
                : "text-neutral-500 hover:text-neutral-800",
              tab.disabled &&
                cn("cursor-not-allowed opacity-50", disabledTabClassName),
              tabClassName
            )}
          >
            {tab.label}
          </div>
        ))}
        {/* Active tab indicator */}
        <div
          className={cn(
            "absolute bottom-0 h-0.5 bg-primary transition-all duration-300 ease-in-out",
            !visibleIndicator && "opacity-0"
          )}
          style={indicatorStyle}
          aria-hidden="true"
        />
      </div>
    </div>
  );
}

export interface TabPanelProps {
  id: string;
  activeTab: string;
  children: React.ReactNode;
  className?: string;
}

export function TabPanel({
  id,
  activeTab,
  children,
  className,
}: TabPanelProps) {
  const isActive = id === activeTab;

  return (
    <div
      role="tabpanel"
      id={`panel-${id}`}
      aria-labelledby={`tab-${id}`}
      hidden={!isActive}
      className={cn(isActive ? "block" : "hidden", className)}
    >
      {children}
    </div>
  );
}
