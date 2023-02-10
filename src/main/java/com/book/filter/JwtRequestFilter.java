package com.book.filter;

import com.book.service.MyUserDetailsService;
import com.book.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final MyUserDetailsService myUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String headerAuth = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            jwt = headerAuth.substring(7);
            username = jwtUtils.getUserNameFromJwtToken(jwt);
        }

        if (username != null && jwt != null && jwtUtils.validateJwtToken(jwt) && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            log.error("Invalid jwt token or wrong credential");
        }

        filterChain.doFilter(request, response);
    }
}
