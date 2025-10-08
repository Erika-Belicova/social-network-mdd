package com.openclassrooms.mddapi.security;

import com.openclassrooms.mddapi.configuration.JwtProperties;
import com.openclassrooms.mddapi.exception.JwtGenerationException;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Service responsible for generating JWT tokens.
 */
@Service
public class JWTService {

    private final JwtEncoder jwtEncoder;

    private final JwtProperties jwtProperties;

    public JWTService(JwtEncoder jwtEncoder, JwtProperties jwtProperties) {
        this.jwtEncoder = jwtEncoder;
        this.jwtProperties = jwtProperties;
    }

    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        String subject = authentication.getName();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.DAYS))
                .subject(subject)
                .build();

        // create encoder parameters with header and claims for token generation
        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters
                .from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);

        try {
            return this.jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
        }
        catch (Exception e) {
            throw new JwtGenerationException("Token generation failed. Please try again later.");
        }
    }

}