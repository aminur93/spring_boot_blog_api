package com.aminurdev.category.domain.repositories;

import com.aminurdev.category.domain.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Integer> {

    @Query("SELECT t FROM Tag t LEFT JOIN FETCH t.blog b WHERE t.id = :tagId")
    Tag findTagByBlog(@Param("tagId") Integer tagId);
}
