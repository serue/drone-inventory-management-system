package com.nyasha.drone;

import com.nyasha.drone.role.Role;
import com.nyasha.drone.role.RoleRepository;
import com.nyasha.drone.user.User;
import com.nyasha.drone.user.UserRepository;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
@EnableScheduling
public class DroneApplication {

	public static void main(String[] args) {
		SpringApplication.run(DroneApplication.class, args);
	}
	@Bean
	public CommandLineRunner commandLineRunner(RoleRepository roleRepository, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
		return args -> {
			if (roleRepository.findByName("USER").isEmpty()) {
				roleRepository.save(
						Role.builder().name("USER").build()
				);
			}
			if (roleRepository.findByName("ADMIN").isEmpty()) {
                roleRepository.save(Role.builder().name("ADMIN").build());
            }

            // Bootstrap admin user if not exists
            if (userRepository.findByEmail("admin@beymo.com").isEmpty()) {
                Role adminRole = roleRepository.findByName("ADMIN").get();

                User adminUser = User.builder()
                        .firstName("admin")
						.lastName("admin")
						.email("admin@beymo.com")
						.password(passwordEncoder.encode("admin123"))
						.accountLocked(false)
						.enabled(true)
						.roles(List.of(adminRole))
                        .build();

                userRepository.save(adminUser);
            }
		};
	}

}
