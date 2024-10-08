package io.github.yeahfo.fit.core.login.domain;

import io.github.yeahfo.fit.common.password.PasswordEncoder;
import io.github.yeahfo.fit.common.security.jwt.JwtService;
import io.github.yeahfo.fit.core.common.exception.FitException;
import io.github.yeahfo.fit.core.member.domain.Member;
import io.github.yeahfo.fit.core.member.domain.MemberDomainService;
import io.github.yeahfo.fit.core.member.domain.MemberRepository;
import io.github.yeahfo.fit.core.verification.domain.VerificationCodeChecker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static io.github.yeahfo.fit.core.common.exception.FitException.authenticationException;
import static io.github.yeahfo.fit.core.verification.domain.VerificationCodeType.LOGIN;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginDomainService {
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final MemberDomainService memberDomainService;
    private final VerificationCodeChecker verificationCodeChecker;

    public String loginWithMobileOrEmail( String mobileOrEmail,
                                          String password ) {
        Member member = memberRepository.findByMobileOrEmail( mobileOrEmail )
                .orElseThrow( FitException::authenticationException );

        if ( !passwordEncoder.matches( password, member.password( ) ) ) {
            memberDomainService.recordMemberFailedLogin( member );
            throw authenticationException( );
        }

        member.checkActive( );
        return jwtService.generateJwt( member.identifier( ) );
    }

    public String loginWithVerificationCode( String mobileOrEmail, String verificationCode ) {
        verificationCodeChecker.check( mobileOrEmail, verificationCode, LOGIN );
        Member member = memberRepository.findByMobileOrEmail( mobileOrEmail )
                .orElseThrow( FitException::authenticationException );

        member.checkActive( );
        return jwtService.generateJwt( member.identifier( ) );
    }

    public String refreshToken( String memberId ) {
        return jwtService.generateJwt( memberId );
    }
}
