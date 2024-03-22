package com.aminurdev.category.domain.repositories;

import com.aminurdev.category.domain.entity.Blog;
import com.aminurdev.category.domain.entity.BlogTag;
import com.aminurdev.category.domain.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BlogTagRepository extends JpaRepository<BlogTag, Integer> {
    void deleteByBlogAndTagNotIn(Blog blog, List<Tag> tags);

    Optional<BlogTag> findByBlogAndTag(Blog blog, Tag tag);
}
