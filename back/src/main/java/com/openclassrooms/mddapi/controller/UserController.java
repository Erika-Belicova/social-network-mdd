package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.user.UpdateUserDTO;
import com.openclassrooms.mddapi.dto.user.UserDTO;
import com.openclassrooms.mddapi.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "Authorization")
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/auth/me")
    public ResponseEntity<UserDTO> getCurrentUser(Authentication authentication) {
        UserDTO userDTO = userService.getUserByCredential(authentication.getName());
        return ResponseEntity.ok(userDTO);
    }

    @PutMapping("/auth/me")
    public ResponseEntity<UserDTO> updateCurrentUser(@RequestBody @Valid UpdateUserDTO updateUserDTO,
                                                     Authentication authentication) {
        UserDTO updatedUser = userService.updateUser(authentication.getName(), updateUserDTO);
        return ResponseEntity.ok(updatedUser);
    }

}
