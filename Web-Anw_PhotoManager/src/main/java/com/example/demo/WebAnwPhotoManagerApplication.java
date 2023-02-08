package com.example.demo;

import com.example.demo.entity.ERole;
import com.example.demo.entity.Role;
import com.example.demo.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class WebAnwPhotoManagerApplication {

    private static Logger logger = LoggerFactory.getLogger(WebAnwPhotoManagerApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(WebAnwPhotoManagerApplication.class, args);
    }

    @Bean
    public CommandLineRunner demoData(RoleRepository repo) {
        List<Role> roles = repo.findAll();
        if (roles.size() == 0)
            return args -> {
                repo.save(new Role(ERole.USER));
                repo.save(new Role(ERole.ADMIN));
            };
        return args -> {
          logger.info("RUNNING....");
        };
    }
}
