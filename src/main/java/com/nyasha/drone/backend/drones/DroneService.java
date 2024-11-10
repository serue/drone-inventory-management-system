package com.nyasha.drone.backend.drones;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class DroneService {
    private final DroneRepository droneRepository;

    public List<Drone> findAll() {
        return droneRepository.findAll();
    }

    public Drone getDrone(int id) {
        return droneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Drone not found"));
    }

    public void save(@Valid DroneRequest request) {
        Drone drone  = Drone.builder()
                .serialNumber(request.getSerialNumber())
                .batteryCapacity(request.getBatteryCapacity())
                .color(request.getColor())
                .state(request.getState())
                .weight(request.getWeight())
                .model(request.getModel())
                .category(request.getCategory())
                .build();
        droneRepository.save(drone);
        log.info("Saving new drone: {}", drone);
    }

    public DroneRequest getDroneRequest(int id) {
        Drone drone = getDrone(id);
        DroneRequest request = new DroneRequest();
        request.setSerialNumber(drone.getSerialNumber());
        request.setBatteryCapacity(drone.getBatteryCapacity());
        request.setColor(drone.getColor());
        request.setState(drone.getState());
        request.setWeight(drone.getWeight());
        request.setModel(drone.getModel());
        request.setCategory(drone.getCategory());
        return request;
    }

    public void update(@Valid DroneRequest request, int id) {
        Drone drone = getDrone(id);
        drone.setColor(request.getColor());
        drone.setBatteryCapacity(request.getBatteryCapacity());
        drone.setModel(request.getModel());
        drone.setSerialNumber(request.getSerialNumber());
        drone.setWeight(request.getWeight());
        drone.setState(request.getState());
        drone.setCategory(request.getCategory());
        droneRepository.save(drone);
    }

    public void delete(int id) {
        droneRepository.deleteById(id);
    }

    public Drone findBySerialNumber(String serialNumber) {
        return droneRepository.findBySerialNumber(serialNumber)
                .orElseThrow(()-> new RuntimeException("Drone not found"));
    }
}
