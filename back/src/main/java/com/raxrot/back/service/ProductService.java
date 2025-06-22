package com.raxrot.back.service;

import com.raxrot.back.dto.ProductDTO;

public interface ProductService {
    ProductDTO createProduct(Long categoryId,ProductDTO productDTO);
}
