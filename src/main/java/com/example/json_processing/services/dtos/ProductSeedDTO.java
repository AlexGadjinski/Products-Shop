package com.example.json_processing.services.dtos;

import com.google.gson.annotations.Expose;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

public class ProductSeedDTO {
    @Expose
    @Length(min = 3, message = "Name should be at least 3 characters!")
    private String name;

    @Expose
    private BigDecimal price;

    public ProductSeedDTO() {}

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
