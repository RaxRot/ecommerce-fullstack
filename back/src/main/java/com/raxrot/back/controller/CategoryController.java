package com.raxrot.back.controller;

import com.raxrot.back.dto.CategoryDTO;
import com.raxrot.back.service.CategoryService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/admin/category")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        log.info("POST /api/admin/category - Payload: {}", categoryDTO);
        CategoryDTO createdCategory = categoryService.createCategory(categoryDTO);
        log.info("Category created with id: {}", createdCategory.getId());
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @GetMapping("/public/categories")
    public ResponseEntity<List<CategoryDTO>> getCategories() {
        log.debug("GET /api/public/categories");
        List<CategoryDTO> categories = categoryService.getAllCategories();
        log.info("Returned {} categories", categories.size());
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/public/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable Long categoryId) {
        log.debug("GET /api/public/categories/{}", categoryId);
        CategoryDTO categoryDTO = categoryService.getCategoryById(categoryId);
        log.info("Returned category: id={}, name={}", categoryDTO.getId(), categoryDTO.getName());
        return ResponseEntity.ok(categoryDTO);
    }

    @PutMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long categoryId,
                                                      @Valid @RequestBody CategoryDTO categoryDTO) {
        log.info("PUT /api/admin/categories/{} - Payload: {}", categoryId, categoryDTO);
        CategoryDTO updatedCategory = categoryService.updateCategory(categoryId, categoryDTO);
        log.info("Category updated: id={}, new name={}", updatedCategory.getId(), updatedCategory.getName());
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {
        log.info("DELETE /api/admin/categories/{}", categoryId);
        categoryService.deleteCategory(categoryId);
        log.info("Category deleted: id={}", categoryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
