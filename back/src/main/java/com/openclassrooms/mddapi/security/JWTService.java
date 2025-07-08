package com.openclassrooms.mddapi.security;

import com.openclassrooms.mddapi.exception.JwtGenerationException;
import com.openclassrooms.mddapi.exception.JwtValidationException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class JWTService {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    private static final int ACCESS_TOKEN_DURATION = 15 * 60; // 15 minutes
    private static final int REFRESH_TOKEN_DURATION = 7 * 24 * 60 * 60; // 7 days
    private static final ChronoUnit TOKEN_UNIT = ChronoUnit.SECONDS;

    public JWTService(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder, UserDetailsServiceImpl userDetailsServiceImpl) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    public String generateToken(Authentication authentication, long duration, ChronoUnit unit, String tokenType) {
        Instant now = Instant.now();
        String subject = authentication.getName();

        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(duration, unit))
                .subject(subject)
                .build();

        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters
                .from(JwsHeader.with(MacAlgorithm.HS256).build(), jwtClaimsSet);

        try {
            return this.jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
        }
        catch (Exception e) {
            throw new JwtGenerationException(tokenType + " token generation failed. Please try again later.");
        }
    }

    public String generateAccessToken(Authentication authentication) {
        return generateToken(authentication, ACCESS_TOKEN_DURATION, TOKEN_UNIT, "Access");
    }

    public String generateRefreshToken(Authentication authentication) {
        return generateToken(authentication, REFRESH_TOKEN_DURATION, TOKEN_UNIT, "Refresh");
    }

    public void addHttpOnlyCookie(HttpServletResponse httpServletResponse, String value, int durationSeconds) {
        Cookie cookie = new Cookie("refresh_token", value);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(durationSeconds);
        httpServletResponse.addCookie(cookie);
    }

    public String generateTokensAndSetCookie(Authentication authentication, HttpServletResponse httpServletResponse) {
        String accessToken = generateAccessToken(authentication);
        String refreshToken = generateRefreshToken(authentication);

        // set refresh token as httpOnly cookie only
        addHttpOnlyCookie(httpServletResponse, refreshToken, REFRESH_TOKEN_DURATION);

        // return access token to send in response body
        return accessToken;
    }

    public String validateAndGetUsername(String token) {
        try {
            Jwt jwt = jwtDecoder.decode(token);
            return jwt.getSubject();
        } catch (JwtException e) {
            throw new JwtValidationException("Invalid token!");
        }
    }

    public Authentication authenticateRefreshToken(String refreshToken) {
        String username = validateAndGetUsername(refreshToken);
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public String extractRefreshTokenFromCookies(Cookie[] cookies) {
        if (cookies == null) {
            throw new JwtValidationException("Refresh token is missing");
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh_token")) {
                return cookie.getValue();
            }
        }
        throw new JwtValidationException("Refresh token is missing");
    }

    public String refreshToken(HttpServletResponse httpServletResponse, Cookie[] cookies) {
        String refreshToken = extractRefreshTokenFromCookies(cookies);
        Authentication authentication = authenticateRefreshToken(refreshToken);
        return generateTokensAndSetCookie(authentication, httpServletResponse);
    }

}