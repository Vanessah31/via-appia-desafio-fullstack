import { Routes } from '@angular/router';
import { Login } from './features/login/login';
import { IncidentsList } from './features/incidents/incidents-list/incidents-list';
import { IncidentForm } from './features/incidents/incident-form/incident-form';
import { IncidentDetail } from './features/incidents/incident-detail/incident-detail';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: Login },
  { path: 'incidents', component: IncidentsList, canActivate: [authGuard] },
  { path: 'incidents/new', component: IncidentForm, canActivate: [authGuard] },
  { path: 'incidents/:id/edit', component: IncidentForm, canActivate: [authGuard] },
  { path: 'incidents/:id', component: IncidentDetail, canActivate: [authGuard] }
];