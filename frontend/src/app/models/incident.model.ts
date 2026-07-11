export type Prioridade = 'BAIXA' | 'MEDIA' | 'ALTA';
export type Status = 'ABERTA' | 'EM_ANDAMENTO' | 'RESOLVIDA' | 'CANCELADA';

export interface Incident {
  id: string;
  titulo: string;
  descricao: string;
  prioridade: Prioridade;
  status: Status;
  responsavelEmail: string;
  tags: string[];
  dataAbertura: string;
  dataAtualizacao: string;
}

export interface IncidentRequest {
  titulo: string;
  descricao: string;
  prioridade: Prioridade;
  status: Status;
  responsavelEmail: string;
  tags: string[];
}

export interface PageResponse<T> {
  content: T[];
  page: {
    size: number;
    number: number;
    totalElements: number;
    totalPages: number;
  };
}