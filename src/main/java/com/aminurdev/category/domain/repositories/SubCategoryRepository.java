package com.aminurdev.category.domain.repositories;

import com.aminurdev.category.domain.entity.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Integer> {

    @Query("SELECT s FROM SubCategory s LEFT JOIN FETCH s.blog b WHERE s.id = :subCategoryId")
   Optional<SubCategory>  findSubCategoryByBlog(@Param("subCategoryId") Integer subCategoryId);
}
