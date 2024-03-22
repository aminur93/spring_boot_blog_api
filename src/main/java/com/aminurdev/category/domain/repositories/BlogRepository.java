package com.aminurdev.category.domain.repositories;

import com.aminurdev.category.domain.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Blog, Integer> {
}
