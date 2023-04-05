package com.tafakkoor.e_learn.config.security;

import com.tafakkoor.e_learn.domain.AuthUser;
import com.tafakkoor.e_learn.enums.Levels;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class UserSession {
    public AuthUser getUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        var authUserDetails = authentication.getPrincipal();
        if (authUserDetails instanceof AuthUserUserDetails a)
            return a.getAuthUser();
        return null;
    }

    public Long getId() {
        AuthUser user = getUser();
        if (user != null)
            return user.getId();
        return null;
    }
    public Levels getLevel() {
        AuthUser user = getUser();
        if (user != null)
            return user.getLevel();
        return null;
    }
}
