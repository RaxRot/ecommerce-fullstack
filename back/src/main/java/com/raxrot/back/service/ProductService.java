package com.raxrot.back.service;

import com.raxrot.back.dto.ProductDTO;
import com.raxrot.back.dto.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ProductDTO createProduct(Long categoryId,ProductDTO productDTO);
    ProductResponse getAllProducts();
    ProductResponse searchByCategory(Long categoryId);
    ProductResponse searchProductByKeyword(String keyword);
    ProductDTO updateProduct(Long productId,ProductDTO productDTO);
    void deleteProduct(Long productId);
    ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;
}
