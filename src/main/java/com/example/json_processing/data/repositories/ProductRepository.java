package com.example.json_processing.data.repositories;

import com.example.json_processing.data.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Set<Product> findByPriceBetweenAndBuyerIsNullOrderByPrice(BigDecimal lowerBound, BigDecimal upperBound);
}
