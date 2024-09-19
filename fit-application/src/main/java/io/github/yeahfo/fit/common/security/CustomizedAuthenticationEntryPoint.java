package io.github.yeahfo.fit.common.security;

import io.github.yeahfo.fit.common.jackson.Jackson;
import io.github.yeahfo.fit.common.tracing.TracingService;
import io.github.yeahfo.fit.core.common.exception.Error;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

import static io.github.yeahfo.fit.core.common.exception.ErrorCode.AUTHENTICATION_FAILED;
import static org.apache.commons.lang.CharEncoding.UTF_8;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Component
@RequiredArgsConstructor
public class CustomizedAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final TracingService tracingService;

    @Override
    public void commence( HttpServletRequest request, HttpServletResponse response, AuthenticationException authException ) throws IOException {
        SecurityContextHolder.clearContext( );
        response.setStatus( 401 );
        response.setContentType( APPLICATION_JSON_VALUE );
        response.setCharacterEncoding( UTF_8 );
        String traceId = tracingService.currentTraceId( );
        Error error = new Error( AUTHENTICATION_FAILED, 401, "Authentication failed.", request.getRequestURI( ), traceId, null );

        PrintWriter writer = response.getWriter( );
        writer.print( Jackson.writeValueAsString( error.toErrorResponse( ) ) );
        writer.flush( );
    }
}
