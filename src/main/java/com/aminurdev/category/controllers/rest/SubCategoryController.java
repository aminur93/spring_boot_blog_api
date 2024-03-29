package com.aminurdev.category.controllers.rest;

import com.aminurdev.category.controllers.advice.ApplicationExcepationHandler;
import com.aminurdev.category.domain.entity.Blog;
import com.aminurdev.category.domain.entity.SubCategory;
import com.aminurdev.category.domain.excepation.ResourceNotFoundExcepation;
import com.aminurdev.category.domain.model.SubCategoryRequest;
import com.aminurdev.category.response.ErrorResponse;
import com.aminurdev.category.response.ResponseWrapper;
import com.aminurdev.category.response.SuccessResponse;
import com.aminurdev.category.response.pagination.PaginatedResponse;
import com.aminurdev.category.service.SubCategoryService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.integration.IntegrationProperties;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/sub-category")
@AllArgsConstructor
public class SubCategoryController {

    private SubCategoryService subCategoryService;

    @GetMapping
    public ResponseEntity<PaginatedResponse<SubCategory>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    )
    {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        PaginatedResponse<SubCategory> paginatedResponse = subCategoryService.index(direction, page, perPage);

        return ResponseEntity.ok(paginatedResponse);
    }

    @GetMapping("/blogs/{id}")
    public ResponseEntity<ResponseWrapper> getSubCategoryWiseBlog(@PathVariable("id") Integer subCategoryId)
    {
        try{
            SubCategory subCategory = subCategoryService.getSubCategoryWiseBlog(subCategoryId);

            Map<String, Object> responseData = new LinkedHashMap<>();

            List<Map<String, Object>> blogData = new ArrayList<>();

            for (Blog blog: subCategory.getBlog())
            {
                Map<String, Object> blogResponse = new LinkedHashMap<>();

                blogResponse.put("id", blog.getId());
                blogResponse.put("title", blog.getTitle());
                blogResponse.put("slogan", blog.getSlogan());
                blogResponse.put("slug", blog.getSlug());
                blogResponse.put("description", blog.getDescription());
                blogResponse.put("image", blog.getImage());
                blogResponse.put("date", blog.getDate());
                blogResponse.put("created_at", blog.getCreatedAt());
                blogResponse.put("updated_at", blog.getUpdatedAt());

                blogData.add(blogResponse);

            }

            responseData.put("id", subCategory.getId());
            responseData.put("name", subCategory.getName());
            responseData.put("image", subCategory.getImage());
            responseData.put("status", subCategory.getStatus());
            responseData.put("blog", blogData);

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

    @GetMapping("/all-sub-categories")
    public ResponseEntity<SuccessResponse> allSubCategories()
    {
        List<SubCategory> subCategories = subCategoryService.allSubCategories();

        Map<String, Object> responseData = new HashMap<>();

        responseData.put("subCategories", subCategories);

        SuccessResponse successResponse = new SuccessResponse(responseData);

        successResponse.setMessage("Successfully fetch sub categories");
        successResponse.setStatus("Success");
        successResponse.setCode(HttpStatus.OK.value());

        return ResponseEntity.status(HttpStatus.OK).body(successResponse);
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper> store(@Valid @ModelAttribute SubCategoryRequest subCategoryRequest)
    {
        try{
            SubCategory subCategory = subCategoryService.store(subCategoryRequest);

            Map<String, Object> responseData = new HashMap<>();

            responseData.put("id", subCategory.getId());
            responseData.put("name", subCategory.getName());
            responseData.put("image", subCategory.getImage());
            responseData.put("status", subCategory.getStatus());
            responseData.put("createdAt", subCategory.getCreatedAt());
            responseData.put("updatedAt", subCategory.getUpdatedAt());


            ResponseWrapper responseWrapper = ResponseWrapper.createSuccessResponse(responseData, "Store Successful","success", HttpStatus.CREATED.value());

            return ResponseEntity.status(HttpStatus.CREATED).body(responseWrapper);

        }catch (Exception e){

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("error", e.getMessage());

            ResponseWrapper errorResponse = ResponseWrapper.error(responseData, "failed", "error", HttpStatus.INTERNAL_SERVER_ERROR.value());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }

    }

    @GetMapping("{id}")
    public ResponseEntity<ResponseWrapper> edit(@PathVariable("id") Integer subCategoryId)
    {
        try{

            SubCategory subCategory = subCategoryService.edit(subCategoryId);

            Map<String, Object> responseData = new HashMap<>();

            responseData.put("id", subCategory.getId());
            responseData.put("name", subCategory.getName());
            responseData.put("image", subCategory.getImage());
            responseData.put("status", subCategory.getStatus());
            responseData.put("createdAt", subCategory.getCreatedAt());
            responseData.put("updatedAt", subCategory.getUpdatedAt());

            ResponseWrapper successResponse = ResponseWrapper.createSuccessResponse(responseData, "fetch successful", "Success", HttpStatus.OK.value());

            return ResponseEntity.status(HttpStatus.OK).body(successResponse);

        } catch (ResourceNotFoundExcepation e){

            Map<String, Object> responseData = new HashMap<>();

            responseData.put("error", e.getMessage());

            ResponseWrapper errorResponse = ResponseWrapper.error(responseData, "failed", "error", 400);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

        } catch (Exception e){

            Map<String, Object> responseData = new HashMap<>();

            responseData.put("error", e.getMessage());

            ResponseWrapper errorResponse = ResponseWrapper.error(responseData, "failed", "error", 500);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<ResponseWrapper> update(@PathVariable("id") @Valid @ModelAttribute Integer subCategoryId, SubCategoryRequest subCategoryRequest)
    {
        try{

            SubCategory subCategory = subCategoryService.update(subCategoryId, subCategoryRequest);

            Map<String, Object> updateResponseData = new HashMap<>();


            updateResponseData.put("id", subCategory.getId());
            updateResponseData.put("name", subCategory.getName());
            updateResponseData.put("image", subCategory.getImage());
            updateResponseData.put("status", subCategory.getStatus());
            updateResponseData.put("createdAt", subCategory.getCreatedAt());
            updateResponseData.put("updatedAt", subCategory.getUpdatedAt());


            ResponseWrapper responseWrapper = ResponseWrapper.createSuccessResponse(updateResponseData, "update Successful","success", HttpStatus.OK.value());

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

    @DeleteMapping("{id}")
    public ResponseEntity<ResponseWrapper> delete(@PathVariable("id") Integer subCategoryId)
    {
        try{

            subCategoryService.delete(subCategoryId);

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
