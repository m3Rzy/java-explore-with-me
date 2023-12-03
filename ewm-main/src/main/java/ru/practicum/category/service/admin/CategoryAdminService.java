package ru.practicum.category.service.admin;

import ru.practicum.category.dto.CategoryDto;

public interface CategoryAdminService {

    CategoryDto createCategory(CategoryDto categoryDto);

    void deleteCategory(Long catId);

    CategoryDto updateCategory(Long catId, CategoryDto categoryDto);
}
