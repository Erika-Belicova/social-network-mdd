package com.openclassrooms.mddapi.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Response object containing the authentication token.
 */
@Data
@Schema(description = "Response object containing the authentication token")
public class AuthResponse {

    @Schema(description = "JWT access token for authenticated user", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    public AuthResponse(String token) {
        this.token = token;
    }

}
