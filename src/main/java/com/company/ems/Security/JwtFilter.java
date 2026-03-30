package com.company.ems.Security;

import com.company.ems.Service.Admin.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        if (path.equals("/login") || path.startsWith("/css") || path.startsWith("/js")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = null;
        String email = null;

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("jwt")) {
                    token = cookie.getValue();

                    if (token != null && !token.isEmpty()) {
                        try {
                            email = jwtUtil.extractEmail(token);

                        } catch (io.jsonwebtoken.ExpiredJwtException e) {

                            Cookie cookie2 = new Cookie("jwt", null);
                            cookie2.setMaxAge(0);
                            cookie2.setPath("/");
                            response.addCookie(cookie2);

                            response.sendRedirect("/login?expired=true");
                            return;

                        } catch (Exception e) {

                            Cookie cookie2 = new Cookie("jwt", null);
                            cookie2.setMaxAge(0);
                            cookie2.setPath("/");
                            response.addCookie(cookie2);

                            response.sendRedirect("/login?error=true");
                            return;
                        }
                    }
                    break;
                }
            }
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            if (jwtUtil.isTokenValid(token, userDetails.getUsername())) {

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}