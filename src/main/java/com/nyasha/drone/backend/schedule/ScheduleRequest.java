package com.nyasha.drone.backend.schedule;

import com.nyasha.drone.backend.drones.Drone;
import com.nyasha.drone.backend.shared.State;
import com.nyasha.drone.user.User;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduleRequest {

    private Integer id;
    private String serialNumber;

    @NotNull(message = "State is required")
    private State state;

    @NotNull(message = "Start time is required")
    @Future(message = "Start time must be in the future")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    @Future(message = "End time must be in the future")
    private LocalDateTime endTime;

    @NotEmpty(message = "Description cannot be empty")
    @NotBlank(message = "Description cannot be blank")
    private String description;
    @NotNull(message = "Drone is required")
    private Drone drone;

    @NotNull(message = "User is required")
    private User operatedBy;

    // Custom validation for ensuring end time is after start time
    public boolean isValidSchedule() {
        return startTime != null && endTime != null && endTime.isAfter(startTime);
    }
}
