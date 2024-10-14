package io.github.yeahfo.fit.core.member.application.imports;

import io.github.yeahfo.fit.core.common.domain.user.User;
import io.github.yeahfo.fit.core.common.exception.FitException;
import io.github.yeahfo.fit.core.member.application.queries.MemberImportResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

import static com.google.common.collect.ImmutableList.toImmutableList;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberImporter {
    private final MemberImportSaver memberImportSaver;
    private final MemberImportParser memberImportParser;

    public MemberImportResponse importMembers( InputStream inputStream, long limit, User user ) {
        List< MemberImportRecord > records = memberImportParser.parse( inputStream, user.tenantId( ), limit );

        records.forEach( record -> {
            try {
                if ( !record.hasError( ) ) {
                    memberImportSaver.save( record, user );
                }
            } catch ( FitException exception ) {
                record.addError( exception.getUserMessage( ) );
            }
        } );

        List< MemberImportRecord > errorRecords = records.stream( ).filter( MemberImportRecord::hasError ).collect( toImmutableList( ) );

        return MemberImportResponse.builder( )
                .readCount( records.size( ) )
                .importedCount( records.size( ) - errorRecords.size( ) )
                .errorRecords( errorRecords )
                .build( );
    }
}
