package com.aminurdev.category.service;

import com.aminurdev.category.domain.entity.Comment;
import com.aminurdev.category.domain.model.CommentRequest;
import com.aminurdev.category.response.pagination.PaginatedResponse;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface CommentService {

    PaginatedResponse<Comment> index(Sort.Direction direction, int page , int perPage);

    List<Comment> allComments();

    int getAllComments();

    Comment store(CommentRequest commentRequest);

    void delete(Integer commentId);
}
