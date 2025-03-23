package com.example.json_processing.services.impls;

import com.example.json_processing.data.entities.User;
import com.example.json_processing.data.repositories.UserRepository;
import com.example.json_processing.services.UserService;
import com.example.json_processing.services.dtos.UserSeedDTO;
import com.example.json_processing.services.dtos.export.*;
import com.example.json_processing.utils.ValidatorUtil;
import com.google.gson.Gson;
import jakarta.validation.ConstraintViolation;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private static final String JSON_SEED_PATH = "src/main/resources/json/users.json";
    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final ValidatorUtil validatorUtil;
    private final Gson gson;

    public UserServiceImpl(UserRepository userRepository, ModelMapper mapper, ValidatorUtil validatorUtil, Gson gson) {
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.validatorUtil = validatorUtil;
        this.gson = gson;
    }

    @Override
    public boolean isImported() {
        return userRepository.count() > 0;
    }

    @Override
    public void seedUsers() throws IOException {
        String jsonString = Files.readAllLines(Path.of(JSON_SEED_PATH)).stream()
                .collect(Collectors.joining(System.lineSeparator()));

        UserSeedDTO[] userSeedDTOs = gson.fromJson(jsonString, UserSeedDTO[].class);

        for (UserSeedDTO userSeedDTO : userSeedDTOs) {
            if (!validatorUtil.isValid(userSeedDTO)) {
                String output = validatorUtil.validate(userSeedDTO).stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.joining(System.lineSeparator()));
                System.out.println(output);
            } else {
                User user = mapper.map(userSeedDTO, User.class);
                userRepository.save(user);
            }
        }
    }

    @Override
    public void exportUsersWithSoldProducts() throws IOException {
        Set<User> users = userRepository.findBySoldProductsIsNotNullOrderByLastNameAscFirstName();
        List<UserWithSoldProductsDTO> userDTOs = users.stream()
                .map(u -> {
                    Set<ProductWithBuyerDTO> productDTOs = u.getSoldProducts().stream()
                            .map(p -> mapper.map(p, ProductWithBuyerDTO.class))
                            .collect(Collectors.toSet());
                    UserWithSoldProductsDTO userDTO = mapper.map(u, UserWithSoldProductsDTO.class);
                    userDTO.setSoldProducts(productDTOs);
                    return userDTO;
                }).toList();

        String jsonString = gson.toJson(userDTOs);
        FileWriter fileWriter = new FileWriter("src/main/resources/json/exports/users-sold-products.json");
        fileWriter.write(jsonString);
        fileWriter.close();
    }

    @Override
    public void exportUsersAndProducts() throws IOException {
        Set<User> users = userRepository.findBySoldProductsIsNotNullOrderBySoldProductsCountDescLastName();

        UserAndSoldProductsDTO userAndSoldProductsDTO = new UserAndSoldProductsDTO();
        userAndSoldProductsDTO.setUsersCount(users.size());
        userAndSoldProductsDTO.setUsers(
                users.stream()
                        .map(u -> {
                            FullUserDTO fullUserDTO = mapper.map(u, FullUserDTO.class);

                            SoldProductsDTO soldProductsDTO = new SoldProductsDTO();
                            soldProductsDTO.setCount(u.getSoldProducts().size());
                            soldProductsDTO.setProducts(u.getSoldProducts().stream()
                                    .map(s -> mapper.map(s, ProductWithNameAndPriceDTO.class))
                                    .collect(Collectors.toSet()));

                            fullUserDTO.setSoldProducts(soldProductsDTO);
                            return fullUserDTO;
                        }).collect(Collectors.toSet())
        );

        String jsonString = gson.toJson(userAndSoldProductsDTO);
        FileWriter fileWriter = new FileWriter("src/main/resources/json/exports/users-and-products.json");
        fileWriter.write(jsonString);
        fileWriter.close();
    }
}
