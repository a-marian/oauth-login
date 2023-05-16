package com.security.oauthlogin;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class AuthenticationController {

    @GetMapping("/welcome")
    public String homePage(HttpServletResponse response){
        Optional<String> username = getUsernameFromSecurityContext();
        if(username.isEmpty()){
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return "error";
        }
        return "hello "+ username.get() +", welcome!";
    }

    private Optional<String> getUsernameFromSecurityContext(){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Object principal = securityContext.getAuthentication().getPrincipal();

        if(!(principal instanceof DefaultOAuth2User)){
            return Optional.empty();
        }

        String username = ((DefaultOAuth2User) principal).getAttributes()
                .get("login").toString();
        return Optional.of(username);
    }


}
