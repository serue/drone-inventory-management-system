package com.nyasha.drone.backend.schedule;

import com.nyasha.drone.backend.drones.Drone;
import com.nyasha.drone.backend.drones.DroneRepository;
import com.nyasha.drone.user.User;
import com.nyasha.drone.user.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final DroneRepository droneRepository;
    private final UserRepository userRepository;

    public List<DroneSchedule> findAll() {
        return scheduleRepository.findAll();
    }

    public void delete(Integer id) {
        scheduleRepository.deleteById(id);
    }

    public DroneSchedule findById(Integer id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
    }

    public List<User> allUsers() {
        return userRepository.findAll();
    }
    public void update(Integer id, @Valid ScheduleRequest request) {
        DroneSchedule schedule = findById(id);
        schedule.setState(request.getState());
        schedule.setStartTime(request.getStartTime());
        schedule.setEndTime(request.getEndTime());
        schedule.setDescription(request.getDescription());
        schedule.setDrone(request.getDrone());
        schedule.setOperatedBy(request.getOperatedBy());
        scheduleRepository.save(schedule);

    }

    public ScheduleRequest getScheduleRequest(Integer id, ScheduleRequest request) {
        DroneSchedule schedule = findById(id);
        request.setId(schedule.getId());
        request.setState(schedule.getState());
        request.setDescription(schedule.getDescription());
        request.setStartTime(schedule.getStartTime());
        request.setEndTime(schedule.getEndTime());
        request.setDrone(schedule.getDrone());
        request.setOperatedBy(schedule.getOperatedBy());
        return request;

    }

    public void create(@Valid ScheduleRequest request) {
        log.info("Creating new schedule: {}", request.getDrone());
        //Drone drone = droneRepository.findBySerialNumber(request.getSerialNumber()).orElseThrow(() -> new RuntimeException("Drone not found"+ request.getSerialNumber()));
        DroneSchedule schedule = DroneSchedule.builder()
                .state(request.getState())
                .description(request.getDescription())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .drone(request.getDrone())
                .operatedBy(request.getOperatedBy())
                .build();
        scheduleRepository.save(schedule);
    }

    public List<DroneSchedule> getPastSchedules() {
        LocalDateTime now = LocalDateTime.now();
        return scheduleRepository.findByStartTimeBefore(now);
    }
//    public List<DroneSchedule> getInTimeSchedules() {
//        LocalDateTime now = LocalDateTime.now();
//        return  scheduleRepository.findByStartTimeBeforeAndEndTimeAfter(now);
//    }

}
