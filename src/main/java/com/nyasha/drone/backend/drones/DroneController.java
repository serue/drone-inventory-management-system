package com.nyasha.drone.backend.drones;

import com.nyasha.drone.backend.category.Category;
import com.nyasha.drone.backend.category.CategoryService;
import com.nyasha.drone.backend.shared.State;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/drones")
@RequiredArgsConstructor
public class DroneController {
    private final DroneService droneService;
    private final CategoryService categoryService;

    @GetMapping
    public String getDrones(Model model) {
        List<Drone> drones = droneService.findAll();
        model.addAttribute("drones", drones);
        return "drones/index";
    }
    @GetMapping("/details/{id}")
    public String getDroneDetails(Model model, @PathVariable int id) {
        Drone drone = droneService.getDrone(id);
        model.addAttribute("drone", drone);
        return "drones/detail";
    }
    @GetMapping("/create")
    public String newDrone(Model model) {
        List<Category> categories = categoryService.findAll();
        model.addAttribute("drone", new DroneRequest());
        model.addAttribute("models", com.nyasha.drone.backend.drones.Model.values());
        model.addAttribute("states", State.values());
        model.addAttribute("categories", categories);
        return "drones/create";
    }

    @PostMapping("/create")
    public String saveDrone(@ModelAttribute("drone") @Valid DroneRequest request, BindingResult bindingResult) {
        droneService.save(request);
        return "redirect:/drones";
    }
    @GetMapping("/edit/{id}")
    public String editDrone(Model model, @PathVariable int id) {
        DroneRequest droneRequest = droneService.getDroneRequest(id);
        model.addAttribute("drone", droneRequest);
        model.addAttribute("models", com.nyasha.drone.backend.drones.Model.values());
        model.addAttribute("states", State.values());
        model.addAttribute("categories", categoryService.findAll());
        return "drones/edit";
    }
    @PostMapping("/edit/{id}")
    public String saveChanges(@ModelAttribute("droneRequest") @Valid DroneRequest request, @PathVariable int id, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("drone", request);
            model.addAttribute("models", com.nyasha.drone.backend.drones.Model.values());
            model.addAttribute("states", State.values());
            model.addAttribute("categories", categoryService.findAll());
            return "drones/edit";
        }
        droneService.update(request, id);
        return "redirect:/drones";
    }
    @GetMapping("/delete/{id}")
    public String deleteDrone(@PathVariable int id, Model model) {
        var drone  = droneService.getDrone(id);
        model.addAttribute("drone", drone);
        return "drones/delete";
    }
    @PostMapping("/delete/{id}")
    public String deleteChanges(@PathVariable int id, Model model) {
        droneService.delete(id);
        return "redirect:/drones";
    }
}
