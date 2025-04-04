package hu.szarvas.football_api.service;

import hu.szarvas.football_api.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

/**
 * Service for JWT token generation and validation.
 * Handles both access tokens and refresh tokens.
 */
@Service
public class TokenService {
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.expiration}")
    private long JWT_EXPIRATION;

    /**
     * Creates the signing key from the secret key.
     *
     * @return SecretKey for JWT signing
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = SECRET_KEY.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Extracts the username from a JWT token.
     *
     * @param token The JWT token
     * @return Username from token subject
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts the expiration date from a JWT token.
     *
     * @param token The JWT token
     * @return Expiration date
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts a specific claim from a JWT token.
     *
     * @param token The JWT token
     * @param claimsResolver Function to extract the claim
     * @param <T> Type of the claim
     * @return The claim value
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from a JWT token.
     *
     * @param token The JWT token
     * @return All claims from the token
     */
    public Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Generates a JWT access token for a user.
     *
     * @param user The user to generate token for
     * @return Generated JWT access token
     */
    public String generateAccessToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", user.getRoles());
        return generateToken(claims, user, JWT_EXPIRATION);
    }

    /**
     * Generates a random UUID refresh token.
     *
     * @return Generated refresh token
     */
    public String generateRefreshToken() {
        return UUID.randomUUID().toString();
    }

    /**
     * Generates a JWT token with custom claims.
     *
     * @param extraClaims Additional claims to include
     * @param user The user to generate token for
     * @param expiration Token expiration time in milliseconds
     * @return Generated JWT token
     */
    private String generateToken(Map<String, Object> extraClaims, User user, long expiration) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Validates a JWT token.
     *
     * @param token The JWT token to validate
     * @return true if token is valid and not expired, false otherwise
     */
    public boolean isTokenValid(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if a JWT token is expired.
     *
     * @param token The JWT token to check
     * @return true if token is expired, false otherwise
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}
