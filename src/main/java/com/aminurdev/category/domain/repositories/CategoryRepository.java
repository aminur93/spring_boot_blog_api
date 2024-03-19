package com.aminurdev.category.domain.repositories;

import com.aminurdev.category.domain.entity.Category;
import com.aminurdev.category.domain.entity.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
