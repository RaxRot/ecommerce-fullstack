package com.raxrot.back.controller;

import com.raxrot.back.dto.CategoryDTO;
import com.raxrot.back.service.CategoryService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/api/admin/category")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        log.info("POST /api/admin/category - Payload: {}", categoryDTO);
        CategoryDTO createdCategory = categoryService.createCategory(categoryDTO);
        log.info("Category created with id: {}", createdCategory.getId());
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @GetMapping("/api/public/categories")
    public ResponseEntity<Page<CategoryDTO>> getCategories(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        log.debug("GET /api/public/categories with paging: {}", pageable);
        Page<CategoryDTO> categoriesPage = categoryService.getAllCategories(pageable);
        log.info("Returned {} categories on page {}", categoriesPage.getNumberOfElements(), categoriesPage.getNumber());
        return ResponseEntity.ok(categoriesPage);
    }

    @GetMapping("/api/public/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable Long categoryId) {
        log.debug("GET /api/public/categories/{}", categoryId);
        CategoryDTO categoryDTO = categoryService.getCategoryById(categoryId);
        log.info("Returned category: id={}, name={}", categoryDTO.getId(), categoryDTO.getName());
        return ResponseEntity.ok(categoryDTO);
    }

    @PutMapping("/api/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long categoryId,
                                                      @Valid @RequestBody CategoryDTO categoryDTO) {
        log.info("PUT /api/admin/categories/{} - Payload: {}", categoryId, categoryDTO);
        CategoryDTO updatedCategory = categoryService.updateCategory(categoryId, categoryDTO);
        log.info("Category updated: id={}, new name={}", updatedCategory.getId(), updatedCategory.getName());
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/api/admin/categories/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {
        log.info("DELETE /api/admin/categories/{}", categoryId);
        categoryService.deleteCategory(categoryId);
        log.info("Category deleted: id={}", categoryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
