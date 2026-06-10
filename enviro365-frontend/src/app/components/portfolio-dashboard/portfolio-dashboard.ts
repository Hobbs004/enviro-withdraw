import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { InvestorService } from '../../services/investor.service';
import { Investor } from '../../models/investor.model';

@Component({
  selector: 'app-portfolio-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './portfolio-dashboard.html',
  styleUrl: './portfolio-dashboard.css'
})
export class PortfolioDashboardComponent implements OnInit {

  investors: Investor[] = [];
  selectedInvestor: Investor | null = null;
  errorMessage: string = '';
  loading: boolean = false;

  constructor(private investorService: InvestorService, private router: Router) {}

  ngOnInit(): void {
    this.loadInvestors();
  }

  loadInvestors(): void {
    this.loading = true;
    this.investorService.getAllInvestors().subscribe({
      next: (response: any) => {
        this.investors = response.data;
        this.loading = false;
      },
      error: () => {
        this.errorMessage = 'Failed to load investors. Is the backend running?';
        this.loading = false;
      }
    });
  }

  selectInvestor(investor: Investor): void {
    this.investorService.getInvestorById(investor.id).subscribe({
      next: (response: any) => { this.selectedInvestor = response.data; },
      error: () => { this.errorMessage = 'Failed to load investor details.'; }
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