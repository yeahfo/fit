package io.github.yeahfo.fit.core.member;

import io.github.yeahfo.fit.core.common.application.IdentifierResponse;
import io.github.yeahfo.fit.core.common.domain.user.User;
import io.github.yeahfo.fit.core.member.application.CreateMemberCommand;
import io.github.yeahfo.fit.core.member.application.MemberCommandService;
import io.github.yeahfo.fit.core.member.application.MemberImportResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static io.github.yeahfo.fit.common.swagger.SwaggerConfiguration.AUTHORIZATION_BEARER_TOKEN;
import static io.github.yeahfo.fit.core.common.application.IdentifierResponse.identifier;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping( "/members" )
@Tag( name = "Member", description = "Member APIs" )
public class MemberResource {

    private final MemberCommandService memberCommandService;


    @ResponseStatus( CREATED )
    @SecurityRequirement( name = AUTHORIZATION_BEARER_TOKEN )
    @PostMapping( consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE )
    public IdentifierResponse createMember( @RequestBody @Valid CreateMemberCommand command,
                                            @AuthenticationPrincipal User user ) {
        String memberId = memberCommandService.createMember( command, user );
        return identifier( memberId );
    }

    @ResponseStatus( CREATED )
    @SecurityRequirement( name = AUTHORIZATION_BEARER_TOKEN )
    @PostMapping( value = "/import", consumes = MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE )
    public MemberImportResponse importMembers( @RequestParam @NotNull MultipartFile file,
                                               @AuthenticationPrincipal User user ) throws IOException {
        return memberCommandService.importMembers( file.getInputStream( ), user );
    }
}
