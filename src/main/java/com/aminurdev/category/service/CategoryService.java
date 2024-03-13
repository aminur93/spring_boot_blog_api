package com.aminurdev.category.service;

import com.aminurdev.category.domain.entity.Category;
import com.aminurdev.category.domain.model.CategoryRequest;

import java.io.IOException;
import java.util.List;

public interface CategoryService {

    List<Category> index();

    Category store(CategoryRequest categoryRequest) throws IOException;

    Category edit(Integer categoryId);

    Category update(Integer categoryId, CategoryRequest categoryRequest);

    void delete(Integer categoryId);
}
