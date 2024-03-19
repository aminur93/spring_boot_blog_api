package com.aminurdev.category.domain.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class TagRequest {

    private Integer id;

    @NotNull(message = "Name field is required")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    private String name;

    private String slug;

    @Min(value = 0)
    @Max(value = 1)
    private int status;
}
