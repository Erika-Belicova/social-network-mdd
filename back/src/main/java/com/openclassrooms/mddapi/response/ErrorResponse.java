package com.openclassrooms.mddapi.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Response object representing an error message.
 */
@Data
@Schema(description = "Response object containing an error message")
public class ErrorResponse {

    @Schema(description = "Error message describing what went wrong", example = "Invalid credentials")
    private final String error;

    public ErrorResponse(String error) {
        this.error = error;
    }

}
