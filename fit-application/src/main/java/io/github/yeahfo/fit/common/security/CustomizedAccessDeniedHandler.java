package io.github.yeahfo.fit.common.security;

import io.github.yeahfo.fit.common.jackson.Jackson;
import io.github.yeahfo.fit.common.tracing.TracingService;
import io.github.yeahfo.fit.core.common.exception.Error;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

import static io.github.yeahfo.fit.core.common.exception.ErrorCode.ACCESS_DENIED;
import static org.apache.commons.lang.CharEncoding.UTF_8;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Component
@RequiredArgsConstructor
public class CustomizedAccessDeniedHandler implements AccessDeniedHandler {
    private final TracingService tracingService;

    @Override
    public void handle( HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException ) throws IOException {
        SecurityContextHolder.clearContext( );
        response.setStatus( 403 );
        response.setContentType( APPLICATION_JSON_VALUE );
        response.setCharacterEncoding( UTF_8 );

        String traceId = tracingService.currentTraceId( );
        Error error = new Error( ACCESS_DENIED, 403, "Access denied.", request.getRequestURI( ), traceId, null );
        PrintWriter writer = response.getWriter( );
        writer.print( Jackson.writeValueAsString( error.toErrorResponse( ) ) );
        writer.flush( );
    }
}
