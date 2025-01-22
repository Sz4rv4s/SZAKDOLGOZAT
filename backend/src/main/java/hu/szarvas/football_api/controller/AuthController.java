package hu.szarvas.football_api.controller;

import hu.szarvas.football_api.dto.request.LoginRequest;
import hu.szarvas.football_api.dto.request.LogoutRequest;
import hu.szarvas.football_api.dto.request.RefreshTokenRequest;
import hu.szarvas.football_api.dto.request.RegisterRequest;
import hu.szarvas.football_api.dto.response.AuthResponse;
import hu.szarvas.football_api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody LogoutRequest request) {
        authService.logout(authHeader.substring(7), request.getRefreshToken());
        return ResponseEntity.ok().build();
    }
}
