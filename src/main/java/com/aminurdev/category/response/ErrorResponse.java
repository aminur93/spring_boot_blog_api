package com.aminurdev.category.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private Map<String, String> errors;
    private String message;
    private String status;

    public ErrorResponse(Map<String, String> errors) {
        this.errors = errors;
    }
}
