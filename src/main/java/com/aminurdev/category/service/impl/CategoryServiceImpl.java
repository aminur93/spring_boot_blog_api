package com.aminurdev.category.service.impl;

import com.aminurdev.category.domain.entity.Category;
import com.aminurdev.category.domain.entity.SubCategory;
import com.aminurdev.category.domain.excepation.ResourceNotFoundExcepation;
import com.aminurdev.category.domain.model.CategoryRequest;
import com.aminurdev.category.domain.repositories.CategoryRepository;
import com.aminurdev.category.response.pagination.Links;
import com.aminurdev.category.response.pagination.Meta;
import com.aminurdev.category.response.pagination.PaginatedResponse;
import com.aminurdev.category.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;

    private static final String RESOURCE_DIRECTORY = "./src/main/resources/uploads/category_images/";

    @Override
    public PaginatedResponse<Category> index(Sort.Direction direction, int page, int perPage) {

        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "updatedAt"));

        Page<Category> categoryPage = categoryRepository.findAll(pageable);

        List<Category> categories = categoryPage.getContent();

        PaginatedResponse<Category> response = new PaginatedResponse<>();
        response.setData(categories);
        response.setMessage("All Categories");

        Meta meta = new Meta();

        meta.setCurrentPage(categoryPage.getNumber() + 1);
        meta.setFrom(categoryPage.getNumber() * categoryPage.getSize() + 1);
        meta.setLastPage(categoryPage.getTotalPages());
        meta.setPath("/categories");
        meta.setPerPage(categoryPage.getSize());
        meta.setTo((int) categoryPage.getTotalElements());
        meta.setTotal((int) categoryPage.getTotalElements());
        response.setMeta(meta);

        Links links = new Links();

        links.setFirst("/categories?page=1");
        links.setLast("/categories?page=" + categoryPage.getTotalPages());
        if (categoryPage.hasPrevious()) {
            links.setPrev("/categories?page=" + categoryPage.previousPageable().getPageNumber());
        }
        if (categoryPage.hasNext()) {
            links.setNext("/categories?page=" + categoryPage.nextPageable().getPageNumber());
        }
        response.setLinks(links);

        return response;
    }

    @Override
    public List<Category> allCategory() {

        return categoryRepository.findAll();
    }

    @Override
    public int getTotalCategories() {
        return (int) categoryRepository.count();
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

    @Override
    public Category edit(Integer categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundExcepation("Category id is not found"));

        return category;
    }

    @Override
    public Category update(Integer categoryId, CategoryRequest categoryRequest) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundExcepation("Category id is not found"));

        if (categoryRequest.getImage() != null) {
            // Delete old image from folder
            deleteImageFile(category.getImage());

            // Save new image and update image name in the database
            String newImageName = null;
            try {
                newImageName = saveImageFile(categoryRequest.getImage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            category.setImage(newImageName);

        }else {
            category.setName(category.getImage());
        }

        category.setName(categoryRequest.getName());
        category.setStatus(categoryRequest.isStatus());

        category = categoryRepository.save(category);

        return category;
    }

    @Override
    public void delete(Integer categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundExcepation("Category id is not found"));

        if (category.getImage() != null)
        {
            deleteImageFile(category.getImage());

            categoryRepository.deleteById(categoryId);

        } else {

            categoryRepository.deleteById(categoryId);
        }

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

    private void deleteImageFile(String imageName) {

        if (imageName != null)
        {
            String imagePath = RESOURCE_DIRECTORY + File.separator + imageName;

            // Create a File object representing the image file
            File imageFile = new File(imagePath);

            // Check if the file exists and delete it if it does
            if (imageFile.exists()) {
                if (imageFile.delete()) {
                    System.out.println("Deleted image file: " + imageName);
                } else {
                    System.err.println("Failed to delete image file: " + imageName);
                }
            } else {
                System.err.println("Image file not found: " + imageName);
            }
        }
    }
}
