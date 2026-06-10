import { Routes } from '@angular/router';
import { PortfolioDashboardComponent } from './components/portfolio-dashboard/portfolio-dashboard';
import { WithdrawalFormComponent } from './components/withdrawal-form/withdrawal-form';
import { WithdrawalHistoryComponent } from './components/withdrawal-history/withdrawal-history';

export const routes: Routes = [
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: PortfolioDashboardComponent },
  { path: 'withdrawal', component: WithdrawalFormComponent },
  { path: 'history', component: WithdrawalHistoryComponent },
  { path: '**', redirectTo: 'dashboard' }
];