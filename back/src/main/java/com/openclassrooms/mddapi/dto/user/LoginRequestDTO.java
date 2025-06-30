package com.openclassrooms.mddapi.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDTO {

    private String username;

    @Email
    private String email;

    @NotBlank
    private String password;

}
