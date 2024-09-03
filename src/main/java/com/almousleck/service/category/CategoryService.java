package com.almousleck.service.category;

import com.almousleck.model.Category;

import java.util.List;

public interface CategoryService {
    Category getCategoryById(Long id);
    Category getCategoryByName(String name);
    List<Category> getAllCategories();
    Category saveCategory(Category category);
    Category updateCategory(Category category, Long categoryId);
    void deleteCategory(Long categoryId);
}
