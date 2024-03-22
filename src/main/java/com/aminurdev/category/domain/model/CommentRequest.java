package com.aminurdev.category.domain.model;

import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {

    private Integer id;
    private Integer user_id;
    private Integer blog_id;
    private String comment;
}
