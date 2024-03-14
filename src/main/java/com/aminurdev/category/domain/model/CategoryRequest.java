package com.aminurdev.category.domain.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class CategoryRequest {

    private Integer id;

    @NotBlank(message = "Name must not be blank")
    @NotNull(message = "Name field is required")
    private String name;

    @NotNull(message = "Image field is required")
    private MultipartFile image;

    @NotNull(message = "Status field is required")
    private boolean status;
}
