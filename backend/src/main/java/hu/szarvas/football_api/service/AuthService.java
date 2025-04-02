package hu.szarvas.football_api.service;

import hu.szarvas.football_api.dto.request.LoginRequestDTO;
import hu.szarvas.football_api.dto.request.RefreshTokenRequestDTO;
import hu.szarvas.football_api.dto.request.RegisterRequestDTO;
import hu.szarvas.football_api.dto.response.AuthResponseDTO;
import hu.szarvas.football_api.model.User;
import hu.szarvas.football_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenBlacklistService tokenBlacklistService;
    private final SequenceGeneratorService sequenceGenerator;

    public AuthResponseDTO register(RegisterRequestDTO request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        User user = User.builder()
                .id(sequenceGenerator.generateSequence(User.SEQUENCE_NAME))
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of("USER"))
                .build();

        String jwtToken = tokenService.generateAccessToken(user);
        String refreshToken = tokenService.generateRefreshToken();
        user.setRefreshToken(refreshToken);

        userRepository.save(user);

        return AuthResponseDTO.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .user(user.getUsername())
                .userId(user.getId())
                .build();
    }

    public AuthResponseDTO login(LoginRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String jwtToken = tokenService.generateAccessToken(user);
        String refreshToken = tokenService.generateRefreshToken();
        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        return AuthResponseDTO.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .user(user.getUsername())
                .userId(user.getId())
                .build();
    }

    public AuthResponseDTO refreshToken(RefreshTokenRequestDTO request) {
        String refreshToken = request.getRefreshToken();
        User user = userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        String newAccessToken = tokenService.generateAccessToken(user);
        String newRefreshToken = tokenService.generateRefreshToken();

        return AuthResponseDTO.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .user(user.getUsername())
                .userId(user.getId())
                .build();
    }

    public void logout(String accessToken, String refreshToken) {
        tokenBlacklistService.blacklistToken(accessToken);
        tokenBlacklistService.blacklistToken(refreshToken);

        userRepository.findByRefreshToken(refreshToken).ifPresent(user -> {
            user.setRefreshToken(null);
            userRepository.save(user);
        });
    }
}
