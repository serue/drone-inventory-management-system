package com.nyasha.drone.backend.spares;


import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data

public class SpareRequest {
    private int id;
    @NotBlank(message="Part name cannot be empty")
    @NotEmpty(message="Part name cannot be blank")
    private String name;
    @NotEmpty(message = "Part number cannot be empty")
    @NotBlank(message = "Prt number cannot be blank")
    private String partNumber;
    @NotBlank(message = "Description cannot be blank")
    @NotEmpty(message = "Description cannot be empty")
    private String description;
    @Digits(integer = 10, fraction = 0, message = "Only digits allowed, up to 10 digits")
    @PositiveOrZero(message = "Only positive number is allowed")
    private Integer quantity;

}
