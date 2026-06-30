package com.january0001.project.forumbackend.security.util;

import com.january0001.project.forumbackend.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserDetailUtil implements UserDetails {

    private final User user;

    public UserDetailUtil(User user) {
        this.user = user;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        if(user.getRole() != null) {
            String roleName = "ROLE_" + user.getRole().getRoleDescription().toUpperCase();
            authorities.add(new SimpleGrantedAuthority(roleName));

            if (user.getRole().getPermissions() != null && user.getRole().getPermissions().getCapabilities() != null) {
                for (String capability : user.getRole().getPermissions().getCapabilities()) {
                    authorities.add(new SimpleGrantedAuthority(capability));
                }
            }
        }
        return authorities; //this is required to ensure that we can actually get the authorities on a per-login basis. It also lets us NOT hardcode things and let the DB host the authorities and we just get them from BE.
    } //it also helps to not have capabilities: null in the DB. Kind of stupid that was.

    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getIsActive();
    }
}
