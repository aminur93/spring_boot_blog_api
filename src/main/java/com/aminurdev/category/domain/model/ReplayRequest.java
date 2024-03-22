package com.aminurdev.category.domain.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplayRequest {

    private Integer id;
    private Integer user_id;
    private Integer blog_id;
    private Integer comment_id;

    @NotNull(message = "Reply field is required")
    private String replay;
}
