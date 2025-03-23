package com.example.json_processing.data.repositories;

import com.example.json_processing.data.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT c FROM Category AS c JOIN c.products AS p ORDER BY SIZE(p) DESC")
    Set<Category> findByOrderByProductsCountDesc();
}
