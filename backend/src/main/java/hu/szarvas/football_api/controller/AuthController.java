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

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDTO> refresh(@Valid @RequestBody RefreshTokenRequestDTO request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody LogoutRequestDTO request) {
        authService.logout(authHeader.substring(7), request.getRefreshToken());
        return ResponseEntity.ok().build();
    }
}
