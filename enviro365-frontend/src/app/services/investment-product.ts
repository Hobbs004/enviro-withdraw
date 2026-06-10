import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { InvestmentProduct } from '../models/investor.model';
import { ApiResponse } from '../models/api-response.model';

@Injectable({
  providedIn: 'root'
})
export class InvestmentProductService {

  private baseUrl = 'http://localhost:8080/api/products';

  constructor(private http: HttpClient) {}

  createProduct(product: Partial<InvestmentProduct>): Observable<ApiResponse<InvestmentProduct>> {
    return this.http.post<ApiResponse<InvestmentProduct>>(this.baseUrl, product);
  }

  getAllProducts(): Observable<ApiResponse<InvestmentProduct[]>> {
    return this.http.get<ApiResponse<InvestmentProduct[]>>(this.baseUrl);
  }

  getProductById(id: number): Observable<ApiResponse<InvestmentProduct>> {
    return this.http.get<ApiResponse<InvestmentProduct>>(`${this.baseUrl}/${id}`);
  }

  updateProduct(id: number, product: Partial<InvestmentProduct>): Observable<ApiResponse<InvestmentProduct>> {
    return this.http.put<ApiResponse<InvestmentProduct>>(`${this.baseUrl}/${id}`, product);
  }

  deleteProduct(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.baseUrl}/${id}`);
  }
}