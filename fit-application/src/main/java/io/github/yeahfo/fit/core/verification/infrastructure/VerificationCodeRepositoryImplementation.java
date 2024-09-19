package io.github.yeahfo.fit.core.verification.infrastructure;

import io.github.yeahfo.fit.core.verification.domain.VerificationCode;
import io.github.yeahfo.fit.core.verification.domain.VerificationCodeType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface VerificationCodeRepositoryImplementation extends MongoRepository< VerificationCode, String > {
    Optional< VerificationCode > findByMobileEmailAndCodeAndType( String mobileEmail, String code, VerificationCodeType type );
}
