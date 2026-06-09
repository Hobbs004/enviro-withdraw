package com.enviro.assessment.junior.nhletelo.repository;

import org.springframework.stereotype.Repository;
import com.enviro.assessment.junior.nhletelo.model.WithdrawalNotice;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


@Repository
public interface WithdrawalNoticeRepository extends JpaRepository<WithdrawalNotice, Long> {
    List<WithdrawalNotice> findByProductInvestorId(Long investorId);
    List<WithdrawalNotice> findByProductInvestorIdAndCreatedAtBetween(
            Long investorId, LocalDateTime from, LocalDateTime to);
    List<WithdrawalNotice> findByProductId(Long productId);
}
