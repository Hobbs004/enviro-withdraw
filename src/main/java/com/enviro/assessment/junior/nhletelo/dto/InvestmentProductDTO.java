package com.enviro.assessment.junior.nhletelo.dto;

import java.math.BigDecimal;

import com.enviro.assessment.junior.nhletelo.model.InvestmentProduct.ProductType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvestmentProductDTO{
    private Long id;
    private String productName;
    private ProductType productType;
    private BigDecimal balance;
    private Long investorId;

public Long getId() {
    return id;
}
public void setId(Long id) {
    this.id = id;
}
public String getProductName() {
    return productName;
}
public void setProductName(String productName) {
    this.productName = productName;
}
public ProductType getProductType() {
    return productType;
}
public void setProductType(ProductType productType) {
    this.productType = productType;
}
public BigDecimal getBalance() {
    return balance;
}
public void setBalance(BigDecimal balance) {
    this.balance = balance;
}
public Long getInvestorId() {
    return investorId;
}
public void setInvestorId(Long investorId) {
    this.investorId = investorId;
}
}