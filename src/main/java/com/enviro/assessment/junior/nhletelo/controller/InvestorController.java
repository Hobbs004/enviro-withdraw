package com.enviro.assessment.junior.nhletelo.controller;

import com.enviro.assessment.junior.nhletelo.dto.*;
import com.enviro.assessment.junior.nhletelo.service.InvestorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * REST endpoints for investor and portfolio data.
 * GET    /api/investors               - all investors
 * GET    /api/investors/{id}          - single investor with portfolio
 * GET    /api/investors/{id}/products - investor's investment products
 * POST   /api/investors               - create investor
 * PUT    /api/investors/{id}          - update investor
 * DELETE /api/investors/{id}          - delete investor*/
@RestController
@RequestMapping("/api/investors")
public class InvestorController {

    private final InvestorService investorService;

    public InvestorController(InvestorService investorService) {
        this.investorService = investorService;
    }

     @PostMapping
    public ResponseEntity<ApiResponseDTO<InvestorDTO>> createInvestor(@RequestBody InvestorDTO investorDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.ok("Investor created", investorService.createInvestor(investorDTO)));
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<InvestorDTO>>> getAllInvestors() {
        return ResponseEntity.ok(ApiResponseDTO.ok("Investors retrieved", investorService.getAllInvestors()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<InvestorDTO>> getInvestorById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponseDTO.ok("Investor retrieved", investorService.getInvestorById(id)));
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<ApiResponseDTO<List<InvestmentProductDTO>>> getProducts(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponseDTO.ok("Products retrieved", investorService.getProductsByInvestorId(id)));
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<InvestorDTO>> updateInvestor(@PathVariable Long id,
                                                                       @RequestBody InvestorDTO investorDTO) {
        return ResponseEntity.ok(ApiResponseDTO.ok("Investor updated", investorService.updateInvestor(id, investorDTO)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteInvestor(@PathVariable Long id) {
        investorService.deleteInvestor(id);
        return ResponseEntity.ok(ApiResponseDTO.ok("Investor deleted", null));
    }
}