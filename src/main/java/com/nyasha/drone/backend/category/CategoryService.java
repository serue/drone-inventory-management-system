package com.nyasha.drone.backend.category;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category getCategory(int id) {
        return categoryRepository.findById(id)
                .orElseThrow(()-> new RuntimeException(String.format("Category with id %s not found", id)));
    }

    public void save(@Valid CategoryRequest request) {
        Category category = Category.builder()
                .categoryType(request.getCategoryType())
                .name(request.getName())
                .description(request.getDescription())
                .build();
        categoryRepository.save(category);
    }

    public void delete(int id) {
        categoryRepository.deleteById(id);
    }

    public CategoryRequest getCategoryRequest(int id) {
        Category category = getCategory(id);
        CategoryRequest categoryRequest = new CategoryRequest();
        categoryRequest.setId(category.getId());
        categoryRequest.setCategoryType(category.getCategoryType());
        categoryRequest.setName(category.getName());
        categoryRequest.setDescription(category.getDescription());
        return categoryRequest;
    }
    public void update(@Valid CategoryRequest request, int id) {
        Category category = getCategory(id);
        category.setCategoryType(request.getCategoryType());
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        categoryRepository.save(category);
    }
}
