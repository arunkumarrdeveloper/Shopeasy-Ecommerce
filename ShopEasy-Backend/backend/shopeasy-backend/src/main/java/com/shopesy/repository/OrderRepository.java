package com.shopesy.repository;

import com.shopesy.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<PurchaseOrder, Long> {
    List<PurchaseOrder> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<PurchaseOrder> findAllByOrderByCreatedAtDesc();
}
