package com.jfarro.app.security.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

@Component
public class JwtServiceImpl implements JwtService {

    private static final String ACCESS_TOKEN_KEY_SECRET = "cHJvYmFuZG8uYWxndW5hY2xhdmVzZWNyZXRh";
    private static final Date ACCESS_TOKEN_DATE_CREATE = new Date();
    private static final Date ACCESS_TOKEN_DATE_EXPIRATION = new Date(System.currentTimeMillis() + 1_600_000L);
    public static final String HEADER_FORMAT = "Bearer ";

    @Override
    public String createToken(Authentication authentication) throws JsonProcessingException {
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        Collection<? extends GrantedAuthority> roles = authentication.getAuthorities();

        Claims claims = Jwts.claims();
        claims.put("authorities", new ObjectMapper().writeValueAsString(roles));

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(ACCESS_TOKEN_DATE_CREATE)
                .setExpiration(ACCESS_TOKEN_DATE_EXPIRATION)
                .signWith(Keys.hmacShaKeyFor(ACCESS_TOKEN_KEY_SECRET.getBytes()))
                .compact();
    }

    @Override
    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(ACCESS_TOKEN_KEY_SECRET.getBytes())
                .build()
                .parseClaimsJws(resolveHeader(token))
                .getBody();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    @Override
    public String resolveHeader(String header) {
        return header.replace("Bearer ", "");
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(String token) throws IOException {
        Object roles = getClaims(token).get("authorities");
        return Arrays.asList(new ObjectMapper().addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityMixin.class)
                .readValue(roles.toString().getBytes(), SimpleGrantedAuthority[].class));
    }

    @Override
    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }
}
