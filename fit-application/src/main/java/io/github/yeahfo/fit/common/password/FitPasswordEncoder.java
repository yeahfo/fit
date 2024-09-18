package io.github.yeahfo.fit.common.password;

public interface FitPasswordEncoder {
    String encode( CharSequence rawPassword );

    boolean matches( CharSequence rawPassword, String encodedPassword );
}
