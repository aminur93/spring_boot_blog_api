package com.aminurdev.category.response.pagination;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Meta {

    private int currentPage;
    private int from;
    private int lastPage;
    private String path;
    private int perPage;
    private int to;
    private int total;
}
