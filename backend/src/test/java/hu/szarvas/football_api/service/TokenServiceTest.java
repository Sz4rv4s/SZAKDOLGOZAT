package hu.szarvas.football_api.service;

import hu.szarvas.football_api.model.User;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    private TokenService tokenService;

    private final long JWT_EXPIRATION = 1000 * 60 * 60;

    @BeforeEach
    void setUp() throws Exception {
        tokenService = new TokenService();

        Field secretKeyField = TokenService.class.getDeclaredField("SECRET_KEY");
        secretKeyField.setAccessible(true);
        String SECRET_KEY = "testsecretkey1234567890testsecretkey1234567890";
        secretKeyField.set(tokenService, SECRET_KEY);

        Field expirationField = TokenService.class.getDeclaredField("JWT_EXPIRATION");
        expirationField.setAccessible(true);
        expirationField.set(tokenService, JWT_EXPIRATION);
    }

    @Test
    void generateAccessToken_createsValidToken() {
        User user = User.builder()
                .username("testuser")
                .roles(Set.of("USER"))
                .build();

        String token = tokenService.generateAccessToken(user);

        assertNotNull(token);
        assertEquals("testuser", tokenService.extractUsername(token));
        assertTrue(tokenService.isTokenValid(token));
        Claims claims = tokenService.extractAllClaims(token);
    }

    @Test
    void generateRefreshToken_createsUUID() {
        String refreshToken = tokenService.generateRefreshToken();

        assertNotNull(refreshToken);
        assertDoesNotThrow(() -> UUID.fromString(refreshToken));
    }

    @Test
    void extractUsername_returnsCorrectUsername() {
        User user = User.builder()
                .username("testuser")
                .build();
        String token = tokenService.generateAccessToken(user);

        String username = tokenService.extractUsername(token);

        assertEquals("testuser", username);
    }

    @Test
    void extractExpiration_returnsFutureDate() {
        User user = User.builder()
                .username("testuser")
                .build();
        String token = tokenService.generateAccessToken(user);

        Date expiration = tokenService.extractExpiration(token);

        assertTrue(expiration.after(new Date()));
    }

    @Test
    void isTokenValid_validToken_returnsTrue() {
        User user = User.builder()
                .username("testuser")
                .build();
        String token = tokenService.generateAccessToken(user);

        boolean isValid = tokenService.isTokenValid(token);

        assertTrue(isValid);
    }

    @Test
    void isTokenValid_expiredToken_returnsFalse() throws Exception {
        User user = User.builder()
                .username("testuser")
                .build();

        Field expirationField = TokenService.class.getDeclaredField("JWT_EXPIRATION");
        expirationField.setAccessible(true);
        expirationField.set(tokenService, -1000);

        String token = tokenService.generateAccessToken(user);

        boolean isValid = tokenService.isTokenValid(token);

        assertFalse(isValid);

        expirationField.set(tokenService, JWT_EXPIRATION);
    }

    @Test
    void isTokenValid_invalidToken_returnsFalse() {
        String invalidToken = "invalid.token.here";

        boolean isValid = tokenService.isTokenValid(invalidToken);

        assertFalse(isValid);
    }

    @Test
    void extractAllClaims_returnsCorrectClaims() {
        User user = User.builder()
                .username("testuser")
                .roles(Set.of("USER"))
                .build();
        String token = tokenService.generateAccessToken(user);

        Claims claims = tokenService.extractAllClaims(token);

        assertEquals("testuser", claims.getSubject());
        assertNotNull(claims.getIssuedAt());
        assertNotNull(claims.getExpiration());
    }

    @Test
    void generateToken_customClaims_includesClaims() throws Exception {
        User user = User.builder()
                .username("testuser")
                .build();
        java.lang.reflect.Method method = TokenService.class.getDeclaredMethod("generateToken", Map.class, User.class, long.class);
        method.setAccessible(true);
        Map<String, Object> extraClaims = Map.of("custom", "value");

        String token = (String) method.invoke(tokenService, extraClaims, user, JWT_EXPIRATION);

        Claims claims = tokenService.extractAllClaims(token);
        assertEquals("value", claims.get("custom"));
        assertEquals("testuser", claims.getSubject());
    }
}