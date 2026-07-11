import { inject, Injectable, signal } from '@angular/core';
import { ApiClient } from './api-client';
import { LoginRequest, LoginResponse } from '../../models/auth.model';
import { tap } from 'rxjs';

const TOKEN_KEY = 'via_appia_token';
const EMAIL_KEY = 'via_appia_email';
const ROLE_KEY = 'via_appia_role';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private api = inject(ApiClient);

  // signal reativo com o estado de autenticação atual
  currentRole = signal<string | null>(localStorage.getItem(ROLE_KEY));
  currentEmail = signal<string | null>(localStorage.getItem(EMAIL_KEY));

  login(request: LoginRequest) {
    return this.api.post<LoginResponse>('/auth/login', request).pipe(
      tap((response) => this.saveSession(response))
    );
  }

  logout(): void {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(EMAIL_KEY);
    localStorage.removeItem(ROLE_KEY);
    this.currentRole.set(null);
    this.currentEmail.set(null);
  }

  getToken(): string | null {
    return localStorage.getItem(TOKEN_KEY);
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  canWrite(): boolean {
    return this.currentRole() === 'READ_WRITE';
  }

  private saveSession(response: LoginResponse): void {
    localStorage.setItem(TOKEN_KEY, response.token);
    localStorage.setItem(EMAIL_KEY, response.email);
    localStorage.setItem(ROLE_KEY, response.role);
    this.currentRole.set(response.role);
    this.currentEmail.set(response.email);
  }
}