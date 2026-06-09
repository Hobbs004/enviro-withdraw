package com.enviro.assessment.junior.nhletelo.dto;

import com.enviro.assessment.junior.nhletelo.model.WithdrawalNotice.WithdrawalStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WithdrawalNoticeDTO {
    private Long id;
    private Long productId;
    private String productName;
    private BigDecimal withdrawalAmount;
    private BigDecimal balanceBefore;
    private BigDecimal balanceAfter;
    private LocalDateTime createdAt;
    private WithdrawalStatus status;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getProductId() {
        return productId;
    }
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public BigDecimal getWithdrawalAmount() {
        return withdrawalAmount;
    }
    public void setWithdrawalAmount(BigDecimal withdrawalAmount) {
        this.withdrawalAmount = withdrawalAmount;
    }
    public BigDecimal getBalanceBefore() {
        return balanceBefore;
    }
    public void setBalanceBefore(BigDecimal balanceBefore) {
        this.balanceBefore = balanceBefore;
    }
    public BigDecimal getBalanceAfter() {
        return balanceAfter;
    }
    public void setBalanceAfter(BigDecimal balanceAfter) {
        this.balanceAfter = balanceAfter;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt( LocalDateTime createdAt){
        this.createdAt = createdAt;
    }
    public WithdrawalStatus getStatus(){
        return status;
    }
    public void setStatus(WithdrawalStatus status){
        this.status = status;
    }
}

