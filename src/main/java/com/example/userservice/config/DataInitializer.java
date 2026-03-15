package com.example.userservice.config;

import com.example.userservice.entity.User;
import com.example.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    @Bean
    CommandLineRunner initData(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() == 0) {
                log.info("Seeding sample data...");

                userRepository.save(User.builder()
                        .username("admin")
                        .email("admin@example.com")
                        .password(passwordEncoder.encode("Admin@1234"))
                        .firstName("System")
                        .lastName("Admin")
                        .role(User.Role.ROLE_ADMIN)
                        .enabled(true)
                        .build());

                userRepository.save(User.builder()
                        .username("john.doe")
                        .email("john.doe@example.com")
                        .password(passwordEncoder.encode("Password@123"))
                        .firstName("John")
                        .lastName("Doe")
                        .phoneNumber("+911234567890")
                        .role(User.Role.ROLE_USER)
                        .enabled(true)
                        .build());

                userRepository.save(User.builder()
                        .username("jane.smith")
                        .email("jane.smith@example.com")
                        .password(passwordEncoder.encode("Password@123"))
                        .firstName("Jane")
                        .lastName("Smith")
                        .phoneNumber("+919876543210")
                        .role(User.Role.ROLE_MODERATOR)
                        .enabled(true)
                        .build());

                userRepository.save(User.builder()
                        .username("raj.kumar")
                        .email("raj.kumar@example.com")
                        .password(passwordEncoder.encode("Password@123"))
                        .firstName("Raj")
                        .lastName("Kumar")
                        .phoneNumber("+919988776655")
                        .role(User.Role.ROLE_USER)
                        .enabled(true)
                        .build());

                log.info("Sample data seeded: 4 users created");
            }
        };
    }
}
