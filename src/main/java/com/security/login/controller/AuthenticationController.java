package com.security.login.controller;

import com.security.login.dto.LoginRecord;
import com.security.login.dto.UserRecord;
import com.security.login.exception.UserNotSavedException;
import com.security.login.repository.AttemptsRepository;
import com.security.login.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);
    private static final String TOKEN_PREFIX = "Bearer";
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    private AttemptsRepository attemptsRepository;


    public AuthenticationController(@Autowired UserService userService){
        super();
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRecord userRecord)
            throws UserNotSavedException {
        if(userService.existsUser(userRecord.email())){
            throw new IllegalArgumentException("There is an account with that email address " + userRecord.email());
        }
        userService.save(userRecord);
        return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRecord loginRecord){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRecord.usernameOrMail(), loginRecord.password()));
        if(authentication.isAuthenticated()){
            String token = userService.generateToken(loginRecord.usernameOrMail());
            return  ResponseEntity.ok(token);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new ResponseEntity<>("User signed-in successfully!", HttpStatus.OK);
    }

    @GetMapping("/validate")
    public String validateToken(@RequestHeader("Authorization") String authorization){
        LOGGER.info("token received:  {}", authorization);

        return "Token valid";
    }


    @GetMapping("find/{id}")
    public UserRecord getUser(@PathVariable("id") long id){
        LOGGER.info("user to find:  {}", id );
        return userService.getUser(id);
    }

    @GetMapping("/home")
    public String home(){
        return "index";
    }


}
