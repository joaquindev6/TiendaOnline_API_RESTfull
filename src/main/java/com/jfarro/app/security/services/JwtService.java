package com.jfarro.app.security.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.IOException;
import java.util.Collection;

public interface JwtService {
    String createToken(Authentication authentication) throws JsonProcessingException;
    Claims getClaims(String token);
    boolean validateToken(String token);
    String resolveHeader(String header);
    Collection<? extends GrantedAuthority> getAuthorities(String token) throws IOException;
    String getUsername(String token);
}
