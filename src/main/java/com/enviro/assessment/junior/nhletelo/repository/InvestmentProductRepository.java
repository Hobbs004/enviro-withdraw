package com.enviro.assessment.junior.nhletelo.repository;

import org.springframework.stereotype.Repository;
import com.enviro.assessment.junior.nhletelo.model.InvestmentProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface InvestmentProductRepository extends JpaRepository<InvestmentProduct, Long> {
  List<InvestmentProduct> findByInvestorId(Long investorId);
}
