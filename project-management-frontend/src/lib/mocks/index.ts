/**
 * Centralized export for all mock data
 * This allows for easier imports in components
 */

export * from "./dashboard";
export * from "./kpis";

// Add more exports here when adding new mock data files

// Helper functions for working with mocks
import { apiConfig } from "../api";

/**
 * Get data from mock or API based on configuration
 * @param mockData The mock data to return if using mocks
 * @param apiCall The API call function to execute if not using mocks
 * @returns Promise that resolves to the data
 */
export async function getDataFromMockOrApi<T>(
  mockData: T,
  apiCall: () => Promise<{ data: T }>
): Promise<T> {
  if (apiConfig.USE_MOCKS) {
    // Return mock data with a delay to simulate API call
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve(mockData);
      }, 300);
    });
  }

  // Call the real API
  const response = await apiCall();
  return response.data;
}
