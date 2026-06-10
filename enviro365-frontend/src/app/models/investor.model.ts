export interface InvestmentProduct {
  id: number;
  productName: string;
  productType: 'RETIREMENT' | 'SAVINGS';
  balance: number;
  investorId: number;
}

export interface Investor {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  dateOfBirth: string;
  age: number;
  products: InvestmentProduct[];
}