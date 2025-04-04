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


/**
 * Service class for handling authentication-related operations including registration, login,
 * token refresh, and logout.
 */
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenBlacklistService tokenBlacklistService;
    private final SequenceGeneratorService sequenceGenerator;

    /**
     * Registers a new user with the system.
     *
     * @param request The registration request containing user details
     * @return AuthResponseDTO containing access token, refresh token and user details
     * @throws RuntimeException if email or username already exists
     */
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

        return getAuthResponse(user);
    }

    /**
     * Generates authentication response for a user.
     *
     * @param user The user entity
     * @return AuthResponseDTO containing tokens and user details
     */
    private AuthResponseDTO getAuthResponse(User user) {
        String jwtToken = tokenService.generateAccessToken(user);
        String refreshToken = tokenService.generateRefreshToken();
        user.setRefreshToken(refreshToken);

        userRepository.save(user);

        return AuthResponseDTO.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .user(user.getUsername())
                .userId(user.getId())
                .roles(user.getRoles())
                .build();
    }

    /**
     * Authenticates a user and generates tokens.
     *
     * @param request The login request containing credentials
     * @return AuthResponseDTO containing tokens and user details
     * @throws RuntimeException if user not found
     */
    public AuthResponseDTO login(LoginRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return getAuthResponse(user);
    }

    /**
     * Refreshes the access token using a valid refresh token.
     *
     * @param request The refresh token request
     * @return AuthResponseDTO containing new tokens
     * @throws RuntimeException if refresh token is invalid
     */
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

    /**
     * Logs out a user by blacklisting tokens and clearing refresh token.
     *
     * @param accessToken The JWT access token to blacklist
     * @param refreshToken The refresh token to blacklist and clear
     */
    public void logout(String accessToken, String refreshToken) {
        tokenBlacklistService.blacklistToken(accessToken);
        tokenBlacklistService.blacklistToken(refreshToken);

        userRepository.findByRefreshToken(refreshToken).ifPresent(user -> {
            user.setRefreshToken(null);
            userRepository.save(user);
        });
    }
}
