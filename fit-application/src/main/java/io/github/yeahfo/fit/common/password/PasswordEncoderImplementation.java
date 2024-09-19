package io.github.yeahfo.fit.common.password;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoderImplementation implements PasswordEncoder {
    private static final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder = new BCryptPasswordEncoder( );

    @Override
    public String encode( CharSequence rawPassword ) {
        return passwordEncoder.encode( rawPassword );
    }

    @Override
    public boolean matches( CharSequence rawPassword, String encodedPassword ) {
        return passwordEncoder.matches( rawPassword, encodedPassword );
    }
}
