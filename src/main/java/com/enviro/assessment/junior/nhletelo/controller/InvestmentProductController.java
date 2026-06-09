package com.enviro.assessment.junior.nhletelo.controller;

import com.enviro.assessment.junior.nhletelo.dto.ApiResponseDTO;
import com.enviro.assessment.junior.nhletelo.dto.InvestmentProductDTO;
import com.enviro.assessment.junior.nhletelo.service.InvestmentProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * REST endpoints for investment products.
 * GET    /api/products              - all products
 * GET    /api/products/{id}         - single product
 * POST   /api/products              - create product
 * PUT    /api/products/{id}         - update product
 * DELETE /api/products/{id}         - delete product
 */
@RestController
@RequestMapping("/api/products")
public class InvestmentProductController {

    private final InvestmentProductService productService;

    public InvestmentProductController(InvestmentProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<InvestmentProductDTO>>> getAllProducts() {
        return ResponseEntity.ok(ApiResponseDTO.ok("Products retrieved", productService.getAllProducts()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<InvestmentProductDTO>> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponseDTO.ok("Product retrieved", productService.getProductById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<InvestmentProductDTO>> createProduct(
            @RequestBody InvestmentProductDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.ok("Product created", productService.createProduct(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<InvestmentProductDTO>> updateProduct(
            @PathVariable Long id, @RequestBody InvestmentProductDTO dto) {
        return ResponseEntity.ok(ApiResponseDTO.ok("Product updated", productService.updateProduct(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponseDTO.ok("Product deleted", null));
    }
}