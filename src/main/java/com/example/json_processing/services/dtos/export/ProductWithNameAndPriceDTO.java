package com.example.json_processing.services.dtos.export;

import com.google.gson.annotations.Expose;

import java.math.BigDecimal;

public class ProductWithNameAndPriceDTO {
    @Expose
    private String name;

    @Expose
    private BigDecimal price;

    public ProductWithNameAndPriceDTO() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
