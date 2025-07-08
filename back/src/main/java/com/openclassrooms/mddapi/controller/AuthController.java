package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.user.LoginRequestDTO;
import com.openclassrooms.mddapi.dto.user.RegisterRequestDTO;
import com.openclassrooms.mddapi.exception.JwtValidationException;
import com.openclassrooms.mddapi.response.AuthResponse;
import com.openclassrooms.mddapi.response.ErrorResponse;
import com.openclassrooms.mddapi.security.JWTService;
import com.openclassrooms.mddapi.service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid RegisterRequestDTO registerRequestDTO, HttpServletResponse httpServletResponse) {
        userService.registerUser(registerRequestDTO);
        // direct login after register
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(registerRequestDTO.getUsername(), registerRequestDTO.getPassword()));

        String accessToken = jwtService.generateTokensAndSetCookie(authentication, httpServletResponse);
        return ResponseEntity.ok(new AuthResponse(accessToken));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO, HttpServletResponse httpServletResponse) {
        // login with username or email
        Authentication authentication = authenticationManager
                .authenticate((new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword())));

        String accessToken = jwtService.generateTokensAndSetCookie(authentication, httpServletResponse);
        return ResponseEntity.ok(new AuthResponse(accessToken));
    }

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

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse httpServletResponse) {
        // clears the refresh token cookie on logout
        jwtService.addHttpOnlyCookie(httpServletResponse, "", 0);
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }

}

