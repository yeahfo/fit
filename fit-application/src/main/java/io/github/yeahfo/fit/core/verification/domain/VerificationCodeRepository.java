package io.github.yeahfo.fit.core.verification.domain;

import io.github.yeahfo.fit.core.common.domain.AggregateRootRepository;

import java.util.Optional;

public interface VerificationCodeRepository extends AggregateRootRepository< VerificationCode, String > {
    boolean existsWithinOneMinutes( String mobileOrEmail, VerificationCodeType type );

    long totalCodeCountOfTodayFor( String mobileOrEmail );

    Optional< VerificationCode > findByMobileEmailAndCodeAndType( String mobileEmail, String code, VerificationCodeType type );
}
