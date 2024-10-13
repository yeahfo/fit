package io.github.yeahfo.fit.core.member.application;

import io.github.yeahfo.fit.core.common.application.Command;
import io.github.yeahfo.fit.core.common.validation.password.Password;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ResetMemberPasswordCommand( @NotNull
                                          @Password
                                          String password ) implements Command {
    @Override
    public void correctAndValidate( ) {

    }
}
