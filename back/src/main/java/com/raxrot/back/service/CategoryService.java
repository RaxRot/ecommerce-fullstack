package com.raxrot.back.service;

import com.raxrot.back.dto.CategoryDTO;
import com.raxrot.back.dto.CategoryResponse;

public interface CategoryService {
    CategoryDTO createCategory(CategoryDTO categoryDTO);
    CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    CategoryDTO getCategoryById(Long id);
    CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO);
    void deleteCategory(Long id);
}
