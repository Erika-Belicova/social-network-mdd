package com.openclassrooms.mddapi.security;

import com.openclassrooms.mddapi.exception.JwtGenerationException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class JWTService {

    private final JwtEncoder jwtEncoder;

    private static final int ACCESS_TOKEN_DURATION = 15 * 60; // 15 minutes
    private static final int REFRESH_TOKEN_DURATION = 7 * 24 * 60 * 60; // 7 days
    private static final ChronoUnit TOKEN_UNIT = ChronoUnit.SECONDS;

    public JWTService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
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

    private void addHttpOnlyCookie(HttpServletResponse httpServletResponse, String value) {
        Cookie cookie = new Cookie("refresh_token", value);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(REFRESH_TOKEN_DURATION);
        httpServletResponse.addCookie(cookie);
    }

    public String generateTokensAndSetCookie(Authentication authentication, HttpServletResponse httpServletResponse) {
        String accessToken = generateAccessToken(authentication);
        String refreshToken = generateRefreshToken(authentication);

        // set refresh token as httpOnly cookie only
        addHttpOnlyCookie(httpServletResponse, refreshToken);

        // return access token to send in response body
        return accessToken;
    }

}