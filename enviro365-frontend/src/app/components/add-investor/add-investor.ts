import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { InvestorService } from '../../services/investor.service';

@Component({
  selector: 'app-add-investor',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './add-investor.html',
  styleUrl: './add-investor.css'
})
export class AddInvestorComponent {

  investorForm: FormGroup;
  successMessage: string = '';
  errorMessage: string = '';

  constructor(private investorService: InvestorService, private fb: FormBuilder) {
    this.investorForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      dateOfBirth: ['', Validators.required]
    });
  }

  createInvestor(): void {
    if (this.investorForm.invalid) return;
    this.errorMessage = '';
    this.successMessage = '';
    this.investorService.createInvestor(this.investorForm.value).subscribe({
      next: (response: any) => {
        this.successMessage = `Investor ${response.data.firstName} ${response.data.lastName} created successfully!`;
        this.investorForm.reset();
      },
      error: (err: any) => {
        this.errorMessage = err.error?.message || 'Failed to create investor.';
      }
    });
  }
}