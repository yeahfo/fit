package io.github.yeahfo.fit.core.verification.domain;

public interface VerificationCodeSender {
    void send( VerificationCode code );
}
