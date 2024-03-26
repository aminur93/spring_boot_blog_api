package com.aminurdev.category.domain.repositories;

import com.aminurdev.category.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query("SELECT c FROM Category c JOIN FETCH c.blogs WHERE c.id = :categoryId")
    List<Category> findAllWithBlogs(Integer categoryId);
}
