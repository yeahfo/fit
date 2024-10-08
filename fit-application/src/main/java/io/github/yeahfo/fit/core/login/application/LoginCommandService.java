package io.github.yeahfo.fit.core.login.application;

import io.github.yeahfo.fit.common.ratelimit.RateLimiter;
import io.github.yeahfo.fit.core.common.domain.user.User;
import io.github.yeahfo.fit.core.common.exception.ErrorCode;
import io.github.yeahfo.fit.core.common.exception.FitException;
import io.github.yeahfo.fit.core.login.domain.LoginDomainService;
import io.github.yeahfo.fit.core.member.domain.Member;
import io.github.yeahfo.fit.core.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static io.github.yeahfo.fit.core.common.exception.FitException.authenticationException;
import static io.github.yeahfo.fit.core.common.utils.CommonUtils.maskMobileOrEmail;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginCommandService {
    private final RateLimiter rateLimiter;
    private final MemberRepository memberRepository;
    private final LoginDomainService loginDomainService;

    @Transactional
    public String loginWithMobileOrEmail( MobileOrEmailLoginCommand command ) {
        String mobileOrEmail = command.mobileOrEmail( );
        rateLimiter.applyFor( "Login:MobileOrEmail:" + mobileOrEmail, 1 );

        try {
            String token = loginDomainService.loginWithMobileOrEmail( mobileOrEmail, command.password( ) );
            log.info( "User[{}] logged in using password.", maskMobileOrEmail( mobileOrEmail ) );
            return token;
        } catch ( Throwable t ) {
            //401或409时直接抛出异常
            if ( t instanceof FitException fitException &&
                    ( fitException.getCode( ).getStatus( ) == 401 || fitException.getCode( ).getStatus( ) == 409 ) ) {
                log.warn( "Password login failed for [{}].", maskMobileOrEmail( mobileOrEmail ) );
                throw fitException;
            }

            //其他情况直接一个笼统的异常
            log.warn( "Password login failed for [{}].", maskMobileOrEmail( mobileOrEmail ), t );
            throw authenticationException( );
        }
    }

    @Transactional
    public String loginWithVerificationCode( VerificationCodeLoginCommand command ) {
        String mobileOrEmail = command.mobileOrEmail( );

        rateLimiter.applyFor( "Login:MobileOrEmail:" + mobileOrEmail, 1 );

        try {
            String token = loginDomainService.loginWithVerificationCode(
                    mobileOrEmail,
                    command.verification( ) );
            log.info( "User[{}] logged in using verification code.", maskMobileOrEmail( mobileOrEmail ) );
            return token;
        } catch ( Throwable t ) {
            //401或409时直接抛出异常
            if ( t instanceof FitException fitException &&
                    ( fitException.getCode( ).getStatus( ) == 401 || fitException.getCode( ).getStatus( ) == 409 ) ) {
                log.warn( "Verification code login failed for [{}].", maskMobileOrEmail( mobileOrEmail ) );
                throw fitException;
            }

            //其他情况直接一个笼统的异常
            log.warn( "Verification code login failed for [{}].", maskMobileOrEmail( mobileOrEmail ), t );
            throw authenticationException( );
        }
    }

    @Transactional
    public String refreshToken( User user ) {
        rateLimiter.applyFor( "Login:RefreshToken:All", 1000 );
        Member member = memberRepository.findById( user.memberId( ) ).orElseThrow( ( )
                -> new FitException( ErrorCode.MEMBER_NOT_FOUND, "无法获得登录成员信息." ) );
        log.info( "User[{}] refreshed token.", user.memberId( ) );
        return loginDomainService.refreshToken( member.identifier( ) );
    }
}
