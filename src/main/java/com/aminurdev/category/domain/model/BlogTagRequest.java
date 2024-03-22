package com.aminurdev.category.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlogTagRequest {

    private Integer id;

    private Integer blogId;

    private Integer tagId;
}
