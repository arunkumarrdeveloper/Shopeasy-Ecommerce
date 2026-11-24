package com.shopesy.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id")
    private PurchaseOrder order;
    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id")
    private Product product;
    @Column(nullable = false)
    private Integer quantity;
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;

    public OrderItem() {}
    public OrderItem(Product product, Integer quantity, BigDecimal unitPrice) {
        this.product = product; this.quantity = quantity; this.unitPrice = unitPrice;
    }
    public void setOrder(PurchaseOrder order) { this.order = order; }
    public Long getId() { return id; }
    public Product getProduct() { return product; }
    public Integer getQuantity() { return quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
}
