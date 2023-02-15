package com.jfarro.app.security.filters;

import com.jfarro.app.security.services.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;

import static com.jfarro.app.security.services.JwtServiceImpl.HEADER_FORMAT;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final JwtService jwtService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtService jwtService) {
        super(authenticationManager);
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = request.getHeader("Authorization");

        if (token == null || !token.startsWith(HEADER_FORMAT)) {
            chain.doFilter(request, response);
            return;
        }

        boolean valid = this.jwtService.validateToken(token);

        UsernamePasswordAuthenticationToken authenticationToken = null;
        if (valid) {
            String username = this.jwtService.getUsername(token);
            Collection<? extends GrantedAuthority> authorities = this.jwtService.getAuthorities(token);

            authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
        }

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request, response);
    }
}
