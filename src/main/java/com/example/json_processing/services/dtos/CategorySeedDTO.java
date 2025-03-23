package com.example.json_processing.services.dtos;

import com.google.gson.annotations.Expose;
import org.hibernate.validator.constraints.Length;

public class CategorySeedDTO {
    @Length(min = 3, max = 15, message = "Name should be between 3 and 15 characters!")
    @Expose
    private String name;

    public CategorySeedDTO() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
