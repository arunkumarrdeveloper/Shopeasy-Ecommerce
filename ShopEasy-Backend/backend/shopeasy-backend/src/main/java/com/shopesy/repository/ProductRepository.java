package com.shopesy.repository;

import com.shopesy.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("select p from Product p where (:q is null or lower(p.name) like lower(concat('%', :q, '%'))) and (:categoryId is null or p.category.id = :categoryId)")
    List<Product> search(@Param("q") String q, @Param("categoryId") Long categoryId);
}
