package com.aminurdev.category.domain.repositories;

import com.aminurdev.category.domain.entity.Blog;
import com.aminurdev.category.domain.entity.BlogSubCategory;
import com.aminurdev.category.domain.entity.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BlogSubCategoryRepository extends JpaRepository<BlogSubCategory, Integer> {
    void deleteByBlogAndSubCategoryNotIn(Blog blog, List<SubCategory> subCategories);

    Optional<BlogSubCategory> findByBlogAndSubCategory(Blog blog, SubCategory subCat);
}
