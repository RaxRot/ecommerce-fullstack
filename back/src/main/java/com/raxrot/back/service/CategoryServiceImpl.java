package com.raxrot.back.service;

import com.raxrot.back.dto.CategoryDTO;
import com.raxrot.back.dto.CategoryResponse;
import com.raxrot.back.exception.ApiException;
import com.raxrot.back.model.Category;
import com.raxrot.back.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {
    private static final String CATEGORY_NOT_FOUND = "Category not found with id: ";

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        log.info("Creating category with name: {}", categoryDTO.getName());
        Category category = modelMapper.map(categoryDTO, Category.class);
        Category savedCategory = categoryRepository.save(category);
        log.info("Category saved with id: {}", savedCategory.getId());
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        log.debug("Fetching categories with pageNumber={}, pageSize={}, sortBy={}, sortOrder={}",
                pageNumber, pageSize, sortBy, sortOrder);

        Sort sort = sortOrder.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sort);

        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);

        List<CategoryDTO> categoryDTOS = categoryPage.getContent()
                .stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();

        log.info("Found {} categories on page {} of {}",
                categoryDTOS.size(), categoryPage.getNumber() + 1, categoryPage.getTotalPages());

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());

        return categoryResponse;
    }



    @Override
    public CategoryDTO getCategoryById(Long id) {
        log.debug("Fetching category with id: {}", id);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Category not found with id: {}", id);
                    return new ApiException(CATEGORY_NOT_FOUND + id);
                });
        log.info("Category found: id={}, name={}", category.getId(), category.getName());
        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        log.info("Updating category with id: {}", id);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Category not found for update, id: {}", id);
                    return new ApiException(CATEGORY_NOT_FOUND + id);
                });
        category.setName(categoryDTO.getName());
        Category savedCategory = categoryRepository.save(category);
        log.info("Category updated: id={}, new name={}", savedCategory.getId(), savedCategory.getName());
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

    @Override
    public void deleteCategory(Long id) {
        log.info("Deleting category with id: {}", id);
        categoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Category not found for deletion, id: {}", id);
                    return new ApiException(CATEGORY_NOT_FOUND + id);
                });
        categoryRepository.deleteById(id);
        log.info("Category deleted: id={}", id);
    }
}