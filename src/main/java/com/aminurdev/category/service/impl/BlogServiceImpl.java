package com.aminurdev.category.service.impl;

import com.aminurdev.category.domain.entity.*;
import com.aminurdev.category.domain.excepation.ResourceNotFoundExcepation;
import com.aminurdev.category.domain.model.BlogRequest;
import com.aminurdev.category.domain.repositories.*;
import com.aminurdev.category.response.pagination.Links;
import com.aminurdev.category.response.pagination.Meta;
import com.aminurdev.category.response.pagination.PaginatedResponse;
import com.aminurdev.category.service.BlogService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BlogServiceImpl implements BlogService {

    private BlogRepository blogRepository;
    private CategoryRepository categoryRepository;
    private TagRepository tagRepository;
    private BlogTagRepository blogTagRepository;
    private SubCategoryRepository subCategoryRepository;
    private BlogSubCategoryRepository blogSubCategoryRepository;

    private static final String  RESOURCE_DIRECTORY = "./src/main/resources/uploads/blog_image/";

    @Override
    public PaginatedResponse<Blog> index(Sort.Direction direction, int page, int perPage) {

        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "updatedAt"));

        Page<Blog> blogPage = blogRepository.findAll(pageable);

        List<Blog> blogs = blogPage.getContent();

        PaginatedResponse<Blog> response = new PaginatedResponse<>();
        response.setData(blogs);
        response.setMessage("All Blogs");

        Meta meta = new Meta();

        meta.setCurrentPage(blogPage.getNumber() + 1);
        meta.setFrom(blogPage.getNumber() * blogPage.getSize() + 1);
        meta.setLastPage(blogPage.getTotalPages());
        meta.setPath("/blogs");
        meta.setPerPage(blogPage.getSize());
        meta.setTo((int) blogPage.getTotalElements());
        meta.setTotal((int) blogPage.getTotalElements());
        response.setMeta(meta);

        Links links = new Links();

        links.setFirst("/blogs?page=1");
        links.setLast("/blogs?page=" + blogPage.getTotalPages());
        if (blogPage.hasPrevious()) {
            links.setPrev("/blogs?page=" + blogPage.previousPageable().getPageNumber());
        }
        if (blogPage.hasNext()) {
            links.setNext("/blogs?page=" + blogPage.nextPageable().getPageNumber());
        }

        response.setLinks(links);

        return response;
    }

    @Override
    public List<Blog> allBlogs() {
        return blogRepository.findAll();
    }

    @Override
    public int getAllBlogs() {
        return (int) blogRepository.count();
    }

    @Override
    public Blog store(BlogRequest blogRequest) {

        Blog blog = new Blog();

        String imageName = null;
        imageName = saveImageFile(blogRequest.getImage());

        String slugName = blog.generateSlug(blogRequest.getTitle());

        blog.setAuthor_id(blogRequest.getAuthor_id());

        // Fetch the Category entity using its ID
        Category category = categoryRepository.findById(blogRequest.getCategory_id())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        blog.setCategory(category);
        blog.setTitle(blogRequest.getTitle());
        blog.setSlogan(blogRequest.getSlogan());
        blog.setSlug(slugName);
        blog.setDescription(blogRequest.getDescription());
        blog.setDate(blogRequest.getDate());
        blog.setImage(imageName);
        blog.setStatus(blogRequest.getStatus());

        blog = blogRepository.save(blog);

        List<Tag> tags = Optional.ofNullable(blogRequest.getTagIds())
                .orElse(List.of()) // Empty list if tagIds is null
                .stream()
                .map(tagId -> tagRepository.findById(tagId)
                        .orElseThrow(() -> new EntityNotFoundException("Tag not found with id: " + tagId)))
                .toList();

        for (Tag tag: tags)
        {
            BlogTag blogTag = new BlogTag();

            blogTag.setBlog(blog);
            blogTag.setTag(tag);

            blogTagRepository.save(blogTag);
        }

        List<SubCategory> subCategories = Optional.ofNullable(blogRequest.getSubCategoryIds())
                .orElse(List.of()) // Empty list if tagIds is null
                .stream()
                .map(subCatId -> subCategoryRepository.findById(subCatId)
                        .orElseThrow(() -> new EntityNotFoundException("Sub category not found with id: " + subCatId)))
                .toList();

        for (SubCategory subCat: subCategories)
        {
            BlogSubCategory blogSubCategory = new BlogSubCategory();

            blogSubCategory.setBlog(blog);
            blogSubCategory.setSubCategory(subCat);

            blogSubCategoryRepository.save(blogSubCategory);
        }

        return blog;
    }

    @Override
    public Blog edit(Integer blogId) {

        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new ResourceNotFoundExcepation("Blog id is not found"));

        return blog;
    }

    @Override
    @Transactional
    public Blog update(Integer blogId, BlogRequest blogRequest) {

        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new ResourceNotFoundExcepation("Blog id is not found"));

        if (blogRequest.getImage() != null)
        {
            deleteImageFile(blog.getImage());

            String imageName = null;
            imageName = saveImageFile(blogRequest.getImage());

            blog.setImage(imageName);
        }else {
            blog.setImage(blog.getImage());
        }

        String slugName = blog.generateSlug(blogRequest.getTitle());

        // Fetch the Category entity using its ID
        Category category = categoryRepository.findById(blogRequest.getCategory_id())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        blog.setAuthor_id(blogRequest.getAuthor_id());
        blog.setCategory(category);
        blog.setTitle(blogRequest.getTitle());
        blog.setSlogan(blogRequest.getSlogan());
        blog.setSlug(slugName);
        blog.setDescription(blogRequest.getDescription());
        blog.setDate(blogRequest.getDate());
        blog.setStatus(blogRequest.getStatus());

        blog = blogRepository.save(blog);

        // Update the associated tags
        List<Tag> tags = Optional.ofNullable(blogRequest.getTagIds())
                .orElse(List.of()) // Empty list if tagIds is null
                .stream()
                .map(tagId -> tagRepository.findById(tagId)
                        .orElseThrow(() -> new EntityNotFoundException("Tag not found with id: " + tagId)))
                .toList();

        // Remove existing BlogTags not present in the updated list
        blogTagRepository.deleteByBlogAndTagNotIn(blog, tags);

        // Add or update BlogTags for the updated list
        for (Tag tag : tags) {
            BlogTag blogTag = blogTagRepository.findByBlogAndTag(blog, tag)
                    .orElse(new BlogTag()); // If not found, create a new BlogTag entity

            // Update the BlogTag entity with the current blog and tag
            blogTag.setBlog(blog);
            blogTag.setTag(tag);

            // Save the BlogTag entity to the database
            blogTagRepository.save(blogTag);
        }

        // Update the associated subcategories
        List<SubCategory> subCategories = Optional.ofNullable(blogRequest.getSubCategoryIds())
                .orElse(List.of()) // Empty list if subCategoryIds is null
                .stream()
                .map(subCatId -> subCategoryRepository.findById(subCatId)
                        .orElseThrow(() -> new EntityNotFoundException("Sub category not found with id: " + subCatId)))
                .toList();

        // Remove existing BlogSubCategories not present in the updated list
        blogSubCategoryRepository.deleteByBlogAndSubCategoryNotIn(blog, subCategories);

        // Add or update BlogSubCategories for the updated list
        for (SubCategory subCat : subCategories) {
            // Try to find an existing BlogSubCategory entity for the current blog and subCategory combination
            BlogSubCategory blogSubCategory = blogSubCategoryRepository.findByBlogAndSubCategory(blog, subCat)
                    .orElse(new BlogSubCategory()); // If not found, create a new BlogSubCategory entity

            // Update the BlogSubCategory entity with the current blog and subCategory
            blogSubCategory.setBlog(blog);
            blogSubCategory.setSubCategory(subCat);

            // Save the BlogSubCategory entity to the database
            blogSubCategoryRepository.save(blogSubCategory);
        }

        return blog;
    }

    @Override
    public void delete(Integer blogId) {

        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new ResourceNotFoundExcepation("Blog id is not found"));

        if (blog.getImage() != null)
        {
            deleteImageFile(blog.getImage());

            blogTagRepository.deleteById(blogId);

            blogSubCategoryRepository.deleteById(blogId);

            blogRepository.deleteById(blogId);

        }else {

            blogTagRepository.deleteById(blogId);

            blogSubCategoryRepository.deleteById(blogId);

            blogRepository.deleteById(blogId);
        }

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
