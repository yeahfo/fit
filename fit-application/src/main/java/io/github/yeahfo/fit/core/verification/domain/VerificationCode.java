package io.github.yeahfo.fit.core.verification.domain;

import io.github.yeahfo.fit.core.common.domain.AggregateRoot;
import io.github.yeahfo.fit.core.common.domain.user.User;
import io.github.yeahfo.fit.core.common.exception.FitException;

import static io.github.yeahfo.fit.core.common.exception.ErrorCode.VERIFICATION_CODE_COUNT_OVERFLOW;
import static io.github.yeahfo.fit.core.common.utils.Identified.newVerificationCodeId;
import static io.github.yeahfo.fit.core.common.utils.FitConstants.NO_TENANT_ID;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class VerificationCode extends AggregateRoot {
    protected String mobileEmail;//邮箱或手机号
    protected String code;//6位数验证码
    protected VerificationCodeType type;//验证码用于的类型
    protected int usedCount;//已经使用的次数，使用次数不能超过3次

    protected VerificationCode( ) {
    }

    protected VerificationCode( String mobileEmail, VerificationCodeType type, String tenantId, User user ) {
        super( newVerificationCodeId( ), isNotBlank( tenantId ) ? tenantId : NO_TENANT_ID, user );
        this.mobileEmail = mobileEmail;
        this.code = randomNumeric( 6 );
        this.type = type;
        this.usedCount = 0;
    }

    public void use( ) {
        if ( usedCount >= 3 ) {
            throw new FitException( VERIFICATION_CODE_COUNT_OVERFLOW, "验证码已超过可使用次数。" );
        }

        this.usedCount++;
    }

    public String mobileEmail( ) {
        return mobileEmail;
    }

    public String code( ) {
        return code;
    }
}
