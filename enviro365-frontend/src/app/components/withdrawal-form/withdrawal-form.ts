import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { WithdrawalService } from '../../services/withdrawal.service';
import { InvestorService } from '../../services/investor.service';
import { InvestmentProduct } from '../../models/investor.model';
import { WithdrawalNotice } from '../../models/withdrawal.model';

@Component({
  selector: 'app-withdrawal-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './withdrawal-form.html',
  styleUrl: './withdrawal-form.css'
})
export class WithdrawalFormComponent implements OnInit {

  withdrawalForm: FormGroup;
  product: InvestmentProduct | null = null;
  successMessage: string = '';
  errorMessage: string = '';
  loading: boolean = false;
  submittedNotice: WithdrawalNotice | null = null;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private withdrawalService: WithdrawalService,
    private investorService: InvestorService
  ) {
    this.withdrawalForm = this.fb.group({
      productId: ['', Validators.required],
      withdrawalAmount: ['', [
        Validators.required,
        Validators.min(0.01)
      ]]
    });
  }

  ngOnInit(): void {
    // Pre-fill productId from query param when coming from dashboard
    this.route.queryParams.subscribe(params => {
      if (params['productId']) {
        const productId = +params['productId'];
        this.withdrawalForm.patchValue({ productId });
        this.loadProduct(productId);
      }
    });
  }

  loadProduct(productId: number): void {
    this.investorService.getProductsByInvestorId(productId).subscribe({
      next: (response) => {
        // Find the specific product from the list
        const found = response.data.find(p => p.id === productId);
        if (found) this.product = found;
      },
      error: () => {}
    });
  }

  get amount() {
    return this.withdrawalForm.get('withdrawalAmount');
  }

  onSubmit(): void {
    if (this.withdrawalForm.invalid) return;

    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.withdrawalService.createWithdrawal(this.withdrawalForm.value).subscribe({
      next: (response) => {
        this.submittedNotice = response.data;
        this.successMessage = response.message;
        this.loading = false;
        this.withdrawalForm.reset();
      },
      error: (err) => {
        this.errorMessage = err.error?.message || 'Withdrawal failed. Please try again.';
        this.loading = false;
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/dashboard']);
  }
}