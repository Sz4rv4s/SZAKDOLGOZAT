package hu.szarvas.football_api.service;

import hu.szarvas.football_api.dto.request.LoginRequestDTO;
import hu.szarvas.football_api.dto.request.RefreshTokenRequestDTO;
import hu.szarvas.football_api.dto.request.RegisterRequestDTO;
import hu.szarvas.football_api.dto.response.AuthResponseDTO;
import hu.szarvas.football_api.model.User;
import hu.szarvas.football_api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenService tokenService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenBlacklistService tokenBlacklistService;

    @Mock
    private SequenceGeneratorService sequenceGenerator;

    @InjectMocks
    private AuthService authService;

    private User testUser;
    private final String ACCESS_TOKEN = "test-access-token";
    private final String REFRESH_TOKEN = "test-refresh-token";

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1)
                .email("test@example.com")
                .username("testuser")
                .password("encodedPassword")
                .roles(Set.of("USER"))
                .build();
    }

    @Test
    void register_success() {
        RegisterRequestDTO request = new RegisterRequestDTO("test@example.com", "testuser", "password");
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(request.getUsername())).thenReturn(false);
        when(sequenceGenerator.generateSequence(User.SEQUENCE_NAME)).thenReturn(1);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(tokenService.generateAccessToken(any(User.class))).thenReturn(ACCESS_TOKEN);
        when(tokenService.generateRefreshToken()).thenReturn(REFRESH_TOKEN);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        AuthResponseDTO response = authService.register(request);

        assertNotNull(response);
        assertEquals(ACCESS_TOKEN, response.getAccessToken());
        assertEquals(REFRESH_TOKEN, response.getRefreshToken());
        assertEquals("testuser", response.getUser());
        assertEquals(1, response.getUserId());
        assertTrue(response.getRoles().contains("USER"));
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_emailExists_throwsException() {
        RegisterRequestDTO request = new RegisterRequestDTO("test@example.com", "testuser", "password");
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> authService.register(request));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void login_success() {
        LoginRequestDTO request = new LoginRequestDTO("testuser", "password");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(tokenService.generateAccessToken(testUser)).thenReturn(ACCESS_TOKEN);
        when(tokenService.generateRefreshToken()).thenReturn(REFRESH_TOKEN);
        when(userRepository.save(testUser)).thenReturn(testUser);

        AuthResponseDTO response = authService.login(request);

        assertNotNull(response);
        assertEquals(ACCESS_TOKEN, response.getAccessToken());
        assertEquals(REFRESH_TOKEN, response.getRefreshToken());
        assertEquals("testuser", response.getUser());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).save(testUser);
    }

    @Test
    void login_userNotFound_throwsException() {
        LoginRequestDTO request = new LoginRequestDTO("testuser", "password");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> authService.login(request));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void refreshToken_success() {
        RefreshTokenRequestDTO request = new RefreshTokenRequestDTO(REFRESH_TOKEN);
        when(userRepository.findByRefreshToken(REFRESH_TOKEN)).thenReturn(Optional.of(testUser));
        when(tokenService.generateAccessToken(testUser)).thenReturn(ACCESS_TOKEN);
        when(tokenService.generateRefreshToken()).thenReturn("new-refresh-token");

        AuthResponseDTO response = authService.refreshToken(request);

        assertNotNull(response);
        assertEquals(ACCESS_TOKEN, response.getAccessToken());
        assertEquals("new-refresh-token", response.getRefreshToken());
        assertEquals("testuser", response.getUser());
        assertEquals(1, response.getUserId());
    }

    @Test
    void refreshToken_invalidToken_throwsException() {
        RefreshTokenRequestDTO request = new RefreshTokenRequestDTO("invalid-token");
        when(userRepository.findByRefreshToken("invalid-token")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> authService.refreshToken(request));
    }

    @Test
    void logout_success() {
        when(userRepository.findByRefreshToken(REFRESH_TOKEN)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        authService.logout(ACCESS_TOKEN, REFRESH_TOKEN);

        verify(tokenBlacklistService).blacklistToken(ACCESS_TOKEN);
        verify(tokenBlacklistService).blacklistToken(REFRESH_TOKEN);
        verify(userRepository).save(testUser);
        assertNull(testUser.getRefreshToken());
    }
}