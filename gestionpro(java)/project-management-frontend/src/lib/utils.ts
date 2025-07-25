import { clsx, type ClassValue } from "clsx";
import { twMerge } from "tailwind-merge";
import { format, parseISO } from "date-fns";
import { fr } from "date-fns/locale";

/**
 * Combines class names using clsx and ensures Tailwind classes merge properly
 */
export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}

/**
 * Formats an ISO date string to a human-readable format
 */
export function formatDate(
  date: Date | string | undefined | null,
  formatStr: string = "PPP"
): string {
  if (!date) return "-";
  try {
    const dateObj = typeof date === "string" ? parseISO(date) : date;
    return format(dateObj, formatStr, { locale: fr });
  } catch (error) {
    // TODO: Replace with proper logging service in production
    // Only using console.error during development for debugging purposes
    console.error("Date formatting error:", error);
    return typeof date === "string" ? date : date.toString();
  }
}

/**
 * Formats a date with time
 */
export function formatDateTime(
  date: Date | string | undefined | null,
  formatStr: string = "PPP à HH:mm"
): string {
  return formatDate(date, formatStr);
}

/**
 * Formats a number as a percentage
 */
export function formatPercent(value: number | undefined | null): string {
  if (value === null || value === undefined) return "-";
  return `${Math.round(value)}%`;
}

/**
 * Formats a number as currency (Euro)
 */
export function formatCurrency(value: number | undefined | null): string {
  if (value === null || value === undefined) return "-";
  return new Intl.NumberFormat("fr-FR", {
    style: "currency",
    currency: "EUR",
  }).format(value);
}

/**
 * Truncates a string to the specified length and adds an ellipsis
 */
export function truncateText(
  text: string | undefined | null,
  maxLength: number
): string {
  if (!text) return "";
  return text.length > maxLength ? `${text.substring(0, maxLength)}...` : text;
}

/**
 * Generates initials from a name (first letter of first and last name)
 */
export function getInitials(
  firstName: string | undefined | null,
  lastName: string | undefined | null
): string {
  if (!firstName && !lastName) return "?";

  const firstInitial = firstName ? firstName.charAt(0).toUpperCase() : "";
  const lastInitial = lastName ? lastName.charAt(0).toUpperCase() : "";

  return `${firstInitial}${lastInitial}`;
}

/**
 * Adds user-friendly error message based on HTTP status codes
 */
export function getErrorMessage(error: any): string {
  const defaultMessage = "Une erreur s'est produite. Veuillez réessayer.";

  if (!error || !error.response) return defaultMessage;

  const { status } = error.response;

  switch (status) {
    case 400:
      return "Données invalides. Veuillez vérifier vos informations.";
    case 401:
      return "Non autorisé. Veuillez vous connecter.";
    case 403:
      return "Vous n'avez pas les droits pour effectuer cette action.";
    case 404:
      return "Ressource non trouvée.";
    case 409:
      return "Conflit avec l'état actuel de la ressource.";
    case 500:
      return "Erreur serveur. Veuillez réessayer ultérieurement.";
    default:
      return defaultMessage;
  }
}
