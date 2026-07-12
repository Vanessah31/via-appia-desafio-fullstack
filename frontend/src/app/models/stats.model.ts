export interface StatsResponse {
  porStatus: Record<string, number>;
  porPrioridade: Record<string, number>;
  total: number;
}