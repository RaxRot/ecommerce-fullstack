package com.raxrot.back.service.impl;

import com.raxrot.back.dto.ProductDTO;
import com.raxrot.back.exception.ApiException;
import com.raxrot.back.model.Category;
import com.raxrot.back.model.Product;
import com.raxrot.back.repository.CategoryRepository;
import com.raxrot.back.repository.ProductRepository;
import com.raxrot.back.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {
    private static final String PRODUCT_NOT_FOUND = "Product not found with id: ";

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository,ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ProductDTO createProduct(Long categoryId, ProductDTO productDTO) {
        log.debug("Creating product in category with id: {}", categoryId);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    log.warn("Category not found with id: {}", categoryId);
                    return new ApiException(PRODUCT_NOT_FOUND + categoryId);
                });

        Product product = modelMapper.map(productDTO, Product.class);
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);
        log.info("Product created: id={}, name={}", savedProduct.getId(), savedProduct.getName());

        return modelMapper.map(savedProduct, ProductDTO.class);
    }
}
