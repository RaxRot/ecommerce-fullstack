package com.raxrot.back.service.impl;

import com.raxrot.back.dto.ProductDTO;
import com.raxrot.back.dto.ProductResponse;
import com.raxrot.back.exception.ApiException;
import com.raxrot.back.model.Category;
import com.raxrot.back.model.Product;
import com.raxrot.back.repository.CategoryRepository;
import com.raxrot.back.repository.ProductRepository;
import com.raxrot.back.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public ProductResponse getAllProducts() {
        log.debug("Fetching all products");

        List<Product> products = productRepository.findAll();
        log.info("Found {} products", products.size());

        List<ProductDTO> productDTOs = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOs);

        log.debug("Returning product response with {} items", productDTOs.size());
        return productResponse;
    }

    @Override
    public ProductResponse searchByCategory(Long categoryId) {
        log.debug("Searching products by category id: {}", categoryId);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    log.warn("Category not found with id: {}", categoryId);
                    return new ApiException(PRODUCT_NOT_FOUND + categoryId);
                });

        List<Product> products = productRepository.findByCategoryOrderByPrice(category);
        log.info("Found {} products for category id: {}", products.size(), categoryId);

        List<ProductDTO> productDTOs = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOs);

        log.debug("Mapped {} products to DTOs", productDTOs.size());

        return productResponse;
    }

    @Override
    public ProductResponse searchProductByKeyword(String keyword) {
        log.debug("Searching products by keyword: {}", keyword);

        List<Product> products = productRepository.findByNameContainingIgnoreCase(keyword);
        log.info("Found {} products matching keyword '{}'", products.size(), keyword);

        List<ProductDTO> productDTOs = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOs);

        return productResponse;
    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        log.debug("Updating product with id: {}", productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ApiException(PRODUCT_NOT_FOUND + productId));

        log.info("Found product: {}", product.getName());

        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setImage(productDTO.getImage());
        product.setQuantity(productDTO.getQuantity());
        product.setPrice(productDTO.getPrice());

        Product savedProduct = productRepository.save(product);

        log.info("Updated product: {}", savedProduct.getName());

        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public void deleteProduct(Long productId) {
        log.debug("Attempting to delete product with id: {}", productId);

        if (!productRepository.existsById(productId)) {
            log.warn("Product not found with id: {}", productId);
            throw new ApiException(PRODUCT_NOT_FOUND + productId);
        }

        productRepository.deleteById(productId);
        log.info("Deleted product with id: {}", productId);
    }
}
