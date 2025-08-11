package com.openclassrooms.mddapi.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO for login requests. Requires either username or email, along with a password.
 */
@Data
public class LoginRequestDTO {

    @NotBlank(message = "Username or email cannot be blank")
    @Schema(description = "Username or email address of the user. One must be provided.", example = "johnsmith or john@smith.com")
    private String usernameOrEmail;

    @NotBlank(message = "Password cannot be blank")
    @Schema(description = "Password of the user", example = "123!*GoodPassword")
    private String password;

}
