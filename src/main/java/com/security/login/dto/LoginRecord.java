package com.security.login.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LoginRecord(
                      @NotBlank(message = "Invalid username or mail: Empty name")
                      @NotNull(message = "Invalid username or mail: Name is NULL")
                      @Size(min = 3, max = 30, message = "Invalid username or mail: Must be of 3 - 20 characters")
                      String usernameOrMail,
                      @NotBlank(message = "Invalid password: Empty name")
                      @NotNull(message = "Invalid password: Name is NULL")
                      @Size(min = 3, max = 30, message = "Invalid password: Must be of 3 - 20 characters")
                      String password) {
}
