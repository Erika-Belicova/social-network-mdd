package com.openclassrooms.mddapi.dto.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserDTO {

    private final Long id;

    private final String username;

    private final String email;

}
