package com.aminurdev.category.controllers.rest;

import com.aminurdev.category.domain.entity.Category;
import com.aminurdev.category.domain.model.CategoryRequest;
import com.aminurdev.category.response.pagination.Links;
import com.aminurdev.category.response.pagination.Meta;
import com.aminurdev.category.response.pagination.PaginatedResponse;
import com.aminurdev.category.service.CategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
@AllArgsConstructor
public class CategoryController {

    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<PaginatedResponse<Category>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        PaginatedResponse<Category> paginatedResponse = categoryService.index(direction, page, perPage);

        return ResponseEntity.ok(paginatedResponse);
    }

    @GetMapping("/all-categories")
    public ResponseEntity<List<Category>> getAllCategory()
    {
        List<Category> categories = categoryService.allCategory();

        return ResponseEntity.ok(categories);
    }


    @PostMapping
    public ResponseEntity<Category> store(@Valid @ModelAttribute CategoryRequest categoryRequest) throws IOException {
        Category category = categoryService.store(categoryRequest);

        return new  ResponseEntity<>(category, HttpStatus.CREATED);
    }
}
