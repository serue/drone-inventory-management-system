package com.nyasha.drone.backend.drones;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DroneRepository extends JpaRepository<Drone, Integer> {

    Optional<Drone> findBySerialNumber(String serialNumber);


}
