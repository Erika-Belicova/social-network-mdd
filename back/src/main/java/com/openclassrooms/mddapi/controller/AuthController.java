package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.user.LoginRequestDTO;
import com.openclassrooms.mddapi.dto.user.RegisterRequestDTO;
import com.openclassrooms.mddapi.exception.JwtValidationException;
import com.openclassrooms.mddapi.response.AuthResponse;
import com.openclassrooms.mddapi.response.ErrorResponse;
import com.openclassrooms.mddapi.security.JWTService;
import com.openclassrooms.mddapi.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for user authentication and authorization operations,
 * including registration, login, token refresh, and logout.
 */
@Tag(name = "Authentication", description = "Endpoints for user authentication and token management")
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
     * @param registerRequestDTO the registration details (username, email, name, password)
     * @param httpServletResponse the HTTP response, used to set refresh token cookie
     * @return the access token for the newly registered user
     */
    @Operation(summary = "Register new user", description = "Registers a new user and returns an access token with a refresh token cookie.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered and logged in successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid registration data"),
            @ApiResponse(responseCode = "409", description = "Username or email already in use")
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid RegisterRequestDTO registerRequestDTO, HttpServletResponse httpServletResponse) {
        userService.registerUser(registerRequestDTO);
        // direct login after register
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(registerRequestDTO.getUsername(), registerRequestDTO.getPassword()));

        String accessToken = jwtService.generateTokensAndSetCookie(authentication, httpServletResponse);
        return ResponseEntity.ok(new AuthResponse(accessToken));
    }

    /**
     * Authenticates a user by username or email and password, returning a JWT access token.
     *
     * @param loginRequestDTO the login credentials (username or email, and password)
     * @param httpServletResponse the HTTP response, used to set refresh token cookie
     * @return the access token for the authenticated user
     */
    @Operation(summary = "User login", description = "Authenticates user and returns an access token with a refresh token cookie.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged in successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials or unauthorized")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO, HttpServletResponse httpServletResponse) {
        // login with the credential provided
        Authentication authentication = authenticationManager
                .authenticate((new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsernameOrEmail(), loginRequestDTO.getPassword())));

        String accessToken = jwtService.generateTokensAndSetCookie(authentication, httpServletResponse);
        return ResponseEntity.ok(new AuthResponse(accessToken));
    }

    /**
     * Refreshes the access token using a valid refresh token stored in an HttpOnly cookie.
     *
     * @param httpServletRequest the HTTP request containing the refresh token cookie
     * @param httpServletResponse the HTTP response to set new tokens and cookies
     * @return the new access token or error if refresh token is invalid
     */
    @Operation(summary = "Refresh JWT token", description = "Refreshes the access token using a valid refresh token cookie.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Access token refreshed successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid or missing refresh token")
    })
    @PostMapping("/refresh")
    public ResponseEntity<Object> refreshToken(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            Cookie[] cookies = httpServletRequest.getCookies();
            String newAccessToken = jwtService.refreshToken(httpServletResponse, cookies);
            return ResponseEntity.ok(new AuthResponse(newAccessToken));
        } catch (JwtValidationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Invalid token!"));
        }
    }

    /**
     * Logs out the current user by clearing the refresh token cookie.
     *
     * @param httpServletResponse the HTTP response to clear the refresh token cookie
     * @return HTTP 204 No Content response
     */
    @Operation(summary = "Logout user", description = "Logs out the user by clearing the refresh token cookie.")
    @ApiResponse(responseCode = "204", description = "User logged out successfully")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse httpServletResponse) {
        // clears the refresh token cookie on logout
        jwtService.addHttpOnlyCookie(httpServletResponse, "", 0);
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }

}

