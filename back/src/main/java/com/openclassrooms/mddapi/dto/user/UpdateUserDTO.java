package com.openclassrooms.mddapi.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * DTO for updating user details.
 */
@Data
public class UpdateUserDTO {

    @NotBlank(message = "Username cannot be blank")
    @Schema(description = "Updated username for the user", example = "johnsmith1")
    private String username;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Must be a valid email format")
    @Schema(description = "Updated email address of the user", example = "john@smith1.com")
    private String email;

    @NotBlank(message = "Name cannot be blank")
    @Schema(description = "Updated full name of the user", example = "John Adam Smith")
    private String name;

    @NotBlank(message = "Password cannot be blank")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_]).{8,}$",
            message = "Password must be minimum 8 characters, include uppercase, lowercase, digit, and special character")
    @Schema(description = "New password (min 8 chars, upper & lower case, digit, special char)", example = "123!*GoodPassword")
    private String password;

}
