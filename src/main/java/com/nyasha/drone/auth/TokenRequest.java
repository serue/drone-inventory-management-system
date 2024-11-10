package com.nyasha.drone.auth;

import jakarta.validation.constraints.NotBlank;


public record TokenRequest(
        @NotBlank(message = "The token cannot be empty")
        @NotBlank(message = "The token cannot be a space")
        String token
) {
}
