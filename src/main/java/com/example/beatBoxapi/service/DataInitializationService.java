package com.example.beatBoxapi.service;


import com.example.beatBoxapi.document.User;
import com.example.beatBoxapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataInitializationService implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void run(String... args) throws Exception {
        createDefaultAdminUser();
    }

    private void createDefaultAdminUser() {
        if(!userRepository.existsByEmail("admin@beatbox.com"))
        {
            // Must use passwordEncoder here!
            String encodedPassword = passwordEncoder.encode("admin123");

            User adminUser = User.builder()
                    .email("admin@beatbox.com")
                    .password(encodedPassword) // This line is crucial
                    .role(User.Role.ADMIN)
                    .build();

            userRepository.save(adminUser);
            log.info("Default admin user created.");
        }
        // ...
    }


}
