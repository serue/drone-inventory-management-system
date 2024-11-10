package com.nyasha.drone.backend.schedule;

import com.nyasha.drone.backend.drones.Drone;
import com.nyasha.drone.backend.shared.State;
import com.nyasha.drone.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class DroneSchedule {
    @Id
    @GeneratedValue
    private Integer id;
    @Enumerated(EnumType.STRING)
    private State state;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String description;
    @ManyToOne
    @JoinColumn(name = "drone_id", nullable = false)
    private Drone drone;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User operatedBy;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;
    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;
}
