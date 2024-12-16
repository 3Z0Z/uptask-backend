package com.up_task_project.uptask_backend.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.IOException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${application.security.jwt.private-key-path}")
    private String PRIVATE_KEY_PATH;

    @Value("${application.security.jwt.public-key-path}")
    private String PUBLIC_KEY_PATH;

    private static final long TOKEN_EXPIRATION_TIME = 15 * 60 * 1000;
    private static final long REFRESH_TOKEN_EXPIRATION_TIME = 30L * 24L * 60L * 60L * 1000L;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    @PostConstruct
    public void init() {
        try {
            this.privateKey = loadPrivateKey(PRIVATE_KEY_PATH);
            this.publicKey = loadPublicKey(PUBLIC_KEY_PATH);
        } catch (IOException e) {
            throw new RuntimeException("Error loading keys: " + e.getMessage(), e);
        }
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractUsernameFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails, boolean isRefreshToken) {
        return generateToken(new HashMap<>(), userDetails, isRefreshToken);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails, boolean isRefreshToken) {
        return buildToken(extraClaims, userDetails, isRefreshToken);
    }

    public String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, boolean isRefreshToken) {
        return Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + (isRefreshToken ? REFRESH_TOKEN_EXPIRATION_TIME : TOKEN_EXPIRATION_TIME)))
            .signWith(privateKey, SignatureAlgorithm.ES512)
            .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && isTokenNonExpired(token);
    }

    public boolean isTokenNonExpired(String token) {
        return !extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
            .parserBuilder()
            .setSigningKey(publicKey)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    private PrivateKey loadPrivateKey(String path) throws IOException {
        try {
            String keyContent = Files.readString(Path.of(path))
                    .replaceAll("-----BEGIN PRIVATE KEY-----", "")
                    .replaceAll("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");
            byte[] keyBytes = Decoders.BASE64.decode(keyContent);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            return keyFactory.generatePrivate(spec);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load private key", e);
        }
    }

    private PublicKey loadPublicKey(String path) throws IOException {
        try {
            String keyContent = Files.readString(Path.of(path))
                    .replaceAll("-----BEGIN PUBLIC KEY-----", "")
                    .replaceAll("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s+", "");
            byte[] keyBytes = Decoders.BASE64.decode(keyContent);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            return keyFactory.generatePublic(spec);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load public key", e);
        }
    }

}
