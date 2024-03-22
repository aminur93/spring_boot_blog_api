package com.aminurdev.category.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlogRequest {

    private Integer id;

    @NotNull(message = " Author id field is required")
    private Integer author_id;

    @NotNull(message = "Category id field is required")
    private Integer category_id;

    @NotNull(message = "Title field is required")
    private String title;

    @NotNull(message = "Slogan field is required")
    private String slogan;

    private String slug;

    @NotNull(message = "Description field is required")
    private String description;

    @NotNull(message = "Date must not be null")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private MultipartFile image;

    @Min(value = 0)
    @Max(value = 1)
    private int status;

    private List<Integer> tagIds;

    private List<Integer> subCategoryIds;
}
