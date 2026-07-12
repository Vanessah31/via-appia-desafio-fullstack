import { Component, OnInit, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ApiClient } from '../../../core/services/api-client';
import { AuthService } from '../../../core/services/auth.service';
import { Incident } from '../../../models/incident.model';
import { Comment, CommentRequest } from '../../../models/comment.model';

@Component({
  selector: 'app-incident-detail',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './incident-detail.html',
  styleUrl: './incident-detail.scss'
})
export class IncidentDetail implements OnInit {
  private fb = inject(FormBuilder);
  private api = inject(ApiClient);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  authService = inject(AuthService);

  incidentId = signal<string>('');
  incident = signal<Incident | null>(null);
  comments = signal<Comment[]>([]);

  loading = signal(false);
  errorMessage = signal<string | null>(null);
  commentError = signal<string | null>(null);
  updatingStatus = signal(false);

  canWrite = computed(() => this.authService.canWrite());

  statusOptions = ['ABERTA', 'EM_ANDAMENTO', 'RESOLVIDA', 'CANCELADA'];

  commentForm = this.fb.group({
    autor: ['', [Validators.required, Validators.maxLength(120)]],
    mensagem: ['', [Validators.required, Validators.maxLength(2000)]]
  });

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id')!;
    this.incidentId.set(id);
    this.loadIncident();
    this.loadComments();
  }

  loadIncident(): void {
    this.loading.set(true);
    this.api.getOne<Incident>(`/incidents/${this.incidentId()}`).subscribe({
      next: (data) => {
        this.incident.set(data);
        this.loading.set(false);
      },
      error: () => {
        this.errorMessage.set('Não foi possível carregar a ocorrência.');
        this.loading.set(false);
      }
    });
  }

  loadComments(): void {
    this.api.getOne<Comment[]>(`/incidents/${this.incidentId()}/comments`).subscribe({
      next: (data) => this.comments.set(data),
      error: () => {
        // silencioso: comentários são complementares à tela principal
      }
    });
  }

  changeStatus(newStatus: string): void {
    if (!this.canWrite()) {
      return;
    }
    this.updatingStatus.set(true);
    this.api.patch<Incident>(`/incidents/${this.incidentId()}/status`, { status: newStatus }).subscribe({
      next: (updated) => {
        this.incident.set(updated);
        this.updatingStatus.set(false);
      },
      error: () => {
        this.errorMessage.set('Não foi possível atualizar o status.');
        this.updatingStatus.set(false);
      }
    });
  }

  submitComment(): void {
    if (this.commentForm.invalid) {
      this.commentForm.markAllAsTouched();
      return;
    }

    this.commentError.set(null);
    const payload = this.commentForm.getRawValue() as CommentRequest;

    this.api.post<Comment>(`/incidents/${this.incidentId()}/comments`, payload).subscribe({
      next: () => {
        this.commentForm.reset();
        this.loadComments();
      },
      error: () => {
        this.commentError.set('Não foi possível adicionar o comentário.');
      }
    });
  }

  deleteIncident(): void {
    if (!this.canWrite()) {
      return;
    }
    if (!confirm('Tem certeza que deseja excluir esta ocorrência? Essa ação não pode ser desfeita.')) {
      return;
    }

    this.api.delete(`/incidents/${this.incidentId()}`).subscribe({
      next: () => this.router.navigate(['/incidents']),
      error: () => this.errorMessage.set('Não foi possível excluir a ocorrência.')
    });
  }

  goBack(): void {
    this.router.navigate(['/incidents']);
  }
}