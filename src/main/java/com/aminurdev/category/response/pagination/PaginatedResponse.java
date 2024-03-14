package com.aminurdev.category.response.pagination;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedResponse<T> {

    private List<T> data;
    private Links links;
    private Meta meta;
    private String message;
}
