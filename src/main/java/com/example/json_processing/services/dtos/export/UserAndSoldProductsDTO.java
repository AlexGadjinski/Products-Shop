package com.example.json_processing.services.dtos.export;

import com.google.gson.annotations.Expose;

import java.util.HashSet;
import java.util.Set;

public class UserAndSoldProductsDTO {
    @Expose
    private int usersCount;

    @Expose
    private Set<FullUserDTO> users;

    public UserAndSoldProductsDTO() {
        this.users = new HashSet<>();
    }

    public int getUsersCount() {
        return usersCount;
    }

    public void setUsersCount(int usersCount) {
        this.usersCount = usersCount;
    }

    public Set<FullUserDTO> getUsers() {
        return users;
    }

    public void setUsers(Set<FullUserDTO> users) {
        this.users = users;
    }
}
