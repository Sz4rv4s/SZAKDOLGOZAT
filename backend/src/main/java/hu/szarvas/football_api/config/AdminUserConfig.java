package hu.szarvas.football_api.config;

import hu.szarvas.football_api.model.User;
import hu.szarvas.football_api.repository.UserRepository;
import hu.szarvas.football_api.service.SequenceGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class AdminUserConfig {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SequenceGeneratorService sequenceGenerator;

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @Bean
    public CommandLineRunner initAdminUser() {
        return args -> {
            if (!userRepository.existsByUsername(adminUsername)) {
                User admin = User.builder()
                        .id(sequenceGenerator.generateSequence(User.SEQUENCE_NAME))
                        .username(adminUsername)
                        .email(adminEmail)
                        .password(passwordEncoder.encode(adminPassword))
                        .roles(Set.of("ADMIN", "USER"))
                        .refreshToken(null)
                        .build();

                userRepository.save(admin);
                log.info("Admin user created: {}", adminUsername);
            } else {
                log.info("Admin user already exists: {}", adminUsername);
            }
        };
    }
}
