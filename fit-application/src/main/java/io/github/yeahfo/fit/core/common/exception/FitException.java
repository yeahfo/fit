package io.github.yeahfo.fit.core.common.exception;

import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.collections4.MapUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class FitException extends RuntimeException {

    private final ErrorCode code;
    private final Map< String, Object > data = new HashMap<>( );
    private String message;
    private final String userMessage;

    public FitException( ErrorCode code, String userMessage ) {
        this.code = code;
        this.userMessage = userMessage;
        this.message = message( userMessage );
    }

    public FitException( ErrorCode code, String userMessage,
                         String key, Object value ) {
        this.code = code;
        addData( key, value );
        this.userMessage = userMessage;
        this.message = message( userMessage );
    }

    public FitException( ErrorCode code, String userMessage,
                         String key1, Object value1,
                         String key2, Object value2 ) {
        this.code = code;
        addData( key1, value1 );
        addData( key2, value2 );
        this.userMessage = userMessage;
        this.message = message( userMessage );
    }

    public FitException( ErrorCode code, String userMessage,
                         String key1, Object value1,
                         String key2, Object value2,
                         String key3, Object value3 ) {
        this.code = code;
        addData( key1, value1 );
        addData( key2, value2 );
        addData( key3, value3 );
        this.userMessage = userMessage;
        this.message = message( userMessage );
    }

    public FitException( ErrorCode code, String userMessage,
                         String key1, Object value1,
                         String key2, Object value2,
                         String key3, Object value3,
                         String key4, Object value4 ) {
        this.code = code;
        addData( key1, value1 );
        addData( key2, value2 );
        addData( key3, value3 );
        addData( key4, value4 );
        this.userMessage = userMessage;
        this.message = message( userMessage );
    }

    public FitException( ErrorCode code, String userMessage, Map< String, Object > data ) {
        this.code = code;
        this.data.putAll( data );
        this.userMessage = userMessage;
        this.message = message( userMessage );
    }

    private String message( String userMessage ) {
        StringBuilder stringBuilder = new StringBuilder( ).append( "[" ).append( this.code.toString( ) ).append( "]" );

        if ( isNotBlank( userMessage ) ) {
            stringBuilder.append( userMessage );
        }

        if ( isNotEmpty( this.data ) ) {
            stringBuilder.append( "Data: " ).append( this.data );
        }

        return stringBuilder.toString( );
    }

    public static FitException requestValidationException( Map< String, Object > data ) {
        return new FitException( ErrorCode.REQUEST_VALIDATION_FAILED, "请求数据验证失败。", data );
    }

    public static FitException requestValidationException( String key, Object value ) {
        return new FitException( ErrorCode.REQUEST_VALIDATION_FAILED, "请求数据验证失败。", key, value );
    }

    public static FitException requestValidationException( String message ) {
        return new FitException( ErrorCode.REQUEST_VALIDATION_FAILED, message );
    }

    public static FitException accessDeniedException( ) {
        return new FitException( ErrorCode.ACCESS_DENIED, "权限不足。" );
    }

    public static FitException accessDeniedException( String userMessage ) {
        return new FitException( ErrorCode.ACCESS_DENIED, userMessage );
    }

    public static FitException authenticationException( ) {
        return new FitException( ErrorCode.AUTHENTICATION_FAILED, "登录失败。" );
    }

    public void addData( String key, Object value ) {
        this.data.put( key, value );
        this.message = message( this.userMessage );
    }
}
