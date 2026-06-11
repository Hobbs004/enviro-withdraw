import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { WithdrawalService } from '../../services/withdrawal.service';
import { InvestorService } from '../../services/investor.service';
import { Investor, InvestmentProduct } from '../../models/investor.model';
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
  investors: Investor[] = [];
  products: InvestmentProduct[] = [];
  selectedProduct: InvestmentProduct | null = null;
  successMessage: string = '';
  errorMessage: string = '';
  loading: boolean = false;
  submittedNotice: WithdrawalNotice | null = null;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private withdrawalService: WithdrawalService,
    private investorService: InvestorService
  ) {
    this.withdrawalForm = this.fb.group({
      investorId: ['', Validators.required],
      productId: ['', Validators.required],
      withdrawalAmount: ['', [Validators.required, Validators.min(0.01)]]
    });
  }

  ngOnInit(): void {
    this.investorService.getAllInvestors().subscribe({
      next: (response: any) => { this.investors = response.data; },
      error: () => { this.errorMessage = 'Failed to load investors.'; }
    });
  }

  onInvestorChange(): void {
    const investorId = this.withdrawalForm.get('investorId')?.value;
    this.products = [];
    this.selectedProduct = null;
    this.withdrawalForm.patchValue({ productId: '' });

    if (!investorId) return;

    this.investorService.getInvestorById(+investorId).subscribe({
      next: (response: any) => { this.products = response.data.products; },
      error: () => { this.errorMessage = 'Failed to load products.'; }
    });
  }

  onProductChange(): void {
    const productId = this.withdrawalForm.get('productId')?.value;
    this.selectedProduct = this.products.find(p => p.id === +productId) || null;
  }

  get amount() {
    return this.withdrawalForm.get('withdrawalAmount');
  }

  onSubmit(): void {
    if (this.withdrawalForm.invalid) return;
    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    const payload = {
      productId: +this.withdrawalForm.value.productId,
      withdrawalAmount: +this.withdrawalForm.value.withdrawalAmount
    };

    this.withdrawalService.createWithdrawal(payload).subscribe({
      next: (response: any) => {
        this.submittedNotice = response.data;
        this.successMessage = response.message;
        this.loading = false;
        this.withdrawalForm.reset();
        this.products = [];
        this.selectedProduct = null;
      },
      error: (err: any) => {
        this.errorMessage = err.error?.message || 'Withdrawal failed. Please try again.';
        this.loading = false;
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/dashboard']);
  }
}