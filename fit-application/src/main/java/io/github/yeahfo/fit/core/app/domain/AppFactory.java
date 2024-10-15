package io.github.yeahfo.fit.core.app.domain;

import io.github.yeahfo.fit.core.common.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppFactory {

    public CreateAppResult create( String name, User user ) {
        return null;
    }
}
