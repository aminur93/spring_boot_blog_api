package com.aminurdev.category.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlogSubCategoryRequest {

    private Integer id;
    private Integer blog_id;
    private Integer sub_category_id;
}
