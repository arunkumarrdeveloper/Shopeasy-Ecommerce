package com.shopesy.service;

import com.shopesy.dto.CartRequest;
import com.shopesy.dto.CheckoutRequest;
import com.shopesy.entity.*;
import com.shopesy.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartService {
    private final CartItemRepository cartItems;
    private final ProductRepository products;
    private final OrderRepository orders;
    private final CurrentUserService currentUser;

    public CartService(CartItemRepository cartItems, ProductRepository products,
                       OrderRepository orders, CurrentUserService currentUser) {
        this.cartItems = cartItems; this.products = products;
        this.orders = orders; this.currentUser = currentUser;
    }

    public List<CartItem> getCart() {
        return cartItems.findByUserId(currentUser.get().getId());
    }

    public CartItem add(CartRequest request) {
        AppUser user = currentUser.get();
        Product product = products.findById(request.productId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        if (product.getStock() < request.quantity()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient stock");
        }
        CartItem item = cartItems.findByUserIdAndProductId(user.getId(), product.getId())
                .orElse(new CartItem(user, product, 0));
        int newQuantity = item.getQuantity() + request.quantity();
        if (newQuantity > product.getStock()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient stock");
        }
        item.setQuantity(newQuantity);
        return cartItems.save(item);
    }

    public void remove(Long itemId) {
        CartItem item = cartItems.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart item not found"));
        if (!item.getUser().getId().equals(currentUser.get().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not your cart item");
        }
        cartItems.delete(item);
    }

    @Transactional
    public PurchaseOrder checkout(CheckoutRequest request) {
        AppUser user = currentUser.get();
        List<CartItem> items = cartItems.findByUserId(user.getId());
        if (items.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cart is empty");

        PurchaseOrder order = new PurchaseOrder();
        order.setUser(user);
        order.setShippingAddress(request.shippingAddress());
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem cartItem : items) {
            Product product = cartItem.getProduct();
            if (product.getStock() < cartItem.getQuantity()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Insufficient stock for " + product.getName());
            }
            BigDecimal lineTotal = product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            total = total.add(lineTotal);
            product.setStock(product.getStock() - cartItem.getQuantity());
            order.addItem(new OrderItem(product, cartItem.getQuantity(), product.getPrice()));
        }
        order.setTotalAmount(total);
        PurchaseOrder saved = orders.save(order);
        cartItems.deleteByUserId(user.getId());
        return saved;
    }
}
