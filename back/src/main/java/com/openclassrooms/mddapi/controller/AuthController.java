package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.user.LoginRequestDTO;
import com.openclassrooms.mddapi.dto.user.RegisterRequestDTO;
import com.openclassrooms.mddapi.response.AuthResponse;
import com.openclassrooms.mddapi.security.JWTService;
import com.openclassrooms.mddapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for user authentication and authorization operations,
 * including registration and login.
 */
@Tag(name = "Authentication", description = "Endpoints for user authentication")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    public AuthController(UserService userService, AuthenticationManager authenticationManager, JWTService jwtService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    /**
     * Registers a new user and immediately logs them in.
     *
     * @param registerRequestDTO the registration details (username, email and password)
     * @return the access token for the newly registered user
     */
    @Operation(summary = "Register new user", description = "Registers a new user and returns an access token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered and logged in successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid registration data"),
            @ApiResponse(responseCode = "409", description = "Username or email already in use")
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid RegisterRequestDTO registerRequestDTO) {
        userService.registerUser(registerRequestDTO);
        // direct login after register
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(registerRequestDTO.getUsername(), registerRequestDTO.getPassword()));

        String accessToken = jwtService.generateToken(authentication);
        return ResponseEntity.ok(new AuthResponse(accessToken));
    }

    /**
     * Authenticates a user by username or email and password, returning a JWT access token.
     *
     * @param loginRequestDTO the login credentials (username or email, and password)
     * @return the access token for the authenticated user
     */
    @Operation(summary = "User login", description = "Authenticates user and returns an access token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged in successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials or unauthorized")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
        // login with the credential provided
        Authentication authentication = authenticationManager
                .authenticate((new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsernameOrEmail(), loginRequestDTO.getPassword())));

        String accessToken = jwtService.generateToken(authentication);
        return ResponseEntity.ok(new AuthResponse(accessToken));
    }

}

