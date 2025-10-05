package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.user.UpdateUserDTO;
import com.openclassrooms.mddapi.dto.user.UserDTO;
import com.openclassrooms.mddapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for retrieving and updating the authenticated user's profile.
 */
@Tag(name = "User profile", description = "Endpoints for user profile operations")
@SecurityRequirement(name = "Authorization")
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Retrieves the currently authenticated user's profile.
     *
     * @param authentication the current user's authentication information
     * @return the user's profile data
     */
    @Operation(summary = "Get current user", description = "Retrieve the profile of the currently authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user profile"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/auth/me")
    public ResponseEntity<UserDTO> getCurrentUser(Authentication authentication) {
        UserDTO userDTO = userService.getUserByCredential(authentication.getName());
        return ResponseEntity.ok(userDTO);
    }

    /**
     * Updates the currently authenticated user's profile.
     *
     * @param updateUserDTO  the updated user information
     * @param authentication the current user's authentication information
     * @return the updated user profile
     */
    @Operation(summary = "Update current user", description = "Update the profile information of the currently authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User profile updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "409", description = "Username or email already in use")
    })
    @PutMapping("/auth/me")
    public ResponseEntity<UserDTO> updateCurrentUser(@RequestBody @Valid UpdateUserDTO updateUserDTO,
                                                     Authentication authentication) {
        UserDTO updatedUser = userService.updateUser(authentication.getName(), updateUserDTO);
        return ResponseEntity.ok(updatedUser);
    }

}
