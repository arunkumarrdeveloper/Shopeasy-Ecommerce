package com.shopesy.controller;

import com.shopesy.dto.ProductRequest;
import com.shopesy.entity.Category;
import com.shopesy.entity.Product;
import com.shopesy.repository.CategoryRepository;
import com.shopesy.repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductRepository products;
    private final CategoryRepository categories;

    public ProductController(ProductRepository products, CategoryRepository categories) {
        this.products = products; this.categories = categories;
    }

    @GetMapping
    public List<Product> getAll(@RequestParam(required = false) String q,
                                @RequestParam(required = false) Long categoryId) {
        String query = (q == null || q.isBlank()) ? null : q.trim();
        return products.search(query, categoryId);
    }

    @GetMapping("/{id}")
    public Product getOne(@PathVariable Long id) {
        return products.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Product create(@Valid @RequestBody ProductRequest request) {
        Product product = new Product();
        apply(product, request);
        return products.save(product);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Product update(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
        Product product = products.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        apply(product, request);
        return products.save(product);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        if (!products.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        products.deleteById(id);
    }

    private void apply(Product product, ProductRequest request) {
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setStock(request.stock());
        product.setImageUrl(request.imageUrl());
        if (request.categoryId() == null) {
            product.setCategory(null);
        } else {
            Category category = categories.findById(request.categoryId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
            product.setCategory(category);
        }
    }
}
