package com.security.login.service;

import com.security.login.dto.UserRecord;
import com.security.login.exception.UserNotSavedException;
import com.security.login.model.Attempt;
import com.security.login.model.Role;
import com.security.login.model.User;
import com.security.login.repository.AttemptsRepository;
import com.security.login.repository.RoleRepository;
import com.security.login.repository.UserRepository;
import com.security.login.util.CustomPasswordEncoder;
import com.security.login.util.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private CustomPasswordEncoder passwordEncoder;
    private JwtTokenUtil jwtTokenUtil;
    private AttemptsRepository attemptsRepository;

    public UserServiceImpl(@Autowired UserRepository userRepository,
                           @Autowired RoleRepository roleRepository,
                           @Autowired CustomPasswordEncoder passwordEncoder,
                           @Autowired JwtTokenUtil jwtTokenUtil,
                           @Autowired AttemptsRepository attemptsRepository){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.attemptsRepository = attemptsRepository;
    }
    @Override
    public UserRecord save(UserRecord userRecord) throws UserNotSavedException {
        User user = new User();
            user.setFirstName(userRecord.firstName());
            user.setUsername(userRecord.username());
            user.setLastName(userRecord.lastName());
            user.setEmail(userRecord.email());
            user.setPassword(passwordEncoder.encode(userRecord.password()));
         try {
             Optional<Role> role = Optional.of(roleRepository.findByName("ROLE_USER"))
                     .orElseThrow(() -> new RoleNotFoundException("Role ROLE_USER not found"));

             user.setRoles(List.of(role.get()));
             userRepository.save(user);
         } catch(Exception e){
             LOGGER.error("User " +userRecord.email()+ " was not saved", e.getLocalizedMessage());
             throw new UserNotSavedException("User was not saved", e.getCause());
         }
         return userRecord;
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public UserRecord getUser(Long id) {
       User user = Optional.of(userRepository.findById(id)).get().orElseThrow();
       UserRecord userRecord = new UserRecord(user.getFirstName(),user.getLastName(),
               user.getUsername(), user.getEmail(),user.getPassword());
       return userRecord;
    }

    @Override
    public boolean existsUser(String email) {
       return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail)
            throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username or mail:"+ usernameOrEmail));

        return new org.springframework.security.core
                .userdetails.User(user.getEmail(), user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
    @Override
    public String generateToken(String username){
        return jwtTokenUtil.generate(username);
    }
    @Override
    public void validateToken(String token){
        jwtTokenUtil.validate(token);
    }

    @Override
    public void increaseFailedAttempts(User user) {
       Attempt failedAttempts = user.getLoginAttempts();
       if(failedAttempts != null){
           int updatedFailedAttempts = failedAttempts.getCount()+1;
           failedAttempts.setCount(updatedFailedAttempts);
           attemptsRepository.save(failedAttempts);
       } else {
           Attempt newFailedAttempts = new Attempt();
           newFailedAttempts.setUser(user);
           newFailedAttempts.setCount(1);
           newFailedAttempts.setUsername(user.getUsername());
           attemptsRepository.save(newFailedAttempts);
       }

    }

    @Override
    public void resetFailedAttempts(String mail) {
        Attempt attempts = attemptsRepository.findAttemptsByUsername(mail)
                .orElseThrow(() -> new IllegalArgumentException("not attempts for this user"));
        attempts.setCount(0);
        attemptsRepository.save(attempts);
    }

    @Override
    public void lock(User user) {
        user.setAccountNonLocked(false);
        user.setLockTime(new Date());
        userRepository.save(user);
    }

    @Override
    public User findUser(String usernameOrMail) {
        User user = userRepository.findByUsernameOrEmail(usernameOrMail, usernameOrMail)
                .orElseThrow(() -> new UsernameNotFoundException("user was not found"));
        return user;
    }
}
