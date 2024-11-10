package com.nyasha.drone.backend.schedule;

import com.nyasha.drone.backend.drones.Drone;
import com.nyasha.drone.backend.drones.DroneRepository;
import com.nyasha.drone.backend.shared.State;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DroneScheduler {
    private final ScheduleService scheduleService;
    private final DroneRepository droneRepository;
    @Scheduled(fixedRate = 300000)
    public void scheduleDroneMaintenanceCheck() {
        List<DroneSchedule> pastSchedules = scheduleService.getPastSchedules();
       // List<DroneSchedule> inTimeSchedules = scheduleService.getInTimeSchedules();
        log.info("List of future schedules"+pastSchedules.toString());
        //log.info("List of current schedules"+inTimeSchedules.toString());
        pastSchedules.forEach(schedule -> {
            // Handle the past schedules here (e.g., send notifications, update state)
            log.info("The list of drones in maintained{}", schedule.getDrone().getSerialNumber());
            if(schedule.getEndTime().isBefore(LocalDateTime.now())){
                log.info("The list of drones in maintained end time");
                Drone drone = schedule.getDrone();
                drone.setState(State.FLYING);
                droneRepository.save(drone);
            }
        });
    }
}
