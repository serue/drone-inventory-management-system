package com.nyasha.drone.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationRequest {
    @NotEmpty(message = "Email is empty")
    @NotBlank(message = "Email is blank")
    @Email(message = "Email is not well formatted")
    private String email;
    @NotEmpty(message = "Password is empty")
    @NotBlank(message = "Password is blank")
    @Size(min = 8, message = "Password should have at least 8 characters")
    private String password;
}
