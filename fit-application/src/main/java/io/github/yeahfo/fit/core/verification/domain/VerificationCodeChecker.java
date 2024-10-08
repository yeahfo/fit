package io.github.yeahfo.fit.core.verification.domain;

import io.github.yeahfo.fit.core.common.exception.FitException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static io.github.yeahfo.fit.core.common.exception.ErrorCode.VERIFICATION_CODE_CHECK_FAILED;
import static io.github.yeahfo.fit.core.common.utils.CommonUtils.maskMobileOrEmail;
import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Component
public class VerificationCodeChecker {
    private final VerificationCodeRepository verificationCodeRepository;

    public VerificationCodeChecker( VerificationCodeRepository verificationCodeRepository ) {
        this.verificationCodeRepository = verificationCodeRepository;
    }

    // REQUIRES_NEW 表示无论最终结果成败，只要check了一次，便标记使用一次
    @Transactional( propagation = REQUIRES_NEW )
    public void check( String mobileEmail, String code, VerificationCodeType type ) {
        VerificationCode verificationCode = verificationCodeRepository.findByMobileEmailAndCodeAndType( mobileEmail, code, type )
                .orElseThrow( ( ) -> new FitException( VERIFICATION_CODE_CHECK_FAILED, "验证码验证失败。",
                        "mobileOrEmail", maskMobileOrEmail( mobileEmail ) ) );
        verificationCode.use( );
        verificationCodeRepository.save( verificationCode );
    }
}
