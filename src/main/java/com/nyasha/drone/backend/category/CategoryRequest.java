package com.nyasha.drone.backend.category;

import com.nyasha.drone.backend.shared.CategoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoryRequest {
    private Integer id;
    @NotBlank(message = "The category name is required")
    @NotEmpty(message = "The category name cannot be empty")
    private String name;
    @NotBlank(message = "The description  is required")
    @NotEmpty(message = "The description cannot be empty")
    private String description;
    @NotNull(message = "The category type is required")
    private CategoryType categoryType;

}
