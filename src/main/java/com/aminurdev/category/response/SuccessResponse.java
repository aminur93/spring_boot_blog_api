package com.aminurdev.category.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SuccessResponse {

    private Map<String,Object> data;
    private String message;
    private String status;
    private Integer code;

    public SuccessResponse(Map<String, Object> data)
    {
        this.data = data;
    }

    public SuccessResponse createSuccessResponse(String successMessage)
    {
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("error", successMessage);
        return new SuccessResponse(responseData);
    }
}
