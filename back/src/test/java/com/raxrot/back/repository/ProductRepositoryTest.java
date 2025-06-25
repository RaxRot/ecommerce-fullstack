package com.raxrot.back.repository;

import com.raxrot.back.model.Category;
import com.raxrot.back.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category savedCategory;

    @BeforeEach
    void setup() {
        Category category = new Category();
        category.setName("Test Category");
        savedCategory = categoryRepository.save(category);
    }

    @Test
    @DisplayName("Should save product")
    void saveProduct() {
        Product product = new Product();
        product.setName("Phone");
        product.setDescription("Smartphone with 6GB RAM");
        product.setImage("phone.jpg");
        product.setQuantity(10);
        product.setPrice(new BigDecimal("499.99"));
        product.setCategory(savedCategory);

        Product saved = productRepository.save(product);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Phone");
    }

    @Test
    @DisplayName("Should find product by ID")
    void findProductById() {
        Product product = new Product(null, "Book", "Novel", "book.jpg", 5, new BigDecimal("19.99"), savedCategory);
        Product saved = productRepository.save(product);

        Optional<Product> found = productRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Book");
    }

    @Test
    @DisplayName("Should return all products")
    void findAllProducts() {
        Product p1 = new Product(null, "Item1", "Desc1", "img1.jpg", 3, new BigDecimal("10.00"), savedCategory);
        Product p2 = new Product(null, "Item2", "Desc2", "img2.jpg", 7, new BigDecimal("20.00"), savedCategory);

        productRepository.save(p1);
        productRepository.save(p2);

        List<Product> products = productRepository.findAll();

        assertThat(products.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should update product name")
    void updateProduct() {
        Product product = new Product(null, "OldName", "Desc", "img.jpg", 1, new BigDecimal("15.00"), savedCategory);
        Product saved = productRepository.save(product);

        saved.setName("NewName");
        Product updated = productRepository.save(saved);

        assertThat(updated.getName()).isEqualTo("NewName");
    }

    @Test
    @DisplayName("Should delete product by ID")
    void deleteProductById() {
        Product product = new Product(null, "DeleteMe", "Trash", "img.jpg", 2, new BigDecimal("5.00"), savedCategory);
        Product saved = productRepository.save(product);

        productRepository.deleteById(saved.getId());

        Optional<Product> deleted = productRepository.findById(saved.getId());
        assertThat(deleted).isEmpty();
    }

    @Test
    @DisplayName("Should check existence by ID")
    void existsById() {
        Product product = new Product(null, "Exist", "desc", "img.jpg", 1, new BigDecimal("1.00"), savedCategory);
        Product saved = productRepository.save(product);

        boolean exists = productRepository.existsById(saved.getId());
        assertThat(exists).isTrue();
    }
}
