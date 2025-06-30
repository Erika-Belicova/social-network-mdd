package com.openclassrooms.mddapi.validator;

import com.openclassrooms.mddapi.annotation.UsernameOrEmailRequired;
import com.openclassrooms.mddapi.dto.user.LoginRequestDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator to check that either username or email
 * is provided in the LoginRequestDTO.
 */
public class UsernameOrEmailValidator implements ConstraintValidator<UsernameOrEmailRequired, LoginRequestDTO> {

    @Override
    public boolean isValid(LoginRequestDTO loginRequestDTO, ConstraintValidatorContext constraintValidatorContext) {
        if (loginRequestDTO == null) return false;

        boolean hasUsername = loginRequestDTO.getUsername() != null && !loginRequestDTO.getUsername().trim().isEmpty();
        boolean hasEmail = loginRequestDTO.getEmail() != null && !loginRequestDTO.getEmail().trim().isEmpty();

        return hasUsername || hasEmail;
    }

}