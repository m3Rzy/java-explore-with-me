package ru.practicum.category.service.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.UniqueException;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CategoryAdminServiceImpl implements CategoryAdminService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CategoryDto createCategory(CategoryDto requestCategory) {
        if (categoryRepository.existsByName(requestCategory.getName())) {
            throw new UniqueException("Категория с таким названием уже существует!");
        }
        try {
            Category result = categoryRepository.saveAndFlush(CategoryMapper.toCategories(requestCategory));
            return CategoryMapper.toCategoryDto(result);

        } catch (DataIntegrityViolationException e) {
            log.error("Категория уже существует : {}", requestCategory);
            throw new ConflictException("Категория с названием " + requestCategory.getName() + " уже существует!");
        }
    }

    @Override
    @Transactional
    public void deleteCategory(Long catId) {

        if (!categoryRepository.existsById(catId)) {
            throw new NotFoundException("Категории с id " + catId + " не существует!");
        }

        if (eventRepository.existsByCategoryId(catId)) {
            throw new ConflictException("Невозможно удалить категорию с событиями!");
        }

        if (!categoryRepository.existsById(catId)) {
            throw new NotFoundException("Категории с id " + catId + " не существует!");
        }
        categoryRepository.deleteById(catId);
    }

    @Transactional
    @Override
    public CategoryDto updateCategory(Long catId, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(catId).orElseThrow(()
                -> new NotFoundException("Категории с id " + catId + " не существует!"));

        if (!category.getName().equals(categoryDto.getName()) &&
                categoryRepository.existsByName(categoryDto.getName())) {
            throw new UniqueException("Такое название категории уже существует! " + categoryDto.getName());
        }
        category.setName(categoryDto.getName());
        return CategoryMapper.toCategoryDto(category);
    }
}
