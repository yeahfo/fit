package io.github.yeahfo.fit.core.app;

import io.github.yeahfo.fit.core.app.application.AppCommandService;
import io.github.yeahfo.fit.core.app.application.CreateAppCommand;
import io.github.yeahfo.fit.core.app.application.CreateAppResponse;
import io.github.yeahfo.fit.core.common.domain.user.User;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static io.github.yeahfo.fit.common.swagger.SwaggerConfiguration.AUTHORIZATION_BEARER_TOKEN;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping( value = "/apps" )
@Tag( name = "App", description = "App apis" )
public class AppResource {
    private final AppCommandService appCommandService;

    @ResponseStatus( CREATED )
    @SecurityRequirement( name = AUTHORIZATION_BEARER_TOKEN )
    @PostMapping( consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE )
    public CreateAppResponse createApp( @RequestBody @Valid CreateAppCommand command,
                                        @AuthenticationPrincipal User user ) {
        return appCommandService.createApp( command, user );
    }
}
