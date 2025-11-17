package com.javierlobo.videoclub.authservice.controllers;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final SecretKey secretKey;

    public AuthController(AuthenticationManager authenticationManager, SecretKey secretKey) {
        this.authenticationManager = authenticationManager;
        this.secretKey = secretKey;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> credentials) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(credentials.get("username"), credentials.get("password"))
            );

            String token = Jwts.builder()
                    .setSubject(authentication.getName())
                    .claim("roles", authentication.getAuthorities())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 3600_000)) // 1 hora
                    .signWith(secretKey, SignatureAlgorithm.HS256)
                    .compact();

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("expires_in", 3600);
            return response;

        } catch (AuthenticationException e) {
            throw new RuntimeException("Credenciales inválidas");
        }
    }

    @GetMapping("/validate")
    public Map<String, Object> validate(@RequestParam String token) {
        var claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Map<String, Object> response = new HashMap<>();
        response.put("user", claims.getSubject());
        response.put("roles", claims.get("roles"));
        response.put("valid", true);
        return response;
    }
}
