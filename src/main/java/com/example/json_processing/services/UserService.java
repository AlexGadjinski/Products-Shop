package com.example.json_processing.services;

import java.io.IOException;

public interface UserService {
    boolean isImported();
    void seedUsers() throws IOException;
    void exportUsersWithSoldProducts() throws IOException;
    void exportUsersAndProducts() throws IOException;
}
