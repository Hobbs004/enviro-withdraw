import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { WithdrawalService } from '../../services/withdrawal.service';
import { InvestorService } from '../../services/investor.service';
import { WithdrawalNotice } from '../../models/withdrawal.model';
import { Investor } from '../../models/investor.model';

@Component({
  selector: 'app-withdrawal-history',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './withdrawal-history.html',
  styleUrl: './withdrawal-history.css'
})
export class WithdrawalHistoryComponent implements OnInit {

  notices: WithdrawalNotice[] = [];
  investors: Investor[] = [];
  investorId: number = 0;
  selectedInvestorName: string = '';
  errorMessage: string = '';
  loading: boolean = false;
  filterForm: FormGroup;

  constructor(
    private withdrawalService: WithdrawalService,
    private investorService: InvestorService,
    private route: ActivatedRoute,
    private fb: FormBuilder
  ) {
    this.filterForm = this.fb.group({ from: [''], to: [''] });
  }

  ngOnInit(): void {
    // Load all investors for the dropdown
    this.investorService.getAllInvestors().subscribe({
      next: (response: any) => {
        this.investors = response.data;

        // If coming from dashboard with investorId query param
        this.route.queryParams.subscribe(params => {
          if (params['investorId']) {
            this.investorId = +params['investorId'];
            const found = this.investors.find(i => i.id === this.investorId);
            if (found) {
              this.selectedInvestorName = `${found.firstName} ${found.lastName}`;
            }
            this.loadHistory();
          }
        });
      },
      error: () => { this.errorMessage = 'Failed to load investors.'; }
    });
  }

  onInvestorChange(event: any): void {
    this.investorId = +event.target.value;
    this.notices = [];
    this.filterForm.reset();

    const found = this.investors.find(i => i.id === this.investorId);
    this.selectedInvestorName = found ? `${found.firstName} ${found.lastName}` : '';

    if (this.investorId) this.loadHistory();
  }

  loadHistory(): void {
    this.loading = true;
    this.errorMessage = '';
    this.withdrawalService.getWithdrawalsByInvestorId(this.investorId).subscribe({
      next: (response: any) => {
        this.notices = response.data;
        this.loading = false;
      },
      error: () => {
        this.errorMessage = 'Failed to load withdrawal history.';
        this.loading = false;
      }
    });
  }

  applyFilter(): void {
    const { from, to } = this.filterForm.value;
    if (!from || !to) { this.loadHistory(); return; }

    this.loading = true;
    const fromISO = new Date(from).toISOString().slice(0, 19);
    const toISO = new Date(to + 'T23:59:59').toISOString().slice(0, 19);

    this.withdrawalService.getWithdrawalsByDateRange(this.investorId, fromISO, toISO).subscribe({
      next: (response: any) => {
        this.notices = response.data;
        this.loading = false;
      },
      error: () => {
        this.errorMessage = 'Failed to filter history.';
        this.loading = false;
      }
    });
  }

  clearFilter(): void {
    this.filterForm.reset();
    this.loadHistory();
  }

  exportCsv(): void {
    this.withdrawalService.exportCsv(this.investorId);
  }
}