package com.enviro.assessment.junior.nhletelo.service;

import com.enviro.assessment.junior.nhletelo.dto.*;
import com.enviro.assessment.junior.nhletelo.model.*;
import com.enviro.assessment.junior.nhletelo.model.InvestmentProduct.ProductType;
import com.enviro.assessment.junior.nhletelo.model.WithdrawalNotice.WithdrawalStatus;
import com.enviro.assessment.junior.nhletelo.exception.ResourceNotFoundException;
import com.enviro.assessment.junior.nhletelo.exception.WithdrawalValidationException;
import com.enviro.assessment.junior.nhletelo.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Core service handling all withdrawal business logic.
 *
 * Business Rules enforced:
 *   1. RETIREMENT products: investor must be older than 65.
 *   2. Withdrawal amount must not exceed the current product balance.
 *   3. Withdrawal amount must not exceed 90% of the current balance.
 */
@Service
@Transactional
public class WithdrawalService {

    private static final BigDecimal MAX_WITHDRAWAL_PERCENT = new BigDecimal("0.90");

    private final WithdrawalNoticeRepository withdrawalRepository;
    private final InvestmentProductRepository productRepository;

    public WithdrawalService(WithdrawalNoticeRepository withdrawalRepository,
                             InvestmentProductRepository productRepository) {
        this.withdrawalRepository = withdrawalRepository;
        this.productRepository = productRepository;
    }

    /**
     * Processes a withdrawal request after enforcing all business rules.
     * Updates the product balance and persists an audit record.
     */
    public WithdrawalNoticeDTO createWithdrawal(WithdrawalRequestDTO request) {
        InvestmentProduct product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Investment product not found with id: " + request.getProductId()));

        Investor investor = product.getInvestor();
        BigDecimal amount = request.getWithdrawalAmount();
        BigDecimal currentBalance = product.getBalance();

        // Rule 1: Retirement age check
        if (product.getProductType() == ProductType.RETIREMENT && investor.getAge() <= 65) {
            throw new WithdrawalValidationException(
                    "Retirement withdrawals are only allowed for investors older than 65. " +
                    "Investor age: " + investor.getAge());
        }

        // Rule 2: Cannot exceed balance
        if (amount.compareTo(currentBalance) > 0) {
            throw new WithdrawalValidationException(
                    String.format("Withdrawal amount R%.2f exceeds the current balance of R%.2f.",
                            amount, currentBalance));
        }

        // Rule 3: Cannot exceed 90% of balance
        BigDecimal maxAllowed = currentBalance.multiply(MAX_WITHDRAWAL_PERCENT)
                .setScale(2, RoundingMode.FLOOR);
        if (amount.compareTo(maxAllowed) > 0) {
            throw new WithdrawalValidationException(
                    String.format("Withdrawal amount R%.2f exceeds 90%% of the balance. " +
                            "Maximum allowed: R%.2f.", amount, maxAllowed));
        }

        // All rules passed — process withdrawal
        BigDecimal balanceBefore = currentBalance;
        BigDecimal balanceAfter = currentBalance.subtract(amount);

        product.setBalance(balanceAfter);
        productRepository.save(product);

        WithdrawalNotice notice = new WithdrawalNotice();
        notice.setProduct(product);
        notice.setWithdrawalAmount(amount);
        notice.setBalanceBefore(balanceBefore);
        notice.setBalanceAfter(balanceAfter);
        notice.setCreatedAt(LocalDateTime.now());
        notice.setStatus(WithdrawalStatus.APPROVED);

        WithdrawalNotice saved = withdrawalRepository.save(notice);

        return mapToDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<WithdrawalNoticeDTO> getWithdrawalsByInvestorId(Long investorId) {
        return withdrawalRepository.findByProductInvestorId(investorId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<WithdrawalNoticeDTO> getWithdrawalsByInvestorIdAndDateRange(
            Long investorId, LocalDateTime from, LocalDateTime to) {
        return withdrawalRepository
                .findByProductInvestorIdAndCreatedAtBetween(investorId, from, to).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private WithdrawalNoticeDTO mapToDTO(WithdrawalNotice notice) {
        WithdrawalNoticeDTO dto = new WithdrawalNoticeDTO();
        dto.setId(notice.getId());
        dto.setProductId(notice.getProduct().getId());
        dto.setProductName(notice.getProduct().getProductName());
        dto.setWithdrawalAmount(notice.getWithdrawalAmount());
        dto.setBalanceBefore(notice.getBalanceBefore());
        dto.setBalanceAfter(notice.getBalanceAfter());
        dto.setCreatedAt(notice.getCreatedAt());
        dto.setStatus(notice.getStatus());
        return dto;
    }
}