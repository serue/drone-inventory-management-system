package com.nyasha.drone.dashboard;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/home")
@Slf4j
public class DashboardController {
    @GetMapping
    public String home() {

       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null) {
            // Log the name and authorities of the authenticated user
            String username = authentication.getName(); // Get the username
            log.info("Logged in user: {}", username); // Log the username

            // Log the roles or authorities of the user
            authentication.getAuthorities().forEach(grantedAuthority -> 
                log.info("User has authority: {}", grantedAuthority.getAuthority())
            );
        } else {
            log.warn("No authentication found.");
        }

        return "home";
    }
}
