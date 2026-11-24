package com.shopesy.dto;

import jakarta.validation.constraints.NotBlank;

public record CheckoutRequest(@NotBlank String shippingAddress) {}
