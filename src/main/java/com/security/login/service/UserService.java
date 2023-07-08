package com.security.login.service;

import com.security.login.dto.UserRecord;
import com.security.login.exception.UserNotSavedException;
import com.security.login.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    UserRecord save(UserRecord userDto) throws UserNotSavedException;

    List<User> getAll();

   UserRecord getUser(Long id);

   boolean existsUser(String email);

    String generateToken(String username);

    void validateToken(String username);

    void increaseFailedAttempts(User user);
    void resetFailedAttempts(String mail);
    void lock(User user);
    User findUser(String usernameOrMail);
}
