export interface Comment {
  id: string;
  incidentId: string;
  autor: string;
  mensagem: string;
  dataCriacao: string;
}

export interface CommentRequest {
  autor: string;
  mensagem: string;
}