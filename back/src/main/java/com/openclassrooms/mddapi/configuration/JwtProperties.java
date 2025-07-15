package com.openclassrooms.mddapi.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for JWT (JSON Web Token) handling.
 * <p>
 * Binds properties prefixed with "jwt" from the application's configuration files
 * (e.g., application.properties or application.yml).
 * </p>
 *
 * Example:
 * <pre>
 * jwt.secret-key=your-secret-key
 * </pre>
 */
@Component
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtProperties {

    private String secretKey;

}
