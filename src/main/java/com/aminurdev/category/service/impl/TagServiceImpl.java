package com.aminurdev.category.service.impl;

import com.aminurdev.category.domain.entity.Blog;
import com.aminurdev.category.domain.entity.Tag;
import com.aminurdev.category.domain.excepation.ResourceNotFoundExcepation;
import com.aminurdev.category.domain.model.TagRequest;
import com.aminurdev.category.domain.repositories.TagRepository;
import com.aminurdev.category.response.pagination.Links;
import com.aminurdev.category.response.pagination.Meta;
import com.aminurdev.category.response.pagination.PaginatedResponse;
import com.aminurdev.category.service.TagService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class TagServiceImpl implements TagService {

    private TagRepository tagRepository;

    @Override
    public PaginatedResponse<Tag> index(Sort.Direction direction, int page, int perPage) {

        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "updatedAt"));

        Page<Tag> tagPage = tagRepository.findAll(pageable);

        List<Tag> tags = tagPage.getContent();

        PaginatedResponse<Tag> response = new PaginatedResponse<>();
        response.setData(tags);
        response.setMessage("All Tags");

        Meta meta = new Meta();

        meta.setCurrentPage(tagPage.getNumber() + 1);
        meta.setFrom(tagPage.getNumber() * tagPage.getSize() + 1);
        meta.setLastPage(tagPage.getTotalPages());
        meta.setPath("/tags");
        meta.setPerPage(tagPage.getSize());
        meta.setTo((int) tagPage.getTotalElements());
        meta.setTotal((int) tagPage.getTotalElements());
        response.setMeta(meta);

        Links links = new Links();

        links.setFirst("/tags?page=1");
        links.setLast("/tags?page=" + tagPage.getTotalPages());
        if (tagPage.hasPrevious()) {
            links.setPrev("/tags?page=" + tagPage.previousPageable().getPageNumber());
        }
        if (tagPage.hasNext()) {
            links.setNext("/tags?page=" + tagPage.nextPageable().getPageNumber());
        }

        response.setLinks(links);

        return response;
    }

    @Override
    public List<Tag> allTags() {

        List<Tag> tags = tagRepository.findAll();

        return tags;
    }

    @Override
    public Tag getTagWiseBlogs(Integer tagId) {

        return tagRepository.findTagByBlog(tagId);
    }

    @Override
    public int getAllTags() {
        return (int) tagRepository.count();
    }

    @Override
    public Tag store(TagRequest tagRequest) {

        Tag tag = new Tag();

        String slugName = tag.generateSlug(tagRequest.getName());

        tag.setName(tagRequest.getName());
        tag.setSlug(slugName);
        tag.setStatus(tagRequest.getStatus());

        tag = tagRepository.save(tag);

        return tag;
    }

    @Override
    public Tag edit(Integer tagId) {

        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundExcepation("Tag is not found"));

        return tag;
    }

    @Override
    public Tag update(Integer tagId, TagRequest tagRequest) {

        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundExcepation("Tag is not found"));

        String slugName = tag.generateSlug(tagRequest.getName());

        tag.setName(tagRequest.getName());
        tag.setSlug(slugName);
        tag.setStatus(tagRequest.getStatus());

        tag = tagRepository.save(tag);

        return tag;
    }

    @Override
    public void delete(Integer tagId) {

        tagRepository.deleteById(tagId);
    }
}
