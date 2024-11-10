package com.nyasha.drone.backend.schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<DroneSchedule, Integer> {
    List<DroneSchedule> findByStartTimeBefore(LocalDateTime currentDate);
    //List<DroneSchedule> findByStartTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
    //List<DroneSchedule> findByStartTimeBeforeAndEndTimeAfter(LocalDateTime currentTime);
}
