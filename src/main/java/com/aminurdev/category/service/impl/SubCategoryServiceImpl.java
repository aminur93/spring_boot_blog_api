package com.aminurdev.category.service.impl;

import com.aminurdev.category.domain.entity.Category;
import com.aminurdev.category.domain.entity.SubCategory;
import com.aminurdev.category.domain.excepation.ResourceNotFoundExcepation;
import com.aminurdev.category.domain.model.SubCategoryRequest;
import com.aminurdev.category.domain.repositories.CategoryRepository;
import com.aminurdev.category.domain.repositories.SubCategoryRepository;
import com.aminurdev.category.response.pagination.Links;
import com.aminurdev.category.response.pagination.Meta;
import com.aminurdev.category.response.pagination.PaginatedResponse;
import com.aminurdev.category.service.SubCategoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SubCategoryServiceImpl implements SubCategoryService {

    private SubCategoryRepository subCategoryRepository;

    private CategoryRepository categoryRepository;

    private static final String  RESOURCE_DIRECTORY = "./src/main/resources/uploads/sub_category_images/";

    public PaginatedResponse<SubCategory> index(Sort.Direction direction, int page, int perPage)
    {
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "updatedAt"));

        Page<SubCategory> subCategoryPage = subCategoryRepository.findAll(pageable);

        List<SubCategory> subCategories = subCategoryPage.getContent();

        PaginatedResponse<SubCategory> response = new PaginatedResponse<>();
        response.setData(subCategories);
        response.setMessage("All Sub Categories");

        Meta meta = new Meta();

        meta.setCurrentPage(subCategoryPage.getNumber() + 1);
        meta.setFrom(subCategoryPage.getNumber() * subCategoryPage.getSize() + 1);
        meta.setLastPage(subCategoryPage.getTotalPages());
        meta.setPath("/sub-categories");
        meta.setPerPage(subCategoryPage.getSize());
        meta.setTo((int) subCategoryPage.getTotalElements());
        meta.setTotal((int) subCategoryPage.getTotalElements());
        response.setMeta(meta);

        Links links = new Links();

        links.setFirst("/sub-categories?page=1");
        links.setLast("/sub-categories?page=" + subCategoryPage.getTotalPages());
        if (subCategoryPage.hasPrevious()) {
            links.setPrev("/sub-categories?page=" + subCategoryPage.previousPageable().getPageNumber());
        }
        if (subCategoryPage.hasNext()) {
            links.setNext("/sub-categories?page=" + subCategoryPage.nextPageable().getPageNumber());
        }

        response.setLinks(links);

        return response;
    }

    @Override
    public List<SubCategory> allSubCategories() {

        return subCategoryRepository.findAll(Sort.by(Sort.Order.desc("id")));
    }

    @Override
    public int getAllSubCategories() {
        return (int) subCategoryRepository.count();
    }

    @Override
    public SubCategory store(SubCategoryRequest subCategoryRequest) {

        String imageName = null;
        imageName = saveImageFile(subCategoryRequest.getImage());

        SubCategory subCategory = new SubCategory();

        subCategory.setName(subCategoryRequest.getName());
        subCategory.setImage(imageName);
        subCategory.setStatus(subCategoryRequest.getStatus());

        // Fetch the Category entity using its ID
        Category category = categoryRepository.findById(subCategoryRequest.getCategory_id())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        subCategory.setCategory(category);

        subCategory = subCategoryRepository.save(subCategory);

        return subCategory;
    }

    @Override
    public SubCategory edit(Integer subCategoryId) {

        SubCategory subCategory = subCategoryRepository.findById(subCategoryId)
                .orElseThrow(() -> new ResourceNotFoundExcepation("Sub category id not found"));

        return subCategory;
    }

    @Override
    public SubCategory update(Integer subCategoryId, SubCategoryRequest subCategoryRequest) {

        SubCategory subCategory = subCategoryRepository.findById(subCategoryId)
                .orElseThrow(() -> new ResourceNotFoundExcepation("Sub category id not found"));

        if (subCategoryRequest.getImage() != null)
        {
            // delete old image
            deleteImageFile(subCategory.getImage());

            // Save new image and update image name in the database
            String newImageName = null;
            newImageName = saveImageFile(subCategoryRequest.getImage());

            subCategory.setImage(newImageName);
        }else {
            subCategory.setImage(subCategory.getImage());
        }

        subCategory.setName(subCategoryRequest.getName());
        subCategory.setStatus(subCategoryRequest.getStatus());

        Category category = categoryRepository.findById(subCategoryRequest.getCategory_id())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        subCategory.setCategory(category);

        subCategory = subCategoryRepository.save(subCategory);

        return subCategory;
    }

    @Override
    public void delete(Integer subCategoryId) {

        SubCategory subCategory = subCategoryRepository.findById(subCategoryId)
                .orElseThrow(() -> new ResourceNotFoundExcepation("Sub category id not found"));

        if (subCategory.getImage() != null)
        {
            deleteImageFile(subCategory.getImage());

            subCategoryRepository.deleteById(subCategoryId);
        }else {
            subCategoryRepository.deleteById(subCategoryId);
        }

        subCategoryRepository.deleteById(subCategoryId);
    }

    private String saveImageFile(MultipartFile image) {

        String imageName = generateUniqueImageName(Objects.requireNonNull(image.getOriginalFilename()));

        byte[] imageData = new byte[0];
        try {
            imageData = image.getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String filePath = RESOURCE_DIRECTORY + imageName;
        try {
            Files.write(Paths.get(filePath), imageData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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
