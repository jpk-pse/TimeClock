package com.psedb.timeclock.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;

@Component
public class KioskKeyFilter extends OncePerRequestFilter {

    private static final String HEADER = "X-KIOSK-KEY";

    private final String expectedKey;
    private final ObjectMapper om = new ObjectMapper();

    public KioskKeyFilter(
            @Value("${app.kiosk.key}") String expectedKey
    ){
        this.expectedKey = expectedKey;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request){
        String path = request.getRequestURI();
        return !path.startsWith("/api/v1/kiosk");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String provided = request.getHeader(HEADER);

        if (provided == null || provided.isBlank() || !provided.equals(expectedKey)) {
            unauthorized(response, request.getRequestURI());
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void unauthorized(HttpServletResponse response, String path) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> body = Map.of(
                "timestamp", Instant.now().toString(),
                "status", 401,
                "error", "Unauthorized",
                "message", "Missing or invalid kiosk key",
                "path", path
        );
        om.writeValue(response.getOutputStream(), body);
    }
}
