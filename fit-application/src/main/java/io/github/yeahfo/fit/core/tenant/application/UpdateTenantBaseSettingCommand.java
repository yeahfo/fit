package io.github.yeahfo.fit.core.tenant.application;

import io.github.yeahfo.fit.core.common.application.Command;
import io.github.yeahfo.fit.core.common.domain.UploadedFile;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import static io.github.yeahfo.fit.core.common.utils.FitConstants.MAX_GENERIC_NAME_LENGTH;

@Builder
public record UpdateTenantBaseSettingCommand( @NotBlank
                                              @Size( max = MAX_GENERIC_NAME_LENGTH )
                                              String name,
                                              @Valid
                                              UploadedFile loginBackground ) implements Command {
    @Override
    public void correctAndValidate( ) {

    }
}
