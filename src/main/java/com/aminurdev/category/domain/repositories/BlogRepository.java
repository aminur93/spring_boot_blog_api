package com.aminurdev.category.domain.repositories;

import com.aminurdev.category.domain.entity.Blog;
import com.aminurdev.category.response.pagination.PaginatedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Integer> {

    Page<Blog> findBlogByCategoryId(Integer categoryId, Pageable pageable);
}
