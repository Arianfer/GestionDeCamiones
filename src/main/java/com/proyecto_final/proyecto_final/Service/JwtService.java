package com.proyecto_final.proyecto_final.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Service
public class JwtService {

    // Trae los valores de tu application.properties
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    // Genera el token final
    public String generarToken(UserDetails usuario) {
        return generarToken(new HashMap<>(), usuario);
    }

    private String generarToken(HashMap<String,Object> extraClaims, UserDetails usuario) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(usuario.getUsername()) // Acá va a guardar el DNI
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(obtenerFirma(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Lee el token y saca el DNI que guardamos adentro
    public String obtenerDniDelToken(String token) {
        return obtenerReclamo(token, Claims::getSubject);
    }

    // Valida que el token sea del usuario y no esté vencido
    public boolean esTokenValido(String token, UserDetails usuario) {
        final String dni = obtenerDniDelToken(token);
        return (dni.equals(usuario.getUsername()) && !estaTokenExpirado(token));
    }

    private Claims obtenerTodosLosReclamos(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(obtenerFirma())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T obtenerReclamo(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = obtenerTodosLosReclamos(token);
        return claimsResolver.apply(claims);
    }

    private boolean estaTokenExpirado(String token) {
        return obtenerExpiracion(token).before(new Date());
    }

    private Date obtenerExpiracion(String token) {
        return obtenerReclamo(token, Claims::getExpiration);
    }

    private Key obtenerFirma() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}