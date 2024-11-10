package com.nyasha.drone.auth;
import java.util.List;

import com.nyasha.drone.role.Role;
import com.nyasha.drone.role.RoleService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.sql.Update;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.nyasha.drone.user.User;

@Controller
@RequiredArgsConstructor
@RequestMapping("/drone/auth")
@Slf4j
public class AuthenticationController {
    private final AuthenticationService service;
    private final RoleService roleService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registrationRequest", new RegistrationRequest());
        return "auth/register"; // This points to the registration.html Thymeleaf template
    }
    @PostMapping(value = "/register") // consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String register(@ModelAttribute("registrationRequest") @Valid RegistrationRequest request, BindingResult bindingResult, Model model) throws MessagingException {
        if(bindingResult.hasErrors()){
            model.addAttribute("registrationRequest", request);
            return "auth/register";
        }
        service.register(request);
        return "redirect:/drone/auth/verify-account";
    }


    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {
        model.addAttribute("loginRequest", new AuthenticationRequest());
        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
            log.error("Invalid username or password");
        }
        return "auth/login"; // This should point to your login view
    }

//    @PostMapping("/login")
//    public String authenticate(@ModelAttribute("loginRequest") @Valid AuthenticationRequest request, BindingResult, Model model) {
//        log.info("Processing login request for email: {}", request.getEmail());
//        if (bindingResult.hasErrors()) {
//            log.error("Binding errors occurred: {}", bindingResult.getAllErrors());
//            return "auth/login";
//        }
//        service.authenticate(request);
//        return "redirect:/home";
//
//    }
    @GetMapping("/verify-account")
    public String verifyAccount(Model model) {
        model.addAttribute("verifyRequest", new TokenRequest(""));
        return "auth/verify-account";
    }

    @PostMapping("/activate-account")
    public String confirm(
            @ModelAttribute("verifyRequest") @Valid TokenRequest request, BindingResult bindingResult, Model model
    ) throws MessagingException {
        if(bindingResult.hasErrors()){
            model.addAttribute("verifyRequest", request);
            return "auth/verify-account";
        }
        service.activateAccount(request);
        return "redirect:/drone/auth/login";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/users")
    public String getUsers(Model model){
        List<User> users = service.getUsers();
        model.addAttribute("users", users);
        return "auth/index";
    }
    @GetMapping("/change-password")
    public String changePassword(Model model){
        model.addAttribute("authenticationRequest", new AuthenticationRequest());
        return "auth/change-password";
    }
    @PostMapping("/change-password")
    public String changePassword(@ModelAttribute("authenticationRequest") @Valid AuthenticationRequest request,  BindingResult bindingResult,  Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("authenticationRequest", request);
            return "auth/change-password";
        }
        service.changePassword(request);
        return "redirect:/home";
    }
    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable int id, UpdateRequest request) {
        request = service.getUser(id, request);
        List<Role> roles = roleService.findAll();
        model.addAttribute("registrationRequest", request);
        model.addAttribute("roles", roles);
        return "auth/edit";
    }
    @PostMapping("/edit/{id}")
    public String updateUser(@ModelAttribute("updateRequest") @Valid UpdateRequest request, @PathVariable int id, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()){
            model.addAttribute("updateRequest", request);
            return "auth/edit";
        }
        service.updateUser(id,request);
        return "redirect:/drone/auth/users";
    }
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id, Model model){
        User user = service.getUserDetails(id);
        model.addAttribute("user", user);
        return "auth/delete";
    }
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        service.delete(id);
        return "redirect:/drone/auth/users";
    }
    @GetMapping("/account-info/{id}")
    public String accountDetails(@PathVariable int id, Model model){
        User user = service.getUserDetails(id);
        model.addAttribute("user", user);
        return "auth/account-info";
    }

    @GetMapping("/profile")
    public String profileDetails(Model model){
        User profile = service.getProfile();
        model.addAttribute("profile", profile);
        return "auth/profile";
    }
}
