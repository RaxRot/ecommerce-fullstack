package com.raxrot.back.repository;

import com.raxrot.back.model.Category;
import com.raxrot.back.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryOrderByPrice(Category category);
}
