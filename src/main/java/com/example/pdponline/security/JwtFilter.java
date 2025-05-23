package com.example.pdponline.security;

import com.example.pdponline.entity.DeviceInfo;
import com.example.pdponline.exception.RestException;
import com.example.pdponline.repository.DeviceInfoRepository;
import com.example.pdponline.service.RedisTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.ErrorMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
@CrossOrigin
public class JwtFilter extends OncePerRequestFilter {

    private final RedisTokenService redisTokenService;
    @Value("${security.whitelist}")
    private String[] whiteList;

    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;
    private final DeviceInfoRepository deviceInfoRepository;
    public String sessionToken;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestPath = request.getServletPath();

        if (isWhiteListed(requestPath)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorization = request.getHeader("Authorization");

        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            sessionToken = token;
            Long deviceId = jwtProvider.extractDeviceId(token);
            try {
                if (redisTokenService.isTokenValid(deviceId,token)) {
                    String phoneNumber = jwtProvider.getPhoneNumberFromToken(token);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(phoneNumber);

                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            } catch (Exception e) {
                throw RestException.restThrow(e.getMessage(), HttpStatus.UNAUTHORIZED);
            }
        }

        // ❗ MUHIM: to‘g‘ri filterChain metodini chaqirish
        filterChain.doFilter(request, response);
    }


    private boolean isWhiteListed(String path) {
        AntPathMatcher matcher = new AntPathMatcher();
        return Arrays.stream(whiteList).anyMatch(pattern -> matcher.match(pattern, path));
    }

    private void handleException(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(new ErrorMessage(message)));
    }
}