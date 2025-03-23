package com.example.json_processing.services;

import java.io.IOException;
import java.math.BigDecimal;

public interface ProductService {
    boolean isImported();
    void seedProducts() throws IOException;
    void exportProductsInRange(BigDecimal from, BigDecimal to) throws IOException;
}
