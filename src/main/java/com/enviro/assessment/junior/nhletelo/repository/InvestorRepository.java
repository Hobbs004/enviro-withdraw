package com.enviro.assessment.junior.nhletelo.repository;

import org.springframework.stereotype.Repository;
import com.enviro.assessment.junior.nhletelo.model.Investor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface InvestorRepository extends JpaRepository<Investor, Long> {
 Optional<Investor> findByEmail(String email);
}
