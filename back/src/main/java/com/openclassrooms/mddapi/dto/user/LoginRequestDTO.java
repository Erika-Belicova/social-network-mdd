package com.openclassrooms.mddapi.dto.user;

import com.openclassrooms.mddapi.annotation.UsernameOrEmailRequired;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@UsernameOrEmailRequired
public class LoginRequestDTO {

    private String username;

    @Email
    private String email;

    @NotBlank
    private String password;

}
