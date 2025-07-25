/**
 * Base model interface that all entity types extend
 */
export interface BaseModel {
  id?: number;
  createdAt?: string;
  updatedAt?: string;
  active?: boolean;
}
