import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { InvestorService } from '../../services/investor.service';
import { InvestmentProductService } from '../../services/investment-product';
import { Investor } from '../../models/investor.model';

@Component({
  selector: 'app-add-product',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './add-product.html',
  styleUrl: './add-product.css'
})
export class AddProductComponent implements OnInit {

  productForm: FormGroup;
  investors: Investor[] = [];
  successMessage: string = '';
  errorMessage: string = '';

  constructor(
    private investorService: InvestorService,
    private productService: InvestmentProductService,
    private fb: FormBuilder
  ) {
    this.productForm = this.fb.group({
      productName: ['', Validators.required],
      productType: ['', Validators.required],
      balance: ['', [Validators.required, Validators.min(0.01)]],
      investorId: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.investorService.getAllInvestors().subscribe({
      next: (response: any) => { this.investors = response.data; },
      error: () => { this.errorMessage = 'Failed to load investors.'; }
    });
  }

  createProduct(): void {
    if (this.productForm.invalid) return;
    this.errorMessage = '';
    this.successMessage = '';
    this.productService.createProduct(this.productForm.value).subscribe({
      next: (response: any) => {
        this.successMessage = `Product ${response.data.productName} created successfully!`;
        this.productForm.reset();
      },
      error: (err: any) => {
        this.errorMessage = err.error?.message || 'Failed to create product.';
      }
    });
  }
}