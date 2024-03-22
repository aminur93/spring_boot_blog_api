package com.aminurdev.category.service.impl;

import com.aminurdev.category.domain.entity.Blog;
import com.aminurdev.category.domain.entity.Comment;
import com.aminurdev.category.domain.entity.Replay;
import com.aminurdev.category.domain.excepation.ResourceNotFoundExcepation;
import com.aminurdev.category.domain.model.ReplayRequest;
import com.aminurdev.category.domain.repositories.BlogRepository;
import com.aminurdev.category.domain.repositories.CommentRepository;
import com.aminurdev.category.domain.repositories.ReplayRepository;
import com.aminurdev.category.response.pagination.Links;
import com.aminurdev.category.response.pagination.Meta;
import com.aminurdev.category.response.pagination.PaginatedResponse;
import com.aminurdev.category.service.ReplayService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ReplayServiceImpl implements ReplayService {

    private ReplayRepository replayRepository;

    private BlogRepository blogRepository;

    private CommentRepository commentRepository;

    @Override
    public PaginatedResponse<Replay> index(Sort.Direction direction, int page, int perPage) {

        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "updatedAt"));

        Page<Replay> replayPage = replayRepository.findAll(pageable);

        List<Replay> replays = replayPage.getContent();

        PaginatedResponse<Replay> response = new PaginatedResponse<>();
        response.setData(replays);
        response.setMessage("All replays");

        Meta meta = new Meta();

        meta.setCurrentPage(replayPage.getNumber() + 1);
        meta.setFrom(replayPage.getNumber() * replayPage.getSize() + 1);
        meta.setLastPage(replayPage.getTotalPages());
        meta.setPath("/replay");
        meta.setPerPage(replayPage.getSize());
        meta.setTo((int) replayPage.getTotalElements());
        meta.setTotal((int) replayPage.getTotalElements());
        response.setMeta(meta);

        Links links = new Links();

        links.setFirst("/replay?page=1");
        links.setLast("/replay?page=" + replayPage.getTotalPages());
        if (replayPage.hasPrevious()) {
            links.setPrev("/replay?page=" + replayPage.previousPageable().getPageNumber());
        }
        if (replayPage.hasNext()) {
            links.setNext("/replay?page=" + replayPage.nextPageable().getPageNumber());
        }

        response.setLinks(links);

        return response;
    }

    @Override
    public List<Replay> allReplays() {

        List<Replay> replays = replayRepository.findAll();

        return replays;
    }

    @Override
    public Replay store(ReplayRequest replayRequest) {

        Replay replay = new Replay();

        replay.setUser_id(replayRequest.getUser_id());

        Blog blog = blogRepository.findById(replayRequest.getBlog_id()).orElseThrow(() -> new ResourceNotFoundExcepation("Blog id is not found"));

        replay.setBlog(blog);

        Comment comment = commentRepository.findById(replayRequest.getComment_id()).orElseThrow(() -> new ResourceNotFoundExcepation("Comment id is not found"));

        replay.setComment(comment);

        replay.setReplay(replayRequest.getReplay());

        replay = replayRepository.save(replay);


        return replay;
    }

    @Override
    public void delete(Integer replayId) {

        replayRepository.deleteById(replayId);
    }
}
