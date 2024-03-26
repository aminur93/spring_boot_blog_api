package com.aminurdev.category.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
@Entity
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", length = 55)
    private String name;

    @Column(name = "slug", length = 55)
    private String slug;

    @Column(name = "status", length = 4)
    private int status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate()
    {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate()
    {
        updatedAt = LocalDateTime.now();
    }

    public String generateSlug(String name) {
        if (StringUtils.hasText(name)) {
            // Convert name to lowercase and replace spaces with dashes
            return name.toLowerCase().replaceAll("\\s+", "-");
        }
        // Handle the case when name is empty or null
        return ""; // or throw an exception, depending on your requirements
    }

    @ManyToMany(mappedBy = "tags")
    @JsonBackReference
    private Set<Blog> blog = new HashSet<>();
}
