package io.github.yeahfo.fit.core.member.application;

import io.github.yeahfo.fit.core.common.application.Command;
import io.github.yeahfo.fit.core.common.domain.user.Role;
import lombok.Builder;

import static io.github.yeahfo.fit.core.common.domain.user.Role.ROBOT;
import static io.github.yeahfo.fit.core.common.exception.FitException.requestValidationException;

@Builder
public record UpdateMemberRoleCommand( Role role ) implements Command {
    @Override
    public void correctAndValidate( ) {
        if ( role == ROBOT ) {
            throw requestValidationException( "Role value not allowed." );
        }
    }
}
