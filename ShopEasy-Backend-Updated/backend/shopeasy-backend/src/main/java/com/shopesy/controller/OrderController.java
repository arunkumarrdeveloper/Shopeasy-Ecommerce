package com.shopesy.controller;

import com.shopesy.dto.StatusRequest;
import com.shopesy.entity.PurchaseOrder;
import com.shopesy.repository.OrderRepository;
import com.shopesy.service.CurrentUserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderRepository orders;
    private final CurrentUserService currentUser;

    public OrderController(OrderRepository orders, CurrentUserService currentUser) {
        this.orders = orders; this.currentUser = currentUser;
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('USER')")
    public List<PurchaseOrder> myOrders() {
        return orders.findByUserIdOrderByCreatedAtDesc(currentUser.get().getId());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<PurchaseOrder> allOrders() {
        return orders.findAllByOrderByCreatedAtDesc();
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public PurchaseOrder updateStatus(@PathVariable Long id, @Valid @RequestBody StatusRequest request) {
        PurchaseOrder order = orders.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
        order.setStatus(request.status());
        return orders.save(order);
    }
}
