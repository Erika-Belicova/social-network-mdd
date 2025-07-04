package com.openclassrooms.mddapi.validator;

import com.openclassrooms.mddapi.annotation.UsernameOrEmailRequired;
import com.openclassrooms.mddapi.dto.user.LoginRequestDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

/**
 * Validator to check that either username or email
 * is provided in the LoginRequestDTO.
 */
public class UsernameOrEmailValidator implements ConstraintValidator<UsernameOrEmailRequired, LoginRequestDTO> {

    @Override
    public boolean isValid(LoginRequestDTO loginRequestDTO, ConstraintValidatorContext constraintValidatorContext) {
        if (loginRequestDTO == null) return false;

        boolean hasUsername = StringUtils.isNotBlank(loginRequestDTO.getUsername());
        boolean hasEmail = StringUtils.isNotBlank(loginRequestDTO.getEmail());

        return hasUsername || hasEmail;
    }

}