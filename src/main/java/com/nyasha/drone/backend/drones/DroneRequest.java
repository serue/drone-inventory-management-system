package com.nyasha.drone.backend.drones;

import com.nyasha.drone.backend.category.Category;
import com.nyasha.drone.backend.shared.State;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class DroneRequest {
    private int id;
    @NotBlank(message = "The serial number cannot be blank")
    @NotEmpty(message = "The serial number cannot be empty")
    private String serialNumber;
    @NotNull(message = "The model is required")
    private Model model;
    @NotBlank(message = "The color cannot be blank")
    @NotEmpty(message = "The color cannot be empty")
    private String color;

    @Digits(message = "The weight should be numbers", integer = 10, fraction = 0)
    @Positive(message = "the weight should be positive")
    private double weight;
    @Digits(message = "The capacity should be numbers", integer = 10, fraction = 0)
    @Positive(message = "the capacity should be positive")
    private double batteryCapacity;
    @NotNull(message = "The state is required")
    private State state;
    private Category category;
}
