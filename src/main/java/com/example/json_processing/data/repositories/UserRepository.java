package com.example.json_processing.data.repositories;

import com.example.json_processing.data.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Set<User> findBySoldProductsIsNotNullOrderByLastNameAscFirstName();

    @Query("SELECT u FROM User AS u JOIN u.soldProducts AS s ORDER BY SIZE(s) DESC, u.lastName")
    Set<User> findBySoldProductsIsNotNullOrderBySoldProductsCountDescLastName();
}
