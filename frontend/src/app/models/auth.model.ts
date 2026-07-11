export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  email: string;
  role: 'READ_ONLY' | 'READ_WRITE';
}