package com.openclassrooms.mddapi.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * DTO for user registration requests.
 */
@Data
public class RegisterRequestDTO {

    @NotBlank(message = "Username cannot be blank")
    @Schema(description = "Unique username for the new user", example = "johnsmith")
    private String username;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Must be a valid email format")
    @Schema(description = "Email address of the new user", example = "john@smith.com")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_]).{8,}$",
            message = "Password must be minimum 8 characters, include uppercase, lowercase, digit, and special character")
    @Schema(description = "Strong password (min 8 chars, upper & lower case, digit, special char)", example = "123!*GoodPassword")
    private String password;

}
