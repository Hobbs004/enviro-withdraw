import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { InvestorService } from '../../services/investor.service';
import { InvestmentProductService } from '../../services/investment-product';
import { Investor } from '../../models/investor.model';

@Component({
  selector: 'app-portfolio-dashboard',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './portfolio-dashboard.html',
  styleUrl: './portfolio-dashboard.css'
})
export class PortfolioDashboardComponent implements OnInit {

  investors: Investor[] = [];
  selectedInvestor: Investor | null = null;
  errorMessage: string = '';
  successMessage: string = '';
  loading: boolean = false;

  investorForm: FormGroup;
  productForm: FormGroup;

  constructor(
    private investorService: InvestorService,
    private productService: InvestmentProductService,
    private router: Router,
    private fb: FormBuilder
  ) {
    this.investorForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      dateOfBirth: ['', Validators.required]
    });

    this.productForm = this.fb.group({
      productName: ['', Validators.required],
      productType: ['', Validators.required],
      balance: ['', [Validators.required, Validators.min(0.01)]],
      investorId: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.loadInvestors();
  }

  loadInvestors(): void {
    this.loading = true;
    this.investorService.getAllInvestors().subscribe({
      next: (response) => {
        this.investors = response.data;
        this.loading = false;
      },
      error: () => {
        this.errorMessage = 'Failed to load investors. Is the backend running?';
        this.loading = false;
      }
    });
  }

  createInvestor(): void {
    if (this.investorForm.invalid) return;
    this.investorService.createInvestor(this.investorForm.value).subscribe({
      next: (response) => {
        this.successMessage = `Investor ${response.data.firstName} created successfully!`;
        this.investorForm.reset();
        this.loadInvestors();
      },
      error: (err) => {
        this.errorMessage = err.error?.message || 'Failed to create investor.';
      }
    });
  }

  createProduct(): void {
    if (this.productForm.invalid) return;
    this.productService.createProduct(this.productForm.value).subscribe({
      next: (response) => {
        this.successMessage = `Product ${response.data.productName} created successfully!`;
        this.productForm.reset();
        this.loadInvestors();
      },
      error: (err) => {
        this.errorMessage = err.error?.message || 'Failed to create product.';
      }
    });
  }

  selectInvestor(investor: Investor): void {
    this.investorService.getInvestorById(investor.id).subscribe({
      next: (response) => {
        this.selectedInvestor = response.data;
      },
      error: () => {
        this.errorMessage = 'Failed to load investor details.';
      }
    });
  }

  goToWithdrawal(productId: number): void {
    this.router.navigate(['/withdrawal'], { queryParams: { productId } });
  }

  goToHistory(): void {
    if (this.selectedInvestor) {
      this.router.navigate(['/history'], { queryParams: { investorId: this.selectedInvestor.id } });
    }
  }
}