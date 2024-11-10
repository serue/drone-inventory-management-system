package com.nyasha.drone.backend.order;

import com.nyasha.drone.backend.shared.CategoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class OrderRequest {
   @NotBlank(message = "Select category please")
   @NotEmpty(message = "Category is empty")
    private CategoryType category;
}
