package com.aminurdev.category.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private Map<String, Object> errors;
    private String message;
    private Integer status;

    public ErrorResponse(Map<String, Object> errors) {
        this.errors = errors;
    }
}
