package com.geekhub1.geekhub1.config;

import com.geekhub1.geekhub1.model.Rol;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    // Generación del token con el rol del usuario
    public String generateToken(String username, Rol rol) {
        return Jwts.builder()
                .setSubject(username)  // Usamos el correo o nombre de usuario como "subject"
                .claim("role", rol.name())  // Agregar el rol como un claim (con rol.name() obtenemos el nombre del enum)
                .setIssuedAt(new Date(System.currentTimeMillis()))  // Fecha de emisión del token
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Expiración en 10 horas
                .signWith(SignatureAlgorithm.HS256, secretKey)  // Algoritmo de firma
                .compact();  // Crear el token
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);  // Extraer el subject (nombre de usuario)
    }

    public String extractRole(String token) {
        try {
            Claims claims = Jwts.parser()  // Utiliza el método anterior
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();

            // Obtener el rol del claim
            return claims.get("role", String.class);
        } catch (JwtException | IllegalArgumentException e) {
            // Maneja el caso donde el token es inválido o el claim "role" no está presente
            throw new RuntimeException("No se pudo extraer el rol del token: " + e.getMessage());
        }
    }

    // Método auxiliar para extraer un claim genérico
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    // Extraer todos los claims del token
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)  // Usar la clave secreta para validar la firma
                .parseClaimsJws(token)  // Parsear el token y obtener los claims
                .getBody();  // Obtener el cuerpo del token con los claims
    }

    // Verificar si el token ha expirado
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());  // Compara la fecha de expiración con la fecha actual
    }

    // Extraer la fecha de expiración desde el token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);  // Extraer la fecha de expiración
    }
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
