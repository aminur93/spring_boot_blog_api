package com.aminurdev.category.service.impl;

import com.aminurdev.category.domain.entity.Category;
import com.aminurdev.category.domain.model.CategoryRequest;
import com.aminurdev.category.domain.repositories.CategoryRepository;
import com.aminurdev.category.service.CategoryService;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;

    private static final String RESOURCE_DIRECTORY = "./src/main/resources/uploads/category_images/";

    @Override
    public List<Category> index() {
        return categoryRepository.findAll();
    }

    @Override
    public Category store(CategoryRequest categoryRequest) throws IOException {

        String imageName = saveImageFile(categoryRequest.getImage());

        Category category = new Category();

        category.setName(categoryRequest.getName());
        category.setImage(imageName);
        category.setStatus(categoryRequest.isStatus());

        category = categoryRepository.save(category);

        return category;
    }

    private String saveImageFile(MultipartFile image) throws IOException {
        String imageName = generateUniqueImageName(Objects.requireNonNull(image.getOriginalFilename()));
        byte[] imageData = image.getBytes();
        String filePath = RESOURCE_DIRECTORY + imageName;
        Files.write(Paths.get(filePath), imageData);
        return imageName;
    }

    private String generateUniqueImageName(String originalFilename){

        String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        return UUID.randomUUID().toString() + extension;
    }

    @Override
    public Category edit(Integer categoryId) {
        return null;
    }

    @Override
    public Category update(Integer categoryId, CategoryRequest categoryRequest) {
        return null;
    }

    @Override
    public void delete(Integer categoryId) {

    }
}
