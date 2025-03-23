package com.example.json_processing.controller;

import com.example.json_processing.services.CategoryService;
import com.example.json_processing.services.ProductService;
import com.example.json_processing.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;

@Component
public class Runner implements CommandLineRunner {
    private final CategoryService categoryService;
    private final ProductService productService;
    private final UserService userService;

    public Runner(CategoryService categoryService, ProductService productService, UserService userService) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        seedData();
//        productService.exportProductsInRange(BigDecimal.valueOf(500), BigDecimal.valueOf(1000));
//        userService.exportUsersWithSoldProducts();
//        categoryService.exportCategoriesWithProducts();
//        userService.exportUsersAndProducts();
    }

    private void seedData() throws IOException {
        if (!categoryService.isImported()) {
            categoryService.seedCategories();
        }
        if (!userService.isImported()) {
            userService.seedUsers();
        }
        if (!productService.isImported()) {
            productService.seedProducts();
        }
    }
}
