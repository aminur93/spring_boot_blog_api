package com.aminurdev.category.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SuccessResponse {

    private Map<String, Object> data;
    private String message;
    private String status;
    private String code;

    public SuccessResponse(Map<String, Object> data)
    {
        this.data = data;
    }
}
