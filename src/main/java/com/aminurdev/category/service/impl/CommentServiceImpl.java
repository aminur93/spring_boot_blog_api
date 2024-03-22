package com.aminurdev.category.service.impl;

import com.aminurdev.category.domain.entity.Blog;
import com.aminurdev.category.domain.entity.Comment;
import com.aminurdev.category.domain.entity.Tag;
import com.aminurdev.category.domain.excepation.ResourceNotFoundExcepation;
import com.aminurdev.category.domain.model.CommentRequest;
import com.aminurdev.category.domain.repositories.BlogRepository;
import com.aminurdev.category.domain.repositories.CommentRepository;
import com.aminurdev.category.response.pagination.Links;
import com.aminurdev.category.response.pagination.Meta;
import com.aminurdev.category.response.pagination.PaginatedResponse;
import com.aminurdev.category.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;

    private BlogRepository blogRepository;


    @Override
    public PaginatedResponse<Comment> index(Sort.Direction direction, int page, int perPage) {

        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "updatedAt"));

        Page<Comment> commentPage = commentRepository.findAll(pageable);

        List<Comment> comments = commentPage.getContent();

        PaginatedResponse<Comment> response = new PaginatedResponse<>();
        response.setData(comments);
        response.setMessage("All Comments");

        Meta meta = new Meta();

        meta.setCurrentPage(commentPage.getNumber() + 1);
        meta.setFrom(commentPage.getNumber() * commentPage.getSize() + 1);
        meta.setLastPage(commentPage.getTotalPages());
        meta.setPath("/comment");
        meta.setPerPage(commentPage.getSize());
        meta.setTo((int) commentPage.getTotalElements());
        meta.setTotal((int) commentPage.getTotalElements());
        response.setMeta(meta);

        Links links = new Links();

        links.setFirst("/comment?page=1");
        links.setLast("/comment?page=" + commentPage.getTotalPages());
        if (commentPage.hasPrevious()) {
            links.setPrev("/comment?page=" + commentPage.previousPageable().getPageNumber());
        }
        if (commentPage.hasNext()) {
            links.setNext("/comment?page=" + commentPage.nextPageable().getPageNumber());
        }

        response.setLinks(links);

        return response;
    }

    @Override
    public List<Comment> allComments() {

        List<Comment> comments = commentRepository.findAll();

        return comments;
    }

    @Override
    public int getAllComments() {

        return (int) commentRepository.count();
    }

    @Override
    public Comment store(CommentRequest commentRequest) {

        Comment comment = new Comment();

        comment.setUser_id(commentRequest.getUser_id());

        Blog blog = blogRepository.findById(commentRequest.getBlog_id()).orElseThrow(() -> new ResourceNotFoundExcepation("Blog id not found"));

        comment.setBlog(blog);

        comment.setComment(commentRequest.getComment());

        comment = commentRepository.save(comment);

        return comment;
    }

    @Override
    public void delete(Integer commentId) {
        commentRepository.deleteById(commentId);
    }
}
