export interface TableState {
  page: number;
  size: number;
  sort?: string;
}

export interface IncidentFilters {
  status?: string | null;
  prioridade?: string | null;
  q?: string | null;
}

export function buildQueryParams(tableState: TableState, filters: IncidentFilters = {}): Record<string, any> {
  return {
    page: tableState.page,
    size: tableState.size,
    sort: tableState.sort,
    status: filters.status,
    prioridade: filters.prioridade,
    q: filters.q
  };
}