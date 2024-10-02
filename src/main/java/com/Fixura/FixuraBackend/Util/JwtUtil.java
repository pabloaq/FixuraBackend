package com.Fixura.FixuraBackend.Util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.Fixura.FixuraBackend.Model.Usuario;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String SECRET_KEY; //32 bytes

    public String generateToken(Usuario user) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("alg", "HS256");
        headers.put("typ", "JWT");
        
        return Jwts.builder()
                .setHeader(headers)
                .setSubject(user.getCorreo())
                .claim("dni", user.getDNI())
                .claim("id_rol", user.getId_rol())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String extractUserEmail(String token) {
        String jwtToken = token.substring(7); //Extraer el token del encabezado, ignorando "Bearer "
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwtToken).getBody().getSubject();
    }

    public String extractUserDni(String token) {
        String jwtToken = token.substring(7); //Extraer el token del encabezado, ignorando "Bearer "
        return (String) Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwtToken).getBody().get("dni");
    }

    public String extractUserIdRol(String token) {
        String jwtToken = token.substring(7); //Extraer el token del encabezado, ignorando "Bearer "
        return (String) Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwtToken).getBody().get("id_rol");
    }

    public boolean isTokenExpired(String token) {
        String jwtToken = token.substring(7); //Extraer el token del encabezado, ignorando "Bearer "
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwtToken).getBody().getExpiration().before(new Date());
    }

}
