package com.enviro.assessment.junior.nhletelo.service;

import com.enviro.assessment.junior.nhletelo.dto.*;
import com.enviro.assessment.junior.nhletelo.model.*;
import com.enviro.assessment.junior.nhletelo.exception.ResourceNotFoundException;
import com.enviro.assessment.junior.nhletelo.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/*
 * Service class for investor and portfolio retrieval operations.
 * Maps entities to DTOs to decouple persistence layer from API responses.
 */
@Service
@Transactional(readOnly = true)
public class InvestorService {

    private final InvestorRepository investorRepository;
    private final InvestmentProductRepository productRepository;

    public InvestorService(InvestorRepository investorRepository,
                           InvestmentProductRepository productRepository) {
        this.investorRepository = investorRepository;
        this.productRepository = productRepository;
    }

    public InvestorDTO getInvestorById(Long investorId) {
        Investor investor = investorRepository.findById(investorId)
                .orElseThrow(() -> new ResourceNotFoundException("Investor not found with id: " + investorId));
        return mapToDTO(investor);
    }

    public List<InvestorDTO> getAllInvestors() {
        return investorRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<InvestmentProductDTO> getProductsByInvestorId(Long investorId) {
        investorRepository.findById(investorId)
                .orElseThrow(() -> new ResourceNotFoundException("Investor not found with id: " + investorId));
        return productRepository.findByInvestorId(investorId).stream()
                .map(this::mapToProductDTO)
                .collect(Collectors.toList());
    }

    public InvestorDTO mapToDTO(Investor investor) {
        List<InvestmentProductDTO> products = productRepository
                .findByInvestorId(investor.getId()).stream()
                .map(this::mapToProductDTO)
                .collect(Collectors.toList());

        InvestorDTO dto = new InvestorDTO();
        dto.setId(investor.getId());
        dto.setFirstName(investor.getFirstName());
        dto.setLastName(investor.getLastName());
        dto.setEmail(investor.getEmail());
        dto.setDateOfBirth(investor.getDateOfBirth());
        dto.setAge(investor.getAge());
        dto.setProducts(products);
        return dto;
    }

    public InvestmentProductDTO mapToProductDTO(InvestmentProduct product) {
        InvestmentProductDTO dto = new InvestmentProductDTO();
        dto.setId(product.getId());
        dto.setProductName(product.getProductName());
        dto.setProductType(product.getProductType());
        dto.setBalance(product.getBalance());
        dto.setInvestorId(product.getInvestor().getId());
        return dto;
    }
@Transactional
    public InvestorDTO createInvestor(InvestorDTO dto) {
    Investor investor = new Investor();
    investor.setFirstName(dto.getFirstName());
    investor.setLastName(dto.getLastName());
    investor.setEmail(dto.getEmail());
    investor.setDateOfBirth(dto.getDateOfBirth());
    investor.calculateAge();
    Investor saved = investorRepository.save(investor);
    return mapToDTO(saved);
}

@Transactional
public InvestorDTO updateInvestor(Long id, InvestorDTO dto) {
    Investor investor = investorRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Investor not found with id: " + id));
    investor.setFirstName(dto.getFirstName());
    investor.setLastName(dto.getLastName());
    investor.setEmail(dto.getEmail());
    investor.setDateOfBirth(dto.getDateOfBirth());
    investor.calculateAge();
    Investor saved = investorRepository.save(investor);
    return mapToDTO(saved);
}

@Transactional
public void deleteInvestor(Long id) {
    Investor investor = investorRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Investor not found with id: " + id));
    investorRepository.delete(investor);
}
}