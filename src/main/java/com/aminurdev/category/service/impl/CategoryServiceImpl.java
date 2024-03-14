package com.aminurdev.category.service.impl;

import com.aminurdev.category.domain.entity.Category;
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

        return categoryRepository.findAll(Sort.by(Sort.Order.desc("id")));
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
