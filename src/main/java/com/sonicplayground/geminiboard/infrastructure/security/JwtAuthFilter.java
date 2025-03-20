package com.sonicplayground.geminiboard.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Order(0)
@RequiredArgsConstructor
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        String token = Optional.ofNullable(request.getHeader("Authorization"))
            .filter(t -> t.startsWith("Bearer "))
            .map(t -> t.substring(7))
            .orElse(null);

        String[] userInfo = Optional.ofNullable(token)
            .filter(subject -> subject.length() >= 10)
            .map(tokenProvider::getUserInfo)
            .orElse("anonymous:anonymous")
            .split(":");

        User user = new User(userInfo[0], "", List.of(new SimpleGrantedAuthority(userInfo[1])));

        UsernamePasswordAuthenticationToken authenticated = UsernamePasswordAuthenticationToken.authenticated(
            user, token, user.getAuthorities());

        authenticated.setDetails(new WebAuthenticationDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authenticated);

        filterChain.doFilter(request, response);
    }
}
