package com.shopesy.controller;

import com.shopesy.dto.CategoryRequest;
import com.shopesy.entity.Category;
import com.shopesy.repository.CategoryRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryRepository categories;
    public CategoryController(CategoryRepository categories) { this.categories = categories; }

    @GetMapping
    public List<Category> getAll() { return categories.findAll(); }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Category create(@Valid @RequestBody CategoryRequest request) {
        if (categories.existsByNameIgnoreCase(request.name()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Category already exists");
        return categories.save(new Category(request.name(), request.description()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Category update(@PathVariable Long id, @Valid @RequestBody CategoryRequest request) {
        Category category = categories.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        category.setName(request.name());
        category.setDescription(request.description());
        return categories.save(category);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        if (!categories.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        categories.deleteById(id);
    }
}
