package com.raxrot.back.repository;

import com.raxrot.back.model.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("Should save category")
    void saveCategory() {
        Category category = new Category();
        category.setName("Books");

        Category saved = categoryRepository.save(category);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Books");
    }

    @Test
    @DisplayName("Should find category by ID")
    void findCategoryById() {
        Category category = new Category();
        category.setName("Movies");
        Category saved = categoryRepository.save(category);

        Optional<Category> found = categoryRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Movies");
    }

    @Test
    @DisplayName("Should return all categories")
    void findAllCategories() {
        Category cat1 = new Category();
        cat1.setName("Food");

        Category cat2 = new Category();
        cat2.setName("Travel");

        categoryRepository.save(cat1);
        categoryRepository.save(cat2);

        List<Category> categories = categoryRepository.findAll();

        assertThat(categories.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should update category name")
    void updateCategory() {
        Category category = new Category();
        category.setName("Old Name");
        Category saved = categoryRepository.save(category);

        saved.setName("New Name");
        Category updated = categoryRepository.save(saved);

        assertThat(updated.getName()).isEqualTo("New Name");
    }

    @Test
    @DisplayName("Should delete category by ID")
    void deleteCategoryById() {
        Category category = new Category();
        category.setName("ToDelete");
        Category saved = categoryRepository.save(category);

        categoryRepository.deleteById(saved.getId());

        Optional<Category> deleted = categoryRepository.findById(saved.getId());
        assertThat(deleted).isEmpty();
    }

    @Test
    @DisplayName("Should check existence by ID")
    void existsByIdTest() {
        Category category = new Category();
        category.setName("ExistenceCheck");
        Category saved = categoryRepository.save(category);

        boolean exists = categoryRepository.existsById(saved.getId());
        assertThat(exists).isTrue();
    }
}