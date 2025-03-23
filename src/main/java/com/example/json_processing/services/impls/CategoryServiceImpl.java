package com.example.json_processing.services.impls;

import com.example.json_processing.data.entities.Category;
import com.example.json_processing.data.repositories.CategoryRepository;
import com.example.json_processing.services.CategoryService;
import com.example.json_processing.services.dtos.CategorySeedDTO;
import com.example.json_processing.services.dtos.export.CategoryDTO;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private static final String JSON_SEED_PATH = "src/main/resources/json/categories.json";
    private static final String JSON_EXPORT_PATH = "src/main/resources/json/exports/categories-by-products.json";
    private final CategoryRepository categoryRepository;
    private final ModelMapper mapper;
    private final ValidatorUtil validatorUtil;
    private final Gson gson;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper mapper, ValidatorUtil validatorUtil, Gson gson) {
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
        this.validatorUtil = validatorUtil;
        this.gson = gson;
    }

    @Override
    public boolean isImported() {
        return categoryRepository.count() > 0;
    }

    @Override
    public void seedCategories() throws IOException {
        String jsonString = Files.readAllLines(Path.of(JSON_SEED_PATH)).stream()
                .collect(Collectors.joining(System.lineSeparator()));

        CategorySeedDTO[] categorySeedDTOs = gson.fromJson(jsonString, CategorySeedDTO[].class);

        for (CategorySeedDTO categorySeedDTO : categorySeedDTOs) {
            if (!validatorUtil.isValid(categorySeedDTO)) {
                String output = validatorUtil.validate(categorySeedDTO).stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.joining(System.lineSeparator()));
                System.out.println(output);
            } else {
                Category category = mapper.map(categorySeedDTO, Category.class);
                categoryRepository.save(category);
            }
        }
    }

    @Override
    public void exportCategoriesWithProducts() throws IOException {
        List<CategoryDTO> categoryDTOs = categoryRepository.findByOrderByProductsCountDesc().stream()
                .map(c -> {
                    CategoryDTO categoryDTO = new CategoryDTO();

                    categoryDTO.setCategory(c.getName());
                    categoryDTO.setProductsCount(c.getProducts().size());

                    double averagePrice = c.getProducts().stream()
                            .mapToDouble(p -> p.getPrice().doubleValue())
                            .average().orElseThrow();
                    categoryDTO.setAveragePrice(BigDecimal.valueOf(averagePrice));

                    double totalRevenue = c.getProducts().stream()
                            .mapToDouble(p -> p.getPrice().doubleValue())
                            .sum();
                    categoryDTO.setTotalRevenue(BigDecimal.valueOf(totalRevenue));

                    return categoryDTO;
                }).toList();

        String jsonString = gson.toJson(categoryDTOs);
        FileWriter fileWriter = new FileWriter(JSON_EXPORT_PATH);
        fileWriter.write(jsonString);
        fileWriter.close();
    }
}
