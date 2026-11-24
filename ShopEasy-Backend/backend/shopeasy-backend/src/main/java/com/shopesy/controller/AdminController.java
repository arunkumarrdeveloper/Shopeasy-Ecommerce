package com.shopesy.controller;

import com.shopesy.entity.AppUser;
import com.shopesy.entity.Role;
import com.shopesy.repository.OrderRepository;
import com.shopesy.repository.ProductRepository;
import com.shopesy.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final UserRepository users;
    private final ProductRepository products;
    private final OrderRepository orders;

    public AdminController(UserRepository users, ProductRepository products, OrderRepository orders) {
        this.users = users; this.products = products; this.orders = orders;
    }

    @GetMapping("/users")
    public List<Map<String, Object>> customers() {
        return users.findAll().stream()
                .filter(u -> u.getRole() == Role.USER)
                .map(u -> Map.<String, Object>of("id", u.getId(), "name", u.getName(),
                        "email", u.getEmail(), "createdAt", u.getCreatedAt()))
                .collect(Collectors.toList());
    }

    @GetMapping("/dashboard")
    public Map<String, Long> dashboard() {
        long customerCount = users.findAll().stream().filter(u -> u.getRole() == Role.USER).count();
        return Map.of(
                "totalUsers", customerCount,
                "totalProducts", products.count(),
                "totalOrders", orders.count()
        );
    }
}
