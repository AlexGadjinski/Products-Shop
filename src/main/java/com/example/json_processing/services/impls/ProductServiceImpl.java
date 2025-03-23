package com.example.json_processing.services.impls;

import com.example.json_processing.data.entities.Category;
import com.example.json_processing.data.entities.Product;
import com.example.json_processing.data.entities.User;
import com.example.json_processing.data.repositories.CategoryRepository;
import com.example.json_processing.data.repositories.ProductRepository;
import com.example.json_processing.data.repositories.UserRepository;
import com.example.json_processing.services.ProductService;
import com.example.json_processing.services.dtos.ProductSeedDTO;
import com.example.json_processing.services.dtos.export.ProductWithNoBuyerDTO;
import com.example.json_processing.utils.ValidatorUtil;
import com.google.gson.Gson;
import jakarta.validation.ConstraintViolation;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private static final String JSON_SEED_PATH = "src/main/resources/json/products.json";
    private static final String JSON_EXPORT_PATH = "src/main/resources/json/exports/products-in-range.json";
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final ValidatorUtil validatorUtil;
    private final Gson gson;

    public ProductServiceImpl(CategoryRepository categoryRepository, ProductRepository productRepository,
                              UserRepository userRepository, ModelMapper mapper, ValidatorUtil validatorUtil, Gson gson) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.validatorUtil = validatorUtil;
        this.gson = gson;
    }

    @Override
    public boolean isImported() {
        return productRepository.count() > 0;
    }

    @Override
    public void seedProducts() throws IOException {
        String jsonString = Files.readAllLines(Path.of(JSON_SEED_PATH)).stream()
                .collect(Collectors.joining(System.lineSeparator()));

        ProductSeedDTO[] productSeedDTOs = gson.fromJson(jsonString, ProductSeedDTO[].class);

        for (ProductSeedDTO productSeedDTO : productSeedDTOs) {
            if (!validatorUtil.isValid(productSeedDTO)) {
                String output = validatorUtil.validate(productSeedDTO).stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.joining(System.lineSeparator()));
                System.out.println(output);
            } else {
                Product product = mapper.map(productSeedDTO, Product.class);
                product.setSeller(getRandomSeller());
                product.setBuyer(getRandomBuyer(product.getSeller()));
                product.setCategories(getRandomCategories());
                productRepository.save(product);
            }
        }
    }

    private Set<Category> getRandomCategories() {
        Set<Category> categories = new HashSet<>();

        int categoryCount = new Random().nextInt(1, 5);
        for (int i = 0; i < categoryCount; i++) {
            long id = new Random().nextLong(1, categoryRepository.count() + 1);
            categories.add(categoryRepository.findById(id).orElseThrow());
        }
        return categories;
    }

    private User getRandomSeller() {
        long id = new Random().nextLong(1, userRepository.count() + 1);
        return userRepository.findById(id).orElseThrow();
    }

    private User getRandomBuyer(User seller) {
        int random = new Random().nextInt(10);
        if (random > 4) {
            while (true) {
                long id = new Random().nextLong(1, userRepository.count() + 1);
                if (id != seller.getId()) {
                    return userRepository.findById(id).orElseThrow();
                }
            }
        }
        return null;
    }

    @Override
    public void exportProductsInRange(BigDecimal from, BigDecimal to) throws IOException {
        Set<Product> products = productRepository.findByPriceBetweenAndBuyerIsNullOrderByPrice(from, to);
        List<ProductWithNoBuyerDTO> productDTOs = products.stream()
                .map(p -> {
                    ProductWithNoBuyerDTO productDTO = mapper.map(p, ProductWithNoBuyerDTO.class);
                    productDTO.setSeller(p.getSeller().getFirstName() + " " + p.getSeller().getLastName());
                    return productDTO;
                }).toList();

        String jsonString = gson.toJson(productDTOs);
        FileWriter fileWriter = new FileWriter(JSON_EXPORT_PATH);
        fileWriter.write(jsonString);
        fileWriter.close();
    }
}
