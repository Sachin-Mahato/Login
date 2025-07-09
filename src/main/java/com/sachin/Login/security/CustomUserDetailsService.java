package com.sachin.Login.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sachin.Login.domain.entities.UserModel;
import com.sachin.Login.repository.UserRepository;

import org.springframework.security.core.userdetails.UserDetailsService;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository repo;

    public CustomUserDetailsService(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        UserModel user = repo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "No user found with username or email: " + email));

        return new UserPrincipal(user);
    }

}
