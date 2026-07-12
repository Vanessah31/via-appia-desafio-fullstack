import { Component, OnInit, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ApiClient } from '../../../core/services/api-client';
import { AuthService } from '../../../core/services/auth.service';
import { buildQueryParams } from '../../../core/utils/build-query-params';
import { Incident } from '../../../models/incident.model';
import { PageResponse } from '../../../models/incident.model';
import { StatsResponse } from '../../../models/stats.model';

@Component({
  selector: 'app-incidents-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './incidents-list.html',
  styleUrl: './incidents-list.scss'
})
export class IncidentsList implements OnInit {
  private api = inject(ApiClient);
  authService = inject(AuthService);
  private router = inject(Router);

  incidents = signal<Incident[]>([]);
  stats = signal<StatsResponse | null>(null);
  loading = signal(false);
  errorMessage = signal<string | null>(null);

  // filtros
  statusFilter = signal<string>('');
  prioridadeFilter = signal<string>('');
  searchTerm = signal<string>('');

  // paginação e ordenação
  page = signal(0);
  size = signal(10);
  sortField = signal('dataAbertura');
  sortDirection = signal<'asc' | 'desc'>('desc');

  totalElements = signal(0);
  totalPages = signal(0);

  canWrite = computed(() => this.authService.canWrite());

  statusOptions = ['ABERTA', 'EM_ANDAMENTO', 'RESOLVIDA', 'CANCELADA'];
  prioridadeOptions = ['BAIXA', 'MEDIA', 'ALTA'];

  ngOnInit(): void {
    this.loadIncidents();
    this.loadStats();
  }

  loadIncidents(): void {
    this.loading.set(true);
    this.errorMessage.set(null);

    const params = buildQueryParams(
      {
        page: this.page(),
        size: this.size(),
        sort: `${this.sortField()},${this.sortDirection()}`
      },
      {
        status: this.statusFilter() || null,
        prioridade: this.prioridadeFilter() || null,
        q: this.searchTerm() || null
      }
    );

    this.api.getPage<PageResponse<Incident>>('/incidents', params).subscribe({
      next: (response) => {
        this.incidents.set(response.content);
        this.totalElements.set(response.page.totalElements);
        this.totalPages.set(response.page.totalPages);
        this.loading.set(false);
      },
      error: () => {
        this.errorMessage.set('Não foi possível carregar os incidents.');
        this.loading.set(false);
      }
    });
  }

  loadStats(): void {
    const params = {
      status: this.statusFilter() || null,
      prioridade: this.prioridadeFilter() || null,
      q: this.searchTerm() || null
    };

    this.api.getPage<StatsResponse>('/stats/incidents', params).subscribe({
      next: (response) => this.stats.set(response),
      error: () => {
        // stats são um complemento visual; se falhar, não bloqueia a lista principal
      }
    });
  }

  applyFilters(): void {
    this.page.set(0);
    this.loadIncidents();
    this.loadStats();
  }

  clearFilters(): void {
    this.statusFilter.set('');
    this.prioridadeFilter.set('');
    this.searchTerm.set('');
    this.applyFilters();
  }

  changeSort(field: string): void {
    if (this.sortField() === field) {
      this.sortDirection.set(this.sortDirection() === 'asc' ? 'desc' : 'asc');
    } else {
      this.sortField.set(field);
      this.sortDirection.set('asc');
    }
    this.loadIncidents();
  }

  goToPage(newPage: number): void {
    if (newPage < 0 || newPage >= this.totalPages()) {
      return;
    }
    this.page.set(newPage);
    this.loadIncidents();
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  countFor(map: Record<string, number> | undefined, key: string): number {
    return map?.[key] ?? 0;
  }
}