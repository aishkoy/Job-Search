package kg.attractor.job_search.config;

import kg.attractor.job_search.dto.user.UserDto;
import kg.attractor.job_search.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class AuthAdapter {

    private final UserService service;

    public UserDto getAuthUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new NoSuchElementException("user not authorized");
        }
        if (authentication instanceof AnonymousAuthenticationToken){
            throw new IllegalArgumentException("user not authorized");
        }
        String email = authentication.getName();
        return service.getUserByEmail(email);
    }

    public Long getAuthId(){
        return getAuthUser().getId();
    }
}
