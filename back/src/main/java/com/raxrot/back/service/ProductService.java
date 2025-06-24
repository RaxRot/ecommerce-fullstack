package com.raxrot.back.service;

import com.raxrot.back.dto.ProductDTO;
import com.raxrot.back.dto.ProductResponse;

public interface ProductService {
    ProductDTO createProduct(Long categoryId,ProductDTO productDTO);
    ProductResponse getAllProducts();
    ProductResponse searchByCategory(Long categoryId);
}
