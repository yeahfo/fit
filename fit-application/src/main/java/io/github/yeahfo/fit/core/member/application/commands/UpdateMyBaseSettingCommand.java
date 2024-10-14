package io.github.yeahfo.fit.core.member.application.commands;

import io.github.yeahfo.fit.core.common.application.Command;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import static io.github.yeahfo.fit.core.common.utils.FitConstants.MAX_GENERIC_NAME_LENGTH;

@Builder
public record UpdateMyBaseSettingCommand( @NotBlank
                                          @Size( max = MAX_GENERIC_NAME_LENGTH )
                                          String name ) implements Command {
    @Override
    public void correctAndValidate( ) {

    }
}
