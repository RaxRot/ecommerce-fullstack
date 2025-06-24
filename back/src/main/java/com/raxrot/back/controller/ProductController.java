package com.raxrot.back.controller;

import com.raxrot.back.dto.ProductDTO;
import com.raxrot.back.dto.ProductResponse;
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

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts() {
        log.debug("GET /api/public/products called");
        ProductResponse productResponse = productService.getAllProducts();
        log.info("Returned {} products", productResponse.getContent().size());
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductsByCategory(@PathVariable Long categoryId) {
        log.debug("GET /api/public/categories/{}/products", categoryId);
        ProductResponse response = productService.searchByCategory(categoryId);
        log.info("Returned {} products for category {}", response.getContent().size(), categoryId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductsByKeyword(@PathVariable String keyword) {
        log.debug("GET /api/public/products/keyword/{}", keyword);
        ProductResponse productResponse = productService.searchProductByKeyword(keyword);
        log.info("Returned {} products matching keyword '{}'", productResponse.getContent().size(), keyword);
        return ResponseEntity.ok(productResponse);
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long productId,
            @Valid @RequestBody ProductDTO productDTO) {

        log.debug("PUT /api/products/{} called", productId);
        ProductDTO updatedProduct = productService.updateProduct(productId, productDTO);
        log.info("Product updated: id={}, name={}", updatedProduct.getId(), updatedProduct.getName());
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }
}
