package io.github.yeahfo.fit.core.register;

import io.github.yeahfo.fit.core.register.application.RegisterCommand;
import io.github.yeahfo.fit.core.register.application.RegisterCommandService;
import io.github.yeahfo.fit.core.register.application.RegisteredResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping( "/registrations" )
@Tag( name = "Registration", description = "Registration APIs" )
public class RegisterResource {
    private final RegisterCommandService registerCommandService;

    @ResponseStatus( CREATED )
    @PostMapping( consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE )
    public RegisteredResponse register( @RequestBody @Valid RegisterCommand command ) {
        return registerCommandService.register( command );
    }
}
