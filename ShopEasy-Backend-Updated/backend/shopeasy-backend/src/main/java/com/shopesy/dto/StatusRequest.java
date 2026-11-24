package com.shopesy.dto;

import com.shopesy.entity.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record StatusRequest(@NotNull OrderStatus status) {}
