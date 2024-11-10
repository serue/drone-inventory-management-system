package com.nyasha.drone.auth;

import com.nyasha.drone.role.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateRequest {
    private Integer id;
    @NotEmpty(message = "First name is cannot be empty")
    @NotBlank(message = "First name requires more than space characters")
    private String firstName;
    @NotEmpty(message = "Last name is mandatory")
    @NotBlank(message = "Last name requires characters not spaces")
    private String lastName;
    private LocalDate dateOfBirth;
    private boolean enabled;
    private boolean accountLocked;
    private List<Integer> roles;
}
