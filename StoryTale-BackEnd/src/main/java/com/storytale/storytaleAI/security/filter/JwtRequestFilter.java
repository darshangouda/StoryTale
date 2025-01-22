package com.storytale.storytaleAI.security.filter;
import com.storytale.storytaleAI.security.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtRequestFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            // Extract JWT from the Authorization header or cookie
            String jwtToken = extractJwtToken(request);
            System.out.println("VALIDATE TOKEN = "+jwtUtil.validateToken(jwtToken));
            if (jwtToken != null && jwtUtil.validateToken(jwtToken)) {
                // Extract authentication from the JWT
                Authentication authentication = jwtUtil.getAuthentication(jwtToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            // Handle any exceptions (e.g., logging)
            logger.error("Failed to process JWT token", e);
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }

    public String extractJwtToken(HttpServletRequest request) {
        // Extract from Authorization header
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Remove "Bearer " prefix
        }

        // Optionally, extract from a cookie (e.g., named "jwtToken")
        if (request.getCookies() != null) {
            for (var cookie : request.getCookies()) {
                if ("jwtToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null; // No JWT token found
    }

}
