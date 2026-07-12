import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ApiClient } from '../../../core/services/api-client';
import { Incident, IncidentRequest } from '../../../models/incident.model';

@Component({
  selector: 'app-incident-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './incident-form.html',
  styleUrl: './incident-form.scss'
})
export class IncidentForm implements OnInit {
  private fb = inject(FormBuilder);
  private api = inject(ApiClient);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  incidentId = signal<string | null>(null);
  isEditMode = signal(false);
  loading = signal(false);
  errorMessage = signal<string | null>(null);

  statusOptions = ['ABERTA', 'EM_ANDAMENTO', 'RESOLVIDA', 'CANCELADA'];
  prioridadeOptions = ['BAIXA', 'MEDIA', 'ALTA'];

  form = this.fb.group({
    titulo: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(120)]],
    descricao: ['', [Validators.maxLength(5000)]],
    prioridade: ['MEDIA', [Validators.required]],
    status: ['ABERTA', [Validators.required]],
    responsavelEmail: ['', [Validators.required, Validators.email]],
    tags: ['']
  });

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.incidentId.set(id);
      this.isEditMode.set(true);
      this.loadIncident(id);
    }
  }

  private loadIncident(id: string): void {
    this.loading.set(true);
    this.api.getOne<Incident>(`/incidents/${id}`).subscribe({
      next: (incident) => {
        this.form.patchValue({
          titulo: incident.titulo,
          descricao: incident.descricao,
          prioridade: incident.prioridade,
          status: incident.status,
          responsavelEmail: incident.responsavelEmail,
          tags: incident.tags.join(', ')
        });
        this.loading.set(false);
      },
      error: () => {
        this.errorMessage.set('Não foi possível carregar a ocorrência.');
        this.loading.set(false);
      }
    });
  }

  // Item de DRY (frontend, item B): normaliza o valor do formulário antes de enviar
  private normalizeIncidentFormValue(raw: any): IncidentRequest {
    return {
      titulo: (raw.titulo ?? '').trim(),
      descricao: (raw.descricao ?? '').trim(),
      prioridade: raw.prioridade,
      status: raw.status,
      responsavelEmail: (raw.responsavelEmail ?? '').trim().toLowerCase(),
      tags: (raw.tags ?? '')
        .split(',')
        .map((t: string) => t.trim().toLowerCase())
        .filter((t: string) => t.length > 0)
    };
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading.set(true);
    this.errorMessage.set(null);

    const payload = this.normalizeIncidentFormValue(this.form.getRawValue());

    const request$ = this.isEditMode()
      ? this.api.put<Incident>(`/incidents/${this.incidentId()}`, payload)
      : this.api.post<Incident>('/incidents', payload);

    request$.subscribe({
      next: () => {
        this.loading.set(false);
        this.router.navigate(['/incidents']);
      },
      error: () => {
        this.loading.set(false);
        this.errorMessage.set('Não foi possível salvar a ocorrência. Verifique os dados.');
      }
    });
  }

  cancel(): void {
    this.router.navigate(['/incidents']);
  }
}