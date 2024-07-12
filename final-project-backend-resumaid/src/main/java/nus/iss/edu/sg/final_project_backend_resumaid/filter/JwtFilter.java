package nus.iss.edu.sg.final_project_backend_resumaid.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nus.iss.edu.sg.final_project_backend_resumaid.service.CustomUserDetailsService;
import nus.iss.edu.sg.final_project_backend_resumaid.utils.JwtUtil;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println(">>>>>>> IN JWT FILTER");
        

        final String authorizationHeader = request.getHeader("Authorization");

        String email = null;
        String jwt = null;

        // If header that starts with "Bearer " is present:
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {

            jwt = authorizationHeader.substring(7); // Obtain JWTs

            System.out.println(">>>>>>> JWT: " + jwt);

            try {
                email = jwtUtil.extractEmail(jwt); // Extract email from JWT
            } catch (ExpiredJwtException e) {
                e.printStackTrace();
            }
        }

        // If email exists and not yet authenticated (prevents re-authentication)
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(email);

            // Ensures that the JWT is valid and corresponds to the correct user
            if (jwtUtil.validateToken(jwt, userDetails.getUsername())) {

                // Create UsernamePasswordAuthenticationToken
                // (so that Spring Security will recognise as authenticated)
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        filterChain.doFilter(request, response);

    }

}
