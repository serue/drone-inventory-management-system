package com.nyasha.drone.role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class RoleRequest {
    private int id;
    @NotEmpty(message = "The role cannot be empty")
    @NotBlank(message = "Role name must not be blank")
    private String name;
}
