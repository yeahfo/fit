package io.github.yeahfo.fit.core.common.exception;

import io.github.yeahfo.fit.common.tracing.TracingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.ImmutableMap.toImmutableMap;
import static io.github.yeahfo.fit.core.common.exception.FitException.*;
import static org.apache.commons.lang3.StringUtils.isBlank;


@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private static final List< Integer > WARN_CODES = List.of( 400, 401, 403, 426, 429 );
    private final TracingService tracingService;


    @ResponseBody
    @ExceptionHandler( FitException.class )
    public ResponseEntity< ? > handleFitException( FitException ex, HttpServletRequest request ) {
        if ( WARN_CODES.contains( ex.getCode( ).getStatus( ) ) ) {
            log.warn( "Fit warning: {}", ex.getMessage( ) );
        } else {
            log.error( "Fit error: {}", ex.getMessage( ) );
        }

        return createErrorResponse( ex, request.getRequestURI( ) );
    }

    @ResponseBody
    @ExceptionHandler( { AccessDeniedException.class } )
    public ResponseEntity< ErrorResponse > handleAccessDinedException( HttpServletRequest request ) {
        return createErrorResponse( accessDeniedException( ), request.getRequestURI( ) );
    }

    @ResponseBody
    @ExceptionHandler( { AuthenticationException.class } )
    public ResponseEntity< ErrorResponse > handleAuthenticationFailedException( HttpServletRequest request ) {
        return createErrorResponse( authenticationException( ), request.getRequestURI( ) );
    }

    @ResponseBody
    @ExceptionHandler( { MethodArgumentNotValidException.class } )
    public ResponseEntity< ErrorResponse > handleInvalidRequest( MethodArgumentNotValidException ex, HttpServletRequest request ) {
        Map< String, Object > error = ex.getBindingResult( ).getFieldErrors( ).stream( )
                .collect( toImmutableMap( FieldError::getField, fieldError -> {
                    String message = fieldError.getDefaultMessage( );
                    return isBlank( message ) ? "无错误提示。" : message;
                }, ( field1, field2 ) -> field1 + "|" + field2 ) );

        log.error( "Method argument validation error[{}]: {}", ex.getParameter( ).getParameterType( ).getName( ), error );
        FitException exception = requestValidationException( error );
        return createErrorResponse( exception, request.getRequestURI( ) );
    }

    @ResponseBody
    @ExceptionHandler( { ServletRequestBindingException.class, HttpMessageNotReadableException.class, ConstraintViolationException.class } )
    public ResponseEntity< ErrorResponse > handleServletRequestBindingException( Exception ex, HttpServletRequest request ) {
        FitException exception = requestValidationException( "message", "请求验证失败。" );
        log.error( "Request processing error: {}", ex.getMessage( ) );
        return createErrorResponse( exception, request.getRequestURI( ) );
    }

    @ResponseBody
    @ExceptionHandler( Throwable.class )
    public ResponseEntity< ? > handleGeneralException( Throwable ex, HttpServletRequest request ) {
        String path = request.getRequestURI( );
        String traceId = tracingService.currentTraceId( );

        log.error( "Error access[{}]:", path, ex );
        Error error = new Error( ErrorCode.SYSTEM_ERROR, ErrorCode.SYSTEM_ERROR.getStatus( ), "系统错误。", path, traceId, null );
        return new ResponseEntity<>( error.toErrorResponse( ), new HttpHeaders( ), HttpStatus.valueOf( ErrorCode.SYSTEM_ERROR.getStatus( ) ) );
    }

    private ResponseEntity< ErrorResponse > createErrorResponse( FitException exception, String path ) {
        String traceId = tracingService.currentTraceId( );
        Error error = new Error( exception, path, traceId );
        ErrorResponse response = error.toErrorResponse( );
        return new ResponseEntity<>( response, new HttpHeaders( ), HttpStatus.valueOf( response.error( ).status( ) ) );
    }

}
