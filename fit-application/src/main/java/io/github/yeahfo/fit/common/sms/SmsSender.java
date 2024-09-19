package io.github.yeahfo.fit.common.sms;

public interface SmsSender {
    boolean sendVerificationCode( String mobile, String code );
}
