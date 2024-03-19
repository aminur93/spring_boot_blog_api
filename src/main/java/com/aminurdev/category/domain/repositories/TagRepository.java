package com.aminurdev.category.domain.repositories;

import com.aminurdev.category.domain.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Integer> {
}
