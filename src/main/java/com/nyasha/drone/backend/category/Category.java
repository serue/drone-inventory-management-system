package com.nyasha.drone.backend.category;

import com.nyasha.drone.backend.drones.Drone;
import com.nyasha.drone.backend.shared.CategoryType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Category {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(nullable = false, unique = true)
    private String name;
    private String description;
    @Enumerated(EnumType.STRING)
    private CategoryType categoryType;
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL) // References the 'category' field in the Drone entity
    private List<Drone> drones;
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;
    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;
}
