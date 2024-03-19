package com.aminurdev.category.service;

import com.aminurdev.category.domain.entity.SubCategory;
import com.aminurdev.category.domain.model.SubCategoryRequest;
import com.aminurdev.category.response.pagination.PaginatedResponse;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface SubCategoryService {

    PaginatedResponse<SubCategory> index(Sort.Direction direction, int page, int perPage);

    List<SubCategory> allSubCategories();

    int getAllSubCategories();

    SubCategory store(SubCategoryRequest subCategoryRequest);

    SubCategory edit(Integer subCategoryId);

    SubCategory update(Integer subCategoryId, SubCategoryRequest subCategoryRequest);

    void delete(Integer subCategoryId);
}
