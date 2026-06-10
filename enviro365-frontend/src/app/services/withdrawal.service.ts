import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { WithdrawalRequest, WithdrawalNotice } from '../models/withdrawal.model';
import { ApiResponse } from '../models/api-response.model';

@Injectable({
  providedIn: 'root'
})
export class WithdrawalService {

  private baseUrl = 'http://localhost:8080/api/withdrawals';

  constructor(private http: HttpClient) {}

  createWithdrawal(request: WithdrawalRequest): Observable<ApiResponse<WithdrawalNotice>> {
    return this.http.post<ApiResponse<WithdrawalNotice>>(this.baseUrl, request);
  }

  getWithdrawalsByInvestorId(investorId: number): Observable<ApiResponse<WithdrawalNotice[]>> {
    return this.http.get<ApiResponse<WithdrawalNotice[]>>(`${this.baseUrl}/investor/${investorId}`);
  }

  getWithdrawalsByDateRange(investorId: number, from: string, to: string): Observable<ApiResponse<WithdrawalNotice[]>> {
    const params = new HttpParams()
      .set('from', from)
      .set('to', to);
    return this.http.get<ApiResponse<WithdrawalNotice[]>>(`${this.baseUrl}/investor/${investorId}`, { params });
  }

  exportCsv(investorId: number): void {
    window.open(`${this.baseUrl}/investor/${investorId}/export`, '_blank');
  }
}