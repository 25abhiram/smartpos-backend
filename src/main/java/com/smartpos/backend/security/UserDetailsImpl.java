package com.smartpos.backend.security;

import com.smartpos.backend.entity.Permission;
import com.smartpos.backend.entity.Role;
import com.smartpos.backend.entity.User;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class UserDetailsImpl implements UserDetails {
    private User user;

    public UserDetailsImpl(User user){
        this.user=user;
    }

    public User getUser(){
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities=new HashSet<>();
        for (Role role: user.getRoles()){
            authorities.add(new SimpleGrantedAuthority("ROLE_"+role.getName().name()));
//            for (Permission permission: role.getPermissions()){
//                authorities.add(new SimpleGrantedAuthority(permission.getName()));
//            }
        }
        return authorities;
    }

    @Override
    public @Nullable String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }
}
