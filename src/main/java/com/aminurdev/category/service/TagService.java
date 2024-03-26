package com.aminurdev.category.service;

import com.aminurdev.category.domain.entity.Blog;
import com.aminurdev.category.domain.entity.Tag;
import com.aminurdev.category.domain.model.TagRequest;
import com.aminurdev.category.response.pagination.PaginatedResponse;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface TagService {

    PaginatedResponse<Tag> index(Sort.Direction direction, int page, int perPage);

    List<Tag> allTags();

    Tag getTagWiseBlogs(Integer tagId);

    int getAllTags();

    Tag store(TagRequest tagRequest);

    Tag edit(Integer tagId);

    Tag update(Integer tagId, TagRequest tagRequest);

    void delete(Integer tagId);
}
