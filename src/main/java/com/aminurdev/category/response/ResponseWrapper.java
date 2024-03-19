package com.aminurdev.category.response;

import com.aminurdev.category.domain.entity.SubCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseWrapper {

    private Map<String, Object> errors;

    private Map<String,Object> data;

    private String message;

    private String status;

    private Integer code;

    public static ResponseWrapper createSuccessResponse(Map<String, Object> responseData, String message, String status, Integer code) {


        return new ResponseWrapper(null, responseData, message, status, code);
    }

    public static ResponseWrapper error(Map<String, Object> errors, String message, String status, Integer code) {

        return new ResponseWrapper(errors, null, message, status, code);
    }
}
