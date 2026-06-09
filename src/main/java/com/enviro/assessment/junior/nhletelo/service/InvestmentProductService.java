package com.enviro.assessment.junior.nhletelo.service;

import com.enviro.assessment.junior.nhletelo.dto.InvestmentProductDTO;
import com.enviro.assessment.junior.nhletelo.model.InvestmentProduct;
import com.enviro.assessment.junior.nhletelo.model.Investor;
import com.enviro.assessment.junior.nhletelo.exception.ResourceNotFoundException;
import com.enviro.assessment.junior.nhletelo.repository.InvestmentProductRepository;
import com.enviro.assessment.junior.nhletelo.repository.InvestorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class InvestmentProductService {

    private final InvestmentProductRepository productRepository;
    private final InvestorRepository investorRepository;

    public InvestmentProductService(InvestmentProductRepository productRepository,
                                    InvestorRepository investorRepository) {
        this.productRepository = productRepository;
        this.investorRepository = investorRepository;
    }

    public List<InvestmentProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public InvestmentProductDTO getProductById(Long id) {
        InvestmentProduct product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return mapToDTO(product);
    }

    public List<InvestmentProductDTO> getProductsByInvestorId(Long investorId) {
        investorRepository.findById(investorId)
                .orElseThrow(() -> new ResourceNotFoundException("Investor not found with id: " + investorId));
        return productRepository.findByInvestorId(investorId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public InvestmentProductDTO createProduct(InvestmentProductDTO dto) {
        Investor investor = investorRepository.findById(dto.getInvestorId())
                .orElseThrow(() -> new ResourceNotFoundException("Investor not found with id: " + dto.getInvestorId()));

        InvestmentProduct product = new InvestmentProduct();
        product.setProductName(dto.getProductName());
        product.setProductType(dto.getProductType());
        product.setBalance(dto.getBalance());
        product.setInvestor(investor);

        InvestmentProduct saved = productRepository.save(product);
        return mapToDTO(saved);
    }

    @Transactional
    public InvestmentProductDTO updateProduct(Long id, InvestmentProductDTO dto) {
        InvestmentProduct product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        product.setProductName(dto.getProductName());
        product.setProductType(dto.getProductType());
        product.setBalance(dto.getBalance());

        InvestmentProduct saved = productRepository.save(product);
        return mapToDTO(saved);
    }

    @Transactional
    public void deleteProduct(Long id) {
        InvestmentProduct product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        productRepository.delete(product);
    }

    private InvestmentProductDTO mapToDTO(InvestmentProduct product) {
        InvestmentProductDTO dto = new InvestmentProductDTO();
        dto.setId(product.getId());
        dto.setProductName(product.getProductName());
        dto.setProductType(product.getProductType());
        dto.setBalance(product.getBalance());
        dto.setInvestorId(product.getInvestor().getId());
        return dto;
    }
}