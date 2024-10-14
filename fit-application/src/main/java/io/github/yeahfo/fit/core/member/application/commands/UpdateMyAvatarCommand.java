package io.github.yeahfo.fit.core.member.application.commands;

import io.github.yeahfo.fit.core.common.application.Command;
import io.github.yeahfo.fit.core.common.domain.UploadedFile;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UpdateMyAvatarCommand( @Valid
                                     @NotNull
                                     UploadedFile avatar ) implements Command {
    @Override
    public void correctAndValidate( ) {

    }
}
