package io.github.yeahfo.fit.core.common.exception;

import io.github.yeahfo.fit.common.tracing.TracingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

import static org.springframework.boot.web.error.ErrorAttributeOptions.defaults;
import static org.springframework.http.HttpStatus.valueOf;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RestErrorController implements ErrorController {
    private static final String ERROR_PATH = "/error";
    private final ErrorAttributes errorAttributes;
    private final TracingService tracingService;

    @RequestMapping( value = ERROR_PATH )
    public ResponseEntity< ? > handleError( WebRequest webRequest ) {
        Map< String, Object > errorAttributes = getErrorAttributes( webRequest );
        String error = ( String ) errorAttributes.get( "error" );
        int status = ( int ) errorAttributes.get( "status" );
        String message = ( String ) errorAttributes.get( "message" );
        String path = ( String ) errorAttributes.get( "path" );
        String traceId = tracingService.currentTraceId( );

        log.error( "Error access[{}]:{}.", path, message );
        Error errorDetail = new Error( ErrorCode.SYSTEM_ERROR, status, error, path, traceId, null );
        return new ResponseEntity<>( errorDetail.toErrorResponse( ), new HttpHeaders( ), valueOf( status ) );
    }

    private Map< String, Object > getErrorAttributes( WebRequest webRequest ) {
        return errorAttributes.getErrorAttributes( webRequest, defaults( ) );
    }
}
