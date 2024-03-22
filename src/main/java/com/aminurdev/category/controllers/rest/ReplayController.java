package com.aminurdev.category.controllers.rest;

import com.aminurdev.category.domain.entity.Comment;
import com.aminurdev.category.domain.entity.Replay;
import com.aminurdev.category.domain.excepation.ResourceNotFoundExcepation;
import com.aminurdev.category.domain.model.ReplayRequest;
import com.aminurdev.category.response.ResponseWrapper;
import com.aminurdev.category.response.SuccessResponse;
import com.aminurdev.category.response.pagination.PaginatedResponse;
import com.aminurdev.category.service.ReplayService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/replay")
@AllArgsConstructor
public class ReplayController {

    private ReplayService replayService;

    @GetMapping
    public ResponseEntity<PaginatedResponse<Replay>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ){

        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        PaginatedResponse<Replay> paginatedResponse = replayService.index(direction, page, perPage);

        return  ResponseEntity.ok(paginatedResponse);
    }

    @GetMapping("/all-replays")
    public ResponseEntity<SuccessResponse> allReplays()
    {
        List<Replay> replays = replayService.allReplays();

        Map<String, Object> responseData = new HashMap<>();

        responseData.put("replays", replays);

        SuccessResponse successResponse = new SuccessResponse(responseData);

        successResponse.setMessage("Successfully fetch comments");
        successResponse.setStatus("Success");
        successResponse.setCode(HttpStatus.OK.value());

        return ResponseEntity.status(HttpStatus.OK).body(successResponse);
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper> store(@RequestBody ReplayRequest replayRequest)
    {
        try{

            Replay replay = replayService.store(replayRequest);

            Map<String, Object> responseData = new HashMap<>();

            responseData.put("id", replay.getId());
            responseData.put("user_id", replay.getUser_id());
            responseData.put("blog_id", replay.getBlog().getId());
            responseData.put("comment_id", replay.getComment().getId());
            responseData.put("replay", replay.getReplay());
            responseData.put("createdAt", replay.getCreatedAt());
            responseData.put("updatedAt", replay.getUpdatedAt());

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
    public ResponseEntity<ResponseWrapper> delete(@PathVariable("id") Integer replayId)
    {
        try{

            replayService.delete(replayId);

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
