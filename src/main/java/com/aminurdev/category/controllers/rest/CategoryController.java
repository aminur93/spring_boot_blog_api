package com.aminurdev.category.controllers.rest;

import com.aminurdev.category.domain.entity.Category;
import com.aminurdev.category.domain.model.CategoryRequest;
import com.aminurdev.category.service.CategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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
    public ResponseEntity<List<Category>> index()
    {
        List<Category> categories = categoryService.index();

        return ResponseEntity.ok(categories);
    }

    @PostMapping
    public ResponseEntity<Category> store(@Valid @ModelAttribute CategoryRequest categoryRequest) throws IOException {
        Category category = categoryService.store(categoryRequest);

        return new  ResponseEntity<>(category, HttpStatus.CREATED);
    }
}
