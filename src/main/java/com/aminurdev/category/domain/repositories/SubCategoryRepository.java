package com.aminurdev.category.domain.repositories;

import com.aminurdev.category.domain.entity.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Integer> {
}
