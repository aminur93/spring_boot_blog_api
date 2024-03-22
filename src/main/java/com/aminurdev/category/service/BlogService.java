package com.aminurdev.category.service;

import com.aminurdev.category.domain.entity.Blog;
import com.aminurdev.category.domain.model.BlogRequest;
import com.aminurdev.category.response.pagination.PaginatedResponse;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface BlogService {

    PaginatedResponse<Blog> index(Sort.Direction direction, int page, int perPage);

    List<Blog> allBlogs();

    int getAllBlogs();

    Blog store(BlogRequest blogRequest);

    Blog edit(Integer blogId);

    Blog update(Integer blogId, BlogRequest blogRequest);

    void delete(Integer blogId);
}
