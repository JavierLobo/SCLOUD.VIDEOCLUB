package com.javierlobo.videoclub.authservice.configs;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Configuration
public class JwtConfig {

    @Value("${jwt.secret:}")
    private String jwtSecret;

    @Bean
    public SecretKey jwtSecretKey() {
        // Si no se define clave o es demasiado corta, genera una segura automáticamente
        if (jwtSecret == null || jwtSecret.getBytes(StandardCharsets.UTF_8).length < 32) {
            System.out.println("⚠️ Clave JWT insuficiente o no definida, generando una nueva segura automáticamente...");
            return Keys.secretKeyFor(SignatureAlgorithm.HS256);
        }

        // Verificar longitud mínima (32 bytes = 256 bits)
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            System.out.println("⚠️ Clave JWT insuficiente (" + keyBytes.length * 8 + " bits). Generando nueva clave segura...");
            return Keys.secretKeyFor(SignatureAlgorithm.HS256);
        }

        return Keys.hmacShaKeyFor(keyBytes);
    }
}
