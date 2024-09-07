package com.almousleck.service.impl;

import com.almousleck.exceptions.ResourceExistException;
import com.almousleck.exceptions.ResourceNotFound;
import com.almousleck.model.Category;
import com.almousleck.repository.CategoryRepository;
import com.almousleck.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Category not found."));
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category saveCategory(Category category) {
        return Optional.ofNullable(category)
                .filter(cat -> !categoryRepository.existsByName(cat.getName()))
                .map(categoryRepository::save)
                .orElseThrow(() ->
                     new ResourceExistException("The given category name [%s] already exist"
                            .formatted(category.getName())));
    }

    @Override
    public Category updateCategory(Category category, Long categoryId) {
        return Optional.ofNullable(getCategoryById(categoryId))
                .map(updated -> {
                    updated.setName(category.getName());
                    return categoryRepository.save(updated);
                })
                .orElseThrow(() -> new ResourceNotFound("Category not found."));
    }

    @Override
    public void deleteCategory(Long categoryId) {
        categoryRepository.findById(categoryId)
                .ifPresentOrElse(categoryRepository::delete, () -> {
                    throw new ResourceNotFound("Category not found.");
                });
    }
}
