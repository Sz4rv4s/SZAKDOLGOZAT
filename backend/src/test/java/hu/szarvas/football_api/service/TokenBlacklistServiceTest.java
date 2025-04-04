package hu.szarvas.football_api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TokenBlacklistServiceTest {

    private TokenBlacklistService tokenBlacklistService;

    @BeforeEach
    void setUp() {
        tokenBlacklistService = new TokenBlacklistService();
    }

    @Test
    void blacklistToken_addsToken_success() {
        String token = "test-token";

        tokenBlacklistService.blacklistToken(token);

        assertTrue(tokenBlacklistService.isBlacklisted(token));
    }

    @Test
    void blacklistToken_duplicateToken_doesNotThrow() {
        String token = "test-token";

        tokenBlacklistService.blacklistToken(token);
        tokenBlacklistService.blacklistToken(token);

        assertTrue(tokenBlacklistService.isBlacklisted(token));
    }

    @Test
    void isBlacklisted_tokenNotPresent_returnsFalse() {
        String token = "non-existent-token";

        boolean result = tokenBlacklistService.isBlacklisted(token);

        assertFalse(result);
    }

    @Test
    void isBlacklisted_tokenPresent_returnsTrue() {
        String token = "test-token";
        tokenBlacklistService.blacklistToken(token);

        boolean result = tokenBlacklistService.isBlacklisted(token);

        assertTrue(result);
    }
}