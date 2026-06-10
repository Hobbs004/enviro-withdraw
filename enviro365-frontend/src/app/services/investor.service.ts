import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Investor, InvestmentProduct } from '../models/investor.model';
import { ApiResponse } from '../models/api-response.model';

@Injectable({
  providedIn: 'root'
})
export class InvestorService {

  private baseUrl = 'http://localhost:8080/api/investors';

  constructor(private http: HttpClient) {}

  getAllInvestors(): Observable<ApiResponse<Investor[]>> {
    return this.http.get<ApiResponse<Investor[]>>(this.baseUrl);
  }

  getInvestorById(id: number): Observable<ApiResponse<Investor>> {
    return this.http.get<ApiResponse<Investor>>(`${this.baseUrl}/${id}`);
  }

  getProductsByInvestorId(id: number): Observable<ApiResponse<InvestmentProduct[]>> {
    return this.http.get<ApiResponse<InvestmentProduct[]>>(`${this.baseUrl}/${id}/products`);
  }

  createInvestor(investor: Partial<Investor>): Observable<ApiResponse<Investor>> {
    return this.http.post<ApiResponse<Investor>>(this.baseUrl, investor);
  }

  updateInvestor(id: number, investor: Partial<Investor>): Observable<ApiResponse<Investor>> {
    return this.http.put<ApiResponse<Investor>>(`${this.baseUrl}/${id}`, investor);
  }

  deleteInvestor(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.baseUrl}/${id}`);
  }
}