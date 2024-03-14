package com.aminurdev.category.service;

import com.aminurdev.category.domain.entity.Category;
import com.aminurdev.category.domain.model.CategoryRequest;
import com.aminurdev.category.response.pagination.PaginatedResponse;
import org.springframework.data.domain.Sort;

import java.io.IOException;
import java.util.List;

public interface CategoryService {

    PaginatedResponse<Category> index(Sort.Direction direction, int page, int perPage);

    List<Category> allCategory();

    int getTotalCategories();

    Category store(CategoryRequest categoryRequest) throws IOException;

    Category edit(Integer categoryId);

    Category update(Integer categoryId, CategoryRequest categoryRequest);

    void delete(Integer categoryId);
}
