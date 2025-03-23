package com.example.json_processing.services.dtos.export;

import com.google.gson.annotations.Expose;

public class FullUserDTO {
    @Expose
    private String firstName;

    @Expose
    private String lastName;

    @Expose
    private int age;

    @Expose
    private SoldProductsDTO soldProducts;

    public FullUserDTO() {}

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public SoldProductsDTO getSoldProducts() {
        return soldProducts;
    }

    public void setSoldProducts(SoldProductsDTO soldProducts) {
        this.soldProducts = soldProducts;
    }
}
