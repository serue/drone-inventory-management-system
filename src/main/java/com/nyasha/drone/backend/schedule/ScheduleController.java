package com.nyasha.drone.backend.schedule;

import com.nyasha.drone.auth.AuthenticationService;
import com.nyasha.drone.backend.drones.Drone;
import com.nyasha.drone.backend.drones.DroneService;
import com.nyasha.drone.backend.shared.State;
import com.nyasha.drone.user.User;
import com.nyasha.drone.user.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/drone/schedules")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final AuthenticationService authenticationService;
    private final DroneService droneService;


    @GetMapping
    public String schedule(Model model) {
//        model.addAttribute("users", authenticationService.getUsers());
//        model.addAttribute("drones", droneService.findAll());
        List<DroneSchedule> schedules = scheduleService.findAll();
        model.addAttribute("schedules", schedules);
        return "schedule/index";
    }
    @GetMapping("/details/{id}")
    public String details(Model model, @PathVariable int id) {
        DroneSchedule schedule = scheduleService.findById(id);
        model.addAttribute("schedule", schedule);
        return "schedule/details";
    }
    @GetMapping("/create/{serialNumber}")
    public String create(Model model, @PathVariable("serialNumber") String serialNumber, ScheduleRequest request) {
        Drone drone = droneService.findBySerialNumber(serialNumber);
        List<Drone> drones = droneService.findAll();
        List<User> users   = scheduleService.allUsers();
        request.setSerialNumber(drone.getSerialNumber());
        model.addAttribute("scheduleRequest", request);
        model.addAttribute("drone", drone);
        model.addAttribute("drones", drones);
        model.addAttribute("states", State.values());
        model.addAttribute("users", users);
        return "schedule/create";
    }
    @PostMapping("/create")
    public String create(Model model,  @Valid ScheduleRequest request, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            //Drone drone = droneService.findBySerialNumber(request.getSerialNumber());
            List<Drone> drones = droneService.findAll();
            List<User> users   = scheduleService.allUsers();
            //request.setSerialNumber(drone.getSerialNumber());
            model.addAttribute("scheduleRequest", request);
            //model.addAttribute("drone", drone);
            model.addAttribute("drones", drones);
            model.addAttribute("states", State.values());
            model.addAttribute("users", users);
            return "schedule/create";
        }
        scheduleService.create(request);
        return "redirect:/drone/schedule/details/" + request.getSerialNumber();
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable Integer id, ScheduleRequest request) {
        List<User> users = authenticationService.getUsers();
        List<Drone> drones = droneService.findAll();
        request = scheduleService.getScheduleRequest(id, request);
        model.addAttribute("users", users);
        model.addAttribute("drones", drones);
        model.addAttribute("states", State.values());
        model.addAttribute("users", users);
        model.addAttribute("schedule", request);
        return "schedule/edit";
    }
    @PostMapping("/edit/{id}")
    public String edit(Model model, @PathVariable Integer id, @Valid ScheduleRequest request, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("schedule", request);
            return "schedule/edit";
        }
        scheduleService.update(id, request);
        return "redirect:/drone/schedule/details/" + id;
    }
    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable Integer id) {
        DroneSchedule schedule = scheduleService.findById(id);
        model.addAttribute("schedule", schedule);
        return "schedule/delete";
    }
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        scheduleService.delete(id);
        return "redirect:/drone/schedule/details/" + id;
    }
}
