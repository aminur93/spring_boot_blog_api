package com.aminurdev.category.response.pagination;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Links {

    private String first;
    private String last;
    private String prev;
    private String next;
}
