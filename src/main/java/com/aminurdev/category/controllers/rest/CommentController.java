package com.aminurdev.category.controllers.rest;

import com.aminurdev.category.domain.entity.Comment;
import com.aminurdev.category.domain.entity.Tag;
import com.aminurdev.category.domain.excepation.ResourceNotFoundExcepation;
import com.aminurdev.category.domain.model.CommentRequest;
import com.aminurdev.category.response.ResponseWrapper;
import com.aminurdev.category.response.SuccessResponse;
import com.aminurdev.category.response.pagination.PaginatedResponse;
import com.aminurdev.category.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/comment")
@AllArgsConstructor
public class CommentController {

    private CommentService commentService;

    @GetMapping
    public ResponseEntity<PaginatedResponse<Comment>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    )
    {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        PaginatedResponse<Comment> paginatedResponse = commentService.index(direction, page, perPage);

        return  ResponseEntity.ok(paginatedResponse);
    }

    @GetMapping("/all-comments")
    public ResponseEntity<SuccessResponse> allComments(){

        List<Comment> comments = commentService.allComments();

        Map<String, Object> responseData = new HashMap<>();

        responseData.put("comments", comments);

        SuccessResponse successResponse = new SuccessResponse(responseData);

        successResponse.setMessage("Successfully fetch comments");
        successResponse.setStatus("Success");
        successResponse.setCode(HttpStatus.OK.value());

        return ResponseEntity.status(HttpStatus.OK).body(successResponse);
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper> store(@RequestBody CommentRequest commentRequest)
    {
        try{

            Comment comment = commentService.store(commentRequest);

            Map<String, Object> responseData = new HashMap<>();

            responseData.put("id", comment.getId());
            responseData.put("user_id", comment.getUser_id());
            responseData.put("blog_id", comment.getBlog());
            responseData.put("comment", comment.getComment());
            responseData.put("createdAt", comment.getCreatedAt());
            responseData.put("updatedAt", comment.getUpdatedAt());

            ResponseWrapper responseWrapper = ResponseWrapper.createSuccessResponse(responseData, "Store Successful","success", HttpStatus.CREATED.value());

            return ResponseEntity.status(HttpStatus.CREATED).body(responseWrapper);

        }catch (Exception e){

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("error", e.getMessage());

            ResponseWrapper errorResponse = ResponseWrapper.error(responseData, "failed", "error", HttpStatus.INTERNAL_SERVER_ERROR.value());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ResponseWrapper> delete(@PathVariable("id") Integer commentId)
    {
        try{

            commentService.delete(commentId);

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
