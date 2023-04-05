package com.tafakkoor.e_learn.config.security;

import com.tafakkoor.e_learn.domain.AuthPermission;
import com.tafakkoor.e_learn.domain.AuthRole;
import com.tafakkoor.e_learn.domain.AuthUser;
import com.tafakkoor.e_learn.enums.Status;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class AuthUserUserDetails implements UserDetails {

    private final AuthUser authUser;

    public AuthUserUserDetails(AuthUser authUser) {
        this.authUser = authUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        var authRoles = Objects.requireNonNullElse(authUser.getAuthRoles(), Collections.<AuthRole>emptySet());
        var authorities = new ArrayList<SimpleGrantedAuthority>();
        authRoles.forEach(authRole -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + authRole.getCode()));
            Collection<AuthPermission> authPermissions = Objects.requireNonNullElse(authRole.getAuthPermissions(), Collections.<AuthPermission>emptySet());
            authPermissions.forEach(authPermission -> {
                authorities.add(new SimpleGrantedAuthority(authPermission.getCode()));
            });
        });
        return authorities;
    }

    @Override
    public String getPassword() {
        return authUser.getPassword();
    }


    public AuthUser getAuthUser() {
        return authUser;
    }

    public Long getId() {
        return authUser.getId();
    }

    @Override
    public String getUsername() {
        return authUser.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !authUser.getStatus().equals(Status.BLOCKED);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return Status.ACTIVE.equals(authUser.getStatus());
    }
    public void setLastLogin(LocalDateTime lastLogin) {
        authUser.setLastLogin(lastLogin);
    }
}
