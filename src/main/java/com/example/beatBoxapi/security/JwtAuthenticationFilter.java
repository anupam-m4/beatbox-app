//package com.example.beatBoxapi.security;
//
//import com.example.beatBoxapi.util.JwtUtil;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Component
//@RequiredArgsConstructor
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    private final JwtUtil jwtUtil;
//    private final UserDetailsService userDetailsService;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
//       final String requestTokenHeader = request.getHeader("Authorization");
//       String email=null;
//       String jwtToken=null;
//
//       if(request.getRequestURI().contains("/login") || request.getRequestURI().contains("/register") ||  request.getRequestURI().contains("/health"))
//       {
//           filterChain.doFilter(request,response);
//           return;
//       }
//
//       if(requestTokenHeader != null && requestTokenHeader.startsWith("Bearer "))
//       {
//          jwtToken = requestTokenHeader.substring(7);
//          try {
//            email =  jwtUtil.extractEmail(jwtToken);
//          } catch (IllegalArgumentException e) {
//              throw new RuntimeException("Unable to get JWT token");
//          } catch (Exception e) {
//              throw new RuntimeException(e.getMessage());
//          }
//
//       }
//       else
//       {
//           throw new IllegalArgumentException("JWT token doesn't begin with Bearer token");
//       }
//
//       if(email != null && SecurityContextHolder.getContext().getAuthentication() == null )
//       {
//           UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);
//           if(jwtUtil.validateToken(jwtToken,userDetails))
//           {
//               UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//               usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//               SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//           }
//       }
//
//       filterChain.doFilter(request,response);
//
//    }
//
//}

//
//package com.example.beatBoxapi.security;
//
//import com.example.beatBoxapi.util.JwtUtil;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Component
//@RequiredArgsConstructor
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    private final JwtUtil jwtUtil;
//    private final UserDetailsService userDetailsService;
//
//    // --- FIX 1: Prevent filter execution for whitelisted paths ---
//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
//        String path = request.getRequestURI();
//
//        // Explicitly exclude all paths configured with .permitAll() in SecurityConfig
//        return path.equals("/") ||
//                path.equals("/favicon.ico") ||
//                path.startsWith("/api/auth/login") ||
//                path.startsWith("/api/auth/register") ||
//                path.startsWith("/api/health") ||
//                path.startsWith("/css/") ||
//                path.startsWith("/js/");
//    }
//    // -----------------------------------------------------------
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
//        final String requestTokenHeader = request.getHeader("Authorization");
//        String email = null;
//        String jwtToken = null;
//
//        // --- FIX 2: Removed local whitelisting as it's now handled by shouldNotFilter ---
//
//        // 1. Token presence and format check
//        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
//            jwtToken = requestTokenHeader.substring(7);
//            try {
//                email = jwtUtil.extractEmail(jwtToken);
//            } catch (Exception e) {
//                // Log issues like token expiration or invalid signature
//                logger.warn("JWT validation failed for URI: " + request.getRequestURI() + " Error: " + e.getMessage());
//            }
//        } else {
//            // --- FIX 3: Removed strict IllegalArgumentException throw ---
//            // Log missing token for secured paths, but don't throw an exception.
//            // Spring Security will handle the 403 Forbidden response later.
//            logger.warn("JWT Token is missing or doesn't begin with Bearer token for URI: " + request.getRequestURI());
//        }
//
//        // 2. Security context population if token is valid
//        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);
//            if (jwtUtil.validateToken(jwtToken, userDetails)) {
//                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            }
//        }
//
//        // 3. Continue the filter chain
//        filterChain.doFilter(request, response);
//    }
//}


package com.example.beatBoxapi.security;

import com.example.beatBoxapi.service.AppUserDetailsService;
import com.example.beatBoxapi.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final AppUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        System.out.println("\n--- JWT Filter Processing URI: " + request.getRequestURI() + " ---");

        // 1. Token presence and format check
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("JWT Filter: No Bearer token found. Continuing filter chain.");
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);

        // **FIX 1: Using extractEmail()**
        userEmail = jwtUtil.extractEmail(jwt);

        // 2. Security context population if token is valid
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Load user details
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // **FIX 2: Using validateToken()**
            if (jwtUtil.validateToken(jwt, userDetails)) {

                // --- CRITICAL DEBUGGING LOG ---
                System.out.println("JWT VALIDATED SUCCESSFULLY for user: " + userEmail);
                System.out.println("Authorities loaded into Context: " + userDetails.getAuthorities());
                // -----------------------------

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);

            } else {
                System.out.println("JWT Validation FAILED: Token expired or invalid signature.");
            }
        }

        // 3. Continue the filter chain
        filterChain.doFilter(request, response);
        System.out.println("--- JWT Filter finished for URI: " + request.getRequestURI() + " ---");
    }
}







