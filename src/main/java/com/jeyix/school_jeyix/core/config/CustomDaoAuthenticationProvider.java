package com.jeyix.school_jeyix.core.config;

import com.jeyix.school_jeyix.core.security.model.User;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomDaoAuthenticationProvider extends DaoAuthenticationProvider {

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
            UsernamePasswordAuthenticationToken authentication)
            throws BadCredentialsException {

        if (userDetails instanceof User user) {
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                throw new BadCredentialsException(
                        "Este usuario no tiene contraseña. Usa OAuth o agrega una contraseña.");
            }
        }

        super.additionalAuthenticationChecks(userDetails, authentication);
    }
}
