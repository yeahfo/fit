package io.github.yeahfo.fit.common.password;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class FitPasswordEncoderImplementation implements FitPasswordEncoder {
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder( );

    @Override
    public String encode( CharSequence rawPassword ) {
        return passwordEncoder.encode( rawPassword );
    }

    @Override
    public boolean matches( CharSequence rawPassword, String encodedPassword ) {
        return passwordEncoder.matches( rawPassword, encodedPassword );
    }
}
