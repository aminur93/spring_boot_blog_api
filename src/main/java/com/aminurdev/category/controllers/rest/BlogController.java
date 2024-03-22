package com.aminurdev.category.controllers.rest;

import com.aminurdev.category.domain.entity.Blog;
import com.aminurdev.category.domain.entity.SubCategory;
import com.aminurdev.category.domain.entity.Tag;
import com.aminurdev.category.domain.excepation.ResourceNotFoundExcepation;
import com.aminurdev.category.domain.model.BlogRequest;
import com.aminurdev.category.response.ResponseWrapper;
import com.aminurdev.category.response.pagination.PaginatedResponse;
import com.aminurdev.category.service.BlogService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/blog")
@AllArgsConstructor
public class BlogController {

    private BlogService blogService;

    @GetMapping
    public ResponseEntity<PaginatedResponse<Blog>> index( @RequestParam(defaultValue = "DESC") String sortDirection,
                                                          @RequestParam(defaultValue = "1") int page,
                                                          @RequestParam(defaultValue = "10") int perPage)
    {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        PaginatedResponse<Blog> paginatedResponse = blogService.index(direction, page, perPage);

        return ResponseEntity.ok(paginatedResponse);
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper> store(@Valid @ModelAttribute BlogRequest blogRequest)
    {
        try{

            Blog blog = blogService.store(blogRequest);

            Map<String, Object> responseData = new HashMap<>();

            responseData.put("id", blog.getId());
            responseData.put("title", blog.getTitle());
            responseData.put("slogan", blog.getSlogan());
            responseData.put("slug", blog.getSlug());
            responseData.put("status", blog.getStatus());
            responseData.put("created_at", blog.getCreatedAt());
            responseData.put("updated_at", blog.getUpdatedAt());

            ResponseWrapper responseWrapper = ResponseWrapper.createSuccessResponse(responseData, "Blog store successful", "success", HttpStatus.CREATED.value());

            return ResponseEntity.status(HttpStatus.CREATED.value()).body(responseWrapper);
        } catch (Exception e){

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("error", e.getMessage());

            ResponseWrapper errorResponse = ResponseWrapper.error(responseData, "failed", "error", HttpStatus.INTERNAL_SERVER_ERROR.value());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<ResponseWrapper> edit(@PathVariable("id") Integer blogId)
    {
        try{

            Blog blog = blogService.edit(blogId);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("blog", blog);

            ResponseWrapper successResponse = ResponseWrapper.createSuccessResponse(responseData, "fetch successful", "Success", HttpStatus.OK.value());

            return ResponseEntity.status(HttpStatus.OK).body(successResponse);

        }catch (ResourceNotFoundExcepation e){

            Map<String, Object> responseData = new HashMap<>();

            responseData.put("error", e.getMessage());

            ResponseWrapper errorResponse = ResponseWrapper.error(responseData, "failed", "error", 400);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

        }catch (Exception e){

            Map<String, Object> responseData = new HashMap<>();

            responseData.put("error", e.getMessage());

            ResponseWrapper errorResponse = ResponseWrapper.error(responseData, "failed", "error", 500);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<ResponseWrapper> update(@PathVariable("id")  @Valid @ModelAttribute Integer blogId, BlogRequest blogRequest)
    {
        try{

            Blog blog = blogService.update(blogId, blogRequest);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("blog", blog);

            ResponseWrapper successResponse = ResponseWrapper.createSuccessResponse(responseData, "update successful", "Success", HttpStatus.OK.value());

            return ResponseEntity.status(HttpStatus.OK).body(successResponse);

        }catch (ResourceNotFoundExcepation e){

            Map<String, Object> responseData = new HashMap<>();

            responseData.put("error", e.getMessage());

            ResponseWrapper errorResponse = ResponseWrapper.error(responseData, "failed", "error", 400);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

        }catch (Exception e){

            Map<String, Object> responseData = new HashMap<>();

            responseData.put("error", e.getMessage());

            ResponseWrapper errorResponse = ResponseWrapper.error(responseData, "failed", "error", 500);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ResponseWrapper> delete(@PathVariable("id") Integer blogId)
    {
        try{

            blogService.delete(blogId);

            ResponseWrapper responseWrapper = ResponseWrapper.createSuccessResponse(null, "delete Successful","success", HttpStatus.OK.value());

            return ResponseEntity.status(HttpStatus.OK).body(responseWrapper);

        }catch (ResourceNotFoundExcepation e){

            Map<String, Object> responseData = new HashMap<>();

            responseData.put("error", e.getMessage());

            ResponseWrapper errorResponse = ResponseWrapper.error(responseData, "failed", "error", 400);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

        }catch (Exception e){

            Map<String, Object> responseData = new HashMap<>();

            responseData.put("error", e.getMessage());

            ResponseWrapper errorResponse = ResponseWrapper.error(responseData, "failed", "error", HttpStatus.INTERNAL_SERVER_ERROR.value());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
