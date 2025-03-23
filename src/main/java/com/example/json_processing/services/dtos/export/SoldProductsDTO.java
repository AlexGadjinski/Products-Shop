package com.example.json_processing.services.dtos.export;

import com.google.gson.annotations.Expose;

import java.util.HashSet;
import java.util.Set;

public class SoldProductsDTO {
    @Expose
    private int count;

    @Expose
    private Set<ProductWithNameAndPriceDTO> products;

    public SoldProductsDTO() {
        this.products = new HashSet<>();
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Set<ProductWithNameAndPriceDTO> getProducts() {
        return products;
    }

    public void setProducts(Set<ProductWithNameAndPriceDTO> products) {
        this.products = products;
    }
}
