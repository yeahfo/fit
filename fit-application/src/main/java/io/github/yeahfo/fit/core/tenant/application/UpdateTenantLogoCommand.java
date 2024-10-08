package io.github.yeahfo.fit.core.tenant.application;

import io.github.yeahfo.fit.core.common.application.Command;
import io.github.yeahfo.fit.core.common.domain.UploadedFile;
import jakarta.validation.Valid;
import lombok.Builder;

@Builder
public record UpdateTenantLogoCommand( @Valid UploadedFile logo ) implements Command {
    @Override
    public void correctAndValidate( ) {

    }
}
