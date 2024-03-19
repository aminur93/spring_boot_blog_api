package com.aminurdev.category.controllers.rest;

import com.aminurdev.category.domain.entity.Tag;
import com.aminurdev.category.domain.excepation.ResourceNotFoundExcepation;
import com.aminurdev.category.domain.model.TagRequest;
import com.aminurdev.category.response.ResponseWrapper;
import com.aminurdev.category.response.SuccessResponse;
import com.aminurdev.category.response.pagination.PaginatedResponse;
import com.aminurdev.category.service.TagService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/tag")
@AllArgsConstructor
public class TagController {

    private TagService tagService;

    @GetMapping
    public ResponseEntity<PaginatedResponse<Tag>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    )
    {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        PaginatedResponse<Tag> paginatedResponse = tagService.index(direction, page, perPage);

        return  ResponseEntity.ok(paginatedResponse);
    }

    @GetMapping("/all-tags")
    public ResponseEntity<SuccessResponse> allTags()
    {
        List<Tag> tags = tagService.allTags();

        Map<String, Object> responseData = new HashMap<>();

        responseData.put("tags", tags);

        SuccessResponse successResponse = new SuccessResponse(responseData);

        successResponse.setMessage("Successfully fetch tags");
        successResponse.setStatus("Success");
        successResponse.setCode(HttpStatus.OK.value());

        return ResponseEntity.status(HttpStatus.OK).body(successResponse);
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper> store(@Valid @RequestBody TagRequest tagRequest)
    {
        try{

            Tag tag = tagService.store(tagRequest);

            Map<String, Object> responseData = new HashMap<>();

            responseData.put("id", tag.getId());
            responseData.put("name", tag.getName());
            responseData.put("slug", tag.getSlug());
            responseData.put("status", tag.getStatus());
            responseData.put("created_at", tag.getCreatedAt());
            responseData.put("updated_at", tag.getUpdatedAt());

            ResponseWrapper responseWrapper = ResponseWrapper.createSuccessResponse(responseData, "Tag store successful", "success", HttpStatus.CREATED.value());

            return ResponseEntity.status(HttpStatus.CREATED.value()).body(responseWrapper);
        } catch (Exception e){

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("error", e.getMessage());

            ResponseWrapper errorResponse = ResponseWrapper.error(responseData, "failed", "error", HttpStatus.INTERNAL_SERVER_ERROR.value());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<ResponseWrapper> edit(@PathVariable("id") Integer tagId)
    {
        try{

            Tag tag = tagService.edit(tagId);

            Map<String, Object> responseData = new HashMap<>();

            responseData.put("id", tag.getId());
            responseData.put("name", tag.getName());
            responseData.put("slug", tag.getSlug());
            responseData.put("status", tag.getStatus());
            responseData.put("createdAt", tag.getCreatedAt());
            responseData.put("updatedAt", tag.getUpdatedAt());

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
    public ResponseEntity<ResponseWrapper> update(@PathVariable("id") Integer tagId,  @Valid @RequestBody TagRequest tagRequest)
    {

        try{

            Tag tag = tagService.update(tagId, tagRequest);

            Map<String, Object> updateResponseData = new HashMap<>();

            updateResponseData.put("id", tag.getId());
            updateResponseData.put("name", tag.getName());
            updateResponseData.put("slug", tag.getSlug());
            updateResponseData.put("status", tag.getStatus());
            updateResponseData.put("createdAt", tag.getCreatedAt());
            updateResponseData.put("updatedAt", tag.getUpdatedAt());

            ResponseWrapper successResponse = ResponseWrapper.createSuccessResponse(updateResponseData, "update successful", "Success", HttpStatus.OK.value());

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
    public ResponseEntity<ResponseWrapper> delete(@PathVariable("id") Integer tagId)
    {
        try{

            tagService.delete(tagId);

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
