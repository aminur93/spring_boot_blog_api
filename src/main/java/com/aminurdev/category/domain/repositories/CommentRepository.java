package com.aminurdev.category.domain.repositories;

import com.aminurdev.category.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
