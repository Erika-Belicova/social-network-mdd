package com.openclassrooms.mddapi.dto.user;

import com.openclassrooms.mddapi.annotation.UsernameOrEmailRequired;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO for login requests. Requires either username or email, along with a password.
 */
@Data
@UsernameOrEmailRequired
public class LoginRequestDTO {

    @Schema(description = "Username of the user. Either username or email must be provided.", example = "johnsmith")
    private String username;

    @Email(message = "Must be a valid email format")
    @Schema(description = "Email address of the user. Either email or username must be provided.", example = "john@smith.com")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Schema(description = "Password of the user", example = "123!*GoodPassword")
    private String password;

}
