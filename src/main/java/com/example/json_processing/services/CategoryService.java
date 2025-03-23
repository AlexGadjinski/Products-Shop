package com.example.json_processing.services;

import java.io.IOException;

public interface CategoryService {
    boolean isImported();
    void seedCategories() throws IOException;
    void exportCategoriesWithProducts() throws IOException;
}
