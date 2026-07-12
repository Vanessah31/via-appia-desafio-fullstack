import { Routes } from '@angular/router';
import { Login } from './features/login/login';
import { IncidentsList } from './features/incidents/incidents-list/incidents-list';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: Login },
  { path: 'incidents', component: IncidentsList, canActivate: [authGuard] }
];