package com.nyasha.drone.role;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/groups")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/list")
    public String getRoles(Model model) {
        List<Role> roles = roleService.findAll();
        model.addAttribute("roles", roles);
        return "roles/index";
    }
    @GetMapping("/details/{id}")
    public String details(@PathVariable int id,  Model model) {
        Role role = roleService.findById(id);
        model.addAttribute("role", role);
        return "roles/detail";
    }

    @GetMapping("/new")
    public String newRole(Model model) {
        model.addAttribute("roleRequest", new RoleRequest());
        return "roles/create";
    }

    @PostMapping("/save")
    public String saveRole(@ModelAttribute("role") @Valid RoleRequest request, BindingResult bindingResult) {
        roleService.create(request);
        return "redirect:/admin/groups/list";
    }
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id, Model model) {
        Role role = roleService.findById(id);
        model.addAttribute("role", role);
        return "roles/delete";
    }
    @PostMapping("/delete/{id}")
    public String deleteRole(@PathVariable int id) {
        roleService.delete(id);
        return "redirect:/admin/groups/list";
    }
    @GetMapping("/edit/{id}")
    public String editRole(@PathVariable int id, RoleRequest request, Model model) {
        request = roleService.getRole(id, request);
        model.addAttribute("roleRequest", request);
        return "roles/edit";
    }
    @PostMapping("/edit/{id}")
    public String editRole(@PathVariable int id, @ModelAttribute("roleRequest") RoleRequest request, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("roleRequest", request);
            return "roles/edit";
        }
        roleService.update(id, request);
        return "redirect:/admin/groups/list";
    }
}
