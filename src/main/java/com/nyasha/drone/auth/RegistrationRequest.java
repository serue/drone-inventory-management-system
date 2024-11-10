package com.nyasha.drone.auth;


import com.nyasha.drone.role.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationRequest {
    private Integer id;
    @NotEmpty(message = "First name is cannot be empty")
    @NotBlank(message = "First name requires more than space characters")
    private String firstName;
    @NotEmpty(message = "Last name is mandatory")
    @NotBlank(message = "Last name requires characters not spaces")
    private String lastName;
    @NotEmpty(message = "Email is mandatory")
    @NotBlank(message = "Email requires characters, only spaces are found")
    @Email(message = "Email is not well formatted")
    private String email;
    @NotEmpty(message = "Password name is mandatory")
    @NotBlank(message = "Password is requires characters but only spaces were found")
    @Size(min = 8, message = "Password should have at least 8 characters")
    private String password;
    private LocalDate dateOfBirth;
    private List<Role> roles;

}
