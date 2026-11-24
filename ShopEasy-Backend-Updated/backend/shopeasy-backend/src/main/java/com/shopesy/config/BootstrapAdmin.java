package com.shopesy.config;

import com.shopesy.entity.AppUser;
import com.shopesy.entity.Role;
import com.shopesy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BootstrapAdmin {
    @Bean
    CommandLineRunner createDefaultAdmin(UserRepository users, PasswordEncoder encoder,
                                         @Value("${app.admin.name:ShopEasy Admin}") String name,
                                         @Value("${app.admin.email:admin@shopeasy.com}") String email,
                                         @Value("${app.admin.password:Admin@12345}") String password) {
        return args -> {
            if (!users.existsByEmail(email)) {
                users.save(new AppUser(name, email, encoder.encode(password), Role.ADMIN));
                System.out.println("Default ShopEasy admin created: " + email);
                System.out.println("Change the default admin password after first login.");
            }
        };
    }
}
