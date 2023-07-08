package com.security.login.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.security.login.model.Attempt;
import com.security.login.model.User;
import com.security.login.repository.AttemptsRepository;
import com.security.login.repository.UserRepository;
import com.security.login.service.UserService;
import io.jsonwebtoken.lang.Assert;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.util.Map;

public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    public static final Logger LOGGER = LoggerFactory.getLogger(LoginFailureHandler.class);
    private final int MAX_FAILED_ATTEMPTS = 3;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private AttemptsRepository attemptsRepository;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
       byte[] inputStreamBytes = StreamUtils.copyToByteArray(request.getInputStream());
        Map<String, String> jsonRequest = new ObjectMapper().readValue(inputStreamBytes, Map.class);
        String username = jsonRequest.get("usernameOrMail");
        Assert.notNull(username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("user not found"));



        if(user.isEnabled() && user.isAccountNonLocked()) {
            if (user.getLoginAttempts() == null) {
                Attempt attemptUser = new Attempt();
                attemptUser.setCount(1);
                attemptUser.setUser(user);
                attemptUser.setUsername(user.getUsername());
                attemptsRepository.save(attemptUser);
                LOGGER.info("user {} has increased login attempts", user.getUsername());
                exception = new LockedException("Your user or password are not correct, please try again") {
                };
            } else if (user.getLoginAttempts().getCount() < MAX_FAILED_ATTEMPTS - 1) {
                userService.increaseFailedAttempts(user);
                LOGGER.info("user {} has increased login attempts", user.getUsername());
                exception = new LockedException("Your user or password are not correct, please try again") {
                };

            } else {
                userService.lock(user);
                exception = new LockedException("Your account has been locked due to 3 failed attempts," +
                        " after 24 hrs., your account will be unlocked.");
            }
        }

        super.onAuthenticationFailure(request, response, exception);
    }
}
