package com.shopesy.controller;

import com.shopesy.dto.CartRequest;
import com.shopesy.dto.CheckoutRequest;
import com.shopesy.entity.CartItem;
import com.shopesy.entity.PurchaseOrder;
import com.shopesy.service.CartService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@PreAuthorize("hasRole('USER')")
public class CartController {
    private final CartService cartService;
    public CartController(CartService cartService) { this.cartService = cartService; }

    @GetMapping
    public List<CartItem> getCart() { return cartService.getCart(); }

    @PostMapping("/items")
    public CartItem add(@Valid @RequestBody CartRequest request) { return cartService.add(request); }

    @DeleteMapping("/items/{id}")
    public void remove(@PathVariable Long id) { cartService.remove(id); }

    @PostMapping("/checkout")
    public PurchaseOrder checkout(@Valid @RequestBody CheckoutRequest request) {
        return cartService.checkout(request);
    }
}
