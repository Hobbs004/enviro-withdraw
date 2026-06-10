export interface WithdrawalRequest {
  productId: number;
  withdrawalAmount: number;
}

export interface WithdrawalNotice {
  id: number;
  productId: number;
  productName: string;
  withdrawalAmount: number;
  balanceBefore: number;
  balanceAfter: number;
  createdAt: string;
  status: 'PENDING' | 'APPROVED' | 'REJECTED';
}