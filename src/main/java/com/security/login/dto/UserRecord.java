package com.security.login.dto;


import com.security.login.validators.ValidMail;
import com.security.login.validators.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRecord(
        @NotBlank(message = "Invalid First Name: Empty name")
        @NotNull(message = "Invalid First Name: Name is NULL")
        @Size(min = 3, max = 30, message = "Invalid First Name: Must be of 3 - 20 characters")
        String firstName,
        @NotBlank(message = "Invalid Last Name: Empty name")
        @NotNull(message = "Invalid Last Name: Name is NULL")
        @Size(min = 3, max = 30, message = "Invalid First Name: Must be of 3 - 20 characters")
        String lastName,
        @NotNull(message = "Username should not be null")
        @NotBlank(message = "Username should not be empty")
        String username,
        @ValidMail
        @NotNull(message = "Email cannot be null")
        @NotEmpty(message = "Email cannot be empty")
        String email,
        @NotNull(message="Password cannot be null")
        @NotEmpty(message = "Password cannot be empty")
        @ValidPassword
        String password) {
}
