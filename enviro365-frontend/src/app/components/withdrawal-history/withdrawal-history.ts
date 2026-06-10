import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { WithdrawalService } from '../../services/withdrawal.service';
import { WithdrawalNotice } from '../../models/withdrawal.model';

@Component({
  selector: 'app-withdrawal-history',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './withdrawal-history.html',
  styleUrl: './withdrawal-history.css'
})
export class WithdrawalHistoryComponent implements OnInit {

  notices: WithdrawalNotice[] = [];
  investorId: number = 0;
  errorMessage: string = '';
  loading: boolean = false;
  filterForm: FormGroup;

  constructor(
    private withdrawalService: WithdrawalService,
    private route: ActivatedRoute,
    private router: Router,
    private fb: FormBuilder
  ) {
    this.filterForm = this.fb.group({
      from: [''],
      to: ['']
    });
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      if (params['investorId']) {
        this.investorId = +params['investorId'];
        this.loadHistory();
      }
    });
  }

  loadHistory(): void {
    this.loading = true;
    this.errorMessage = '';
    this.withdrawalService.getWithdrawalsByInvestorId(this.investorId).subscribe({
      next: (response) => {
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
    if (!from || !to) {
      this.loadHistory();
      return;
    }

    this.loading = true;
    this.errorMessage = '';

    // Convert date inputs to ISO datetime format
    const fromISO = new Date(from).toISOString().slice(0, 19);
    const toISO = new Date(to + 'T23:59:59').toISOString().slice(0, 19);

    this.withdrawalService.getWithdrawalsByDateRange(this.investorId, fromISO, toISO).subscribe({
      next: (response) => {
        this.notices = response.data;
        this.loading = false;
      },
      error: () => {
        this.errorMessage = 'Failed to filter withdrawal history.';
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

  goBack(): void {
    this.router.navigate(['/dashboard']);
  }
}