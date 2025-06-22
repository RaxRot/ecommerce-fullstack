package com.raxrot.back.controller;

import com.raxrot.back.dto.ProductDTO;
import com.raxrot.back.service.ProductService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> createProduct(@PathVariable Long categoryId, @Valid @RequestBody ProductDTO productDTO) {
        log.debug("POST /api/admin/categories/{}/product", categoryId);
        ProductDTO createdProduct = productService.createProduct(categoryId, productDTO);
        log.info("Product created: name={}, categoryId={}", createdProduct.getName(), categoryId);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }
}
