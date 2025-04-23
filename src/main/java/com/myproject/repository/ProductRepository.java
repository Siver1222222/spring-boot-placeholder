package com.socialhub.repository;

import com.socialhub.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Simple query methods
    List<Product> findByName(String name);
    List<Product> findByPriceGreaterThan(double price);

    // Add your custom query methods here
}