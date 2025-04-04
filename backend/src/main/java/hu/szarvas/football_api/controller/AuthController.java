package hu.szarvas.football_api.controller;

import hu.szarvas.football_api.dto.request.LoginRequestDTO;
import hu.szarvas.football_api.dto.request.LogoutRequestDTO;
import hu.szarvas.football_api.dto.request.RefreshTokenRequestDTO;
import hu.szarvas.football_api.dto.request.RegisterRequestDTO;
import hu.szarvas.football_api.dto.response.AuthResponseDTO;
import hu.szarvas.football_api.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling authentication-related endpoints such as registration, login, logout, and token refresh.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    /**
     * Registers a new user.
     * @param request registration details
     * @return authentication response with JWT tokens
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        return ResponseEntity.ok(authService.register(request));
    }

    /**
     * Logs in an existing user.
     * @param request login credentials
     * @return authentication response with JWT tokens
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }

    /**
     * Refreshes an access token using a valid refresh token.
     * @param request refresh token payload
     * @return new authentication tokens
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDTO> refresh(@Valid @RequestBody RefreshTokenRequestDTO request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    /**
     * Logs out a user and invalidates the refresh token.
     * @param authHeader Bearer token from the Authorization header
     * @param request contains the refresh token
     * @return 200 OK if successful
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody LogoutRequestDTO request) {
        authService.logout(authHeader.substring(7), request.getRefreshToken());
        return ResponseEntity.ok().build();
    }
}
