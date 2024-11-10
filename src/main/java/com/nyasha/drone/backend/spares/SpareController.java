package com.nyasha.drone.backend.spares;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/drone/spares")
@RequiredArgsConstructor
@Slf4j
public class SpareController {
    private final SpareService spareService;

    @GetMapping
    public String getSpares(Model model) {
        List<Spare> spares = spareService.findAll();
        model.addAttribute("spares", spares);
        return "spares/index";
    }
    @GetMapping("/details/{id}")
    public String getSpare(@PathVariable int id, Model model) {
        Spare spare = spareService.getSpare(id);
        model.addAttribute("spare", spare);
        return "spares/detail";
    }

    @GetMapping("/create")
    public String newSpare(Model model) {
        model.addAttribute("spareRequest", new SpareRequest());
        return "spares/create";
    }
    @PostMapping("/create")
    public String saveSpare(@ModelAttribute("spareRequest") @Valid SpareRequest request, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            log.error(bindingResult.getAllErrors().toString());
            return "spares/create";
        }
        spareService.create(request);
        return "redirect:/drone/spares";
    }

    @GetMapping("/edit/{id}")
    public String editSpare(@PathVariable int id, Model model) {
        SpareRequest spareRequest = spareService.getSpareRequest(id);
        model.addAttribute("spareRequest", spareRequest);
        return "spares/edit";
    }

    @PostMapping("/edit/{id}")
    public String saveChanges(@ModelAttribute("spareRequest") @Valid SpareRequest request, @PathVariable int id, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            log.error(bindingResult.getAllErrors().toString());
            return "spares/edit";
        }
        spareService.update(request, id);
        return "redirect:/drone/spares";
    }
    @GetMapping("/delete/{id}")
    public String deleteSpare(@PathVariable int id, Model model){
        Spare spare = spareService.getSpare(id);
        model.addAttribute("spare", spare);
        return "spares/delete";
    }
    @PostMapping("/delete/{id}")
    public String deleteSpare(@PathVariable int id) {
        spareService.delete(id);
        return "redirect:/drone/spares";
    }
}
