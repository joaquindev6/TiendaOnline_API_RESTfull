package com.jfarro.app.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfarro.app.security.UserCredentials;
import com.jfarro.app.security.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.jfarro.app.security.services.JwtServiceImpl.HEADER_FORMAT;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    /**
     * Autentica el username y el password que se pasa por el request
     * @param request
     * @param response
     * @return el contenedor de credenciales con el username y el password
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String username = this.obtainUsername(request);
        String password = this.obtainPassword(request);

        if (username == null && password == null) {
            try {
                UserCredentials userCredentials = new ObjectMapper().readValue(request.getInputStream(), UserCredentials.class);
                username = userCredentials.getUsername();
                password = userCredentials.getPassword();
            } catch (IOException e) {
                throw new RuntimeException("Error al guardar datos de las credenciales.", e.getCause());
            }
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        return this.authenticationManager.authenticate(authenticationToken);
    }

    /**
     * Crea el token y lo agrega a la cabecera de la respuesta, muestra datos adicionales en el cuerpo de la respuesta
     * @param request
     * @param response
     * @param chain
     * @param authResult
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String token = this.jwtService.createToken(authResult);

        response.setHeader("Authorization", HEADER_FORMAT.concat(token));

        Map<String, Object> body = new HashMap<>();
        body.put("token", token);
        body.put("user", (UserDetails) authResult.getPrincipal());
        body.put("mensaje", "El inicio de sesión ha sido con exito.");

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
    }

    /**
     * En el caso que no se haya autenticado se mostrara datos en el cuerpo de la respuesta
     * @param request
     * @param response
     * @param failed
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        Map<String, Object> data = new HashMap<>();
        data.put("mensaje", "Error de autenticacion: el usrname o password son incorrectos.");
        data.put("error", failed.getMessage());

        response.getWriter().write(new ObjectMapper().writeValueAsString(data));
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
    }
}
