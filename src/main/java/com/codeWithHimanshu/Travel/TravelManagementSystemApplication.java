package com.codeWithHimanshu.Travel;

import com.codeWithHimanshu.Travel.entity.User;
import com.codeWithHimanshu.Travel.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class TravelManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(TravelManagementSystemApplication.class, args);
    }

    @Bean
    CommandLineRunner init(UserRepository userRepository, BCryptPasswordEncoder encoder) {
        return args -> {
            if (userRepository.count() == 0) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(encoder.encode("admin123"));
                admin.setRole("ROLE_ADMIN");
                userRepository.save(admin);
                System.out.println("Default admin created -> username: admin  password: admin123");
            }
        };
    }
}
