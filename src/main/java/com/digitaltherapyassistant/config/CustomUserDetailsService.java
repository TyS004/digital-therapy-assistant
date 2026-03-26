package com.digitaltherapyassistant.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.digitaltherapyassistant.entity.User;
import com.digitaltherapyassistant.repository.UserRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * ============================================================================
 * CUSTOM USER DETAILS SERVICE — SPRING SECURITY INTEGRATION
 * ============================================================================
 *
 * CONCEPT: UserDetailsService
 * ------------------------------
 * UserDetailsService is a core interface in Spring Security. It defines a
 * single method: loadUserByUsername(). Spring Security calls this during
 * authentication to load user data from your data store.
 *
 * The returned UserDetails object contains:
 *   - Username and password (for credential verification)
 *   - Granted authorities (roles/permissions for authorization)
 *   - Account status flags (enabled, locked, expired, etc.)
 *
 * CONCEPT: GrantedAuthority
 * ---------------------------
 * GrantedAuthority represents a permission or role assigned to a user.
 * Spring Security uses the "ROLE_" prefix convention:
 *   - UserRole.SALES_REP → ROLE_SALES_REP
 *   - UserRole.SYSTEM_ADMIN → ROLE_SYSTEM_ADMIN
 *
 * In authorization rules, hasRole("SALES_REP") automatically checks for
 * the "ROLE_SALES_REP" authority.
 *
 * CONCEPT: @Service
 * -------------------
 * @Service is a specialization of @Component that indicates this class
 * contains business logic. It's functionally identical to @Component but
 * communicates intent — this bean is part of the service/business layer.
 *
 * @see org.springframework.security.core.userdetails.UserDetailsService
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                    "User not found with email: " + email
                ));

        return userRepository.findByEmail(email)
        .map(userL -> new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getPasswordHash(),
            List.of())
        )
        .orElseThrow(() -> new UsernameNotFoundException("Not found"));          
    }
}