package io.github.yeahfo.fit.core.verification;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping( "/verification-codes" )
@Tag( name = "Verification code", description = "Create various types of verification codes" )
public class VerificationResource {
}
