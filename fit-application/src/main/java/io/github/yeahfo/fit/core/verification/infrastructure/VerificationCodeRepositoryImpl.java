package io.github.yeahfo.fit.core.verification.infrastructure;

import io.github.yeahfo.fit.core.verification.domain.VerificationCode;
import io.github.yeahfo.fit.core.verification.domain.VerificationCodeRepository;
import io.github.yeahfo.fit.core.verification.domain.VerificationCodeType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

import static io.github.yeahfo.fit.core.common.utils.CommonUtils.requireNonBlank;
import static java.time.Instant.now;
import static java.time.ZoneId.systemDefault;
import static java.time.temporal.ChronoUnit.MINUTES;
import static java.util.Objects.requireNonNull;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Repository
@RequiredArgsConstructor
public class VerificationCodeRepositoryImpl implements VerificationCodeRepository {
    private final MongoTemplate mongoTemplate;
    private final VerificationCodeRepositoryImplementation implementation;

    @Override
    public VerificationCode save( VerificationCode verificationCode ) {
        return implementation.save( verificationCode );
    }

    @Override
    public Optional< VerificationCode > findById( String id ) {
        return implementation.findById( id );
    }

    @Override
    public void deleteById( String id ) {
        implementation.deleteById( id );
    }

    @Override
    public boolean existsWithinOneMinutes( String mobileOrEmail, VerificationCodeType type ) {
        requireNonBlank( mobileOrEmail, "Mobile or email must not be blank." );
        requireNonNull( type, "Type must not be null." );
        Query query = Query.query( where( "mobileEmail" ).is( mobileOrEmail )
                .and( "type" ).is( type.name( ) )
                .and( "createdAt" ).gte( now( ).minus( 1, MINUTES ) ) );
        return mongoTemplate.exists( query, VerificationCode.class );
    }

    @Override
    public long totalCodeCountOfTodayFor( String mobileOrEmail ) {
        requireNonBlank( mobileOrEmail, "Mobile or email must not be blank." );
        Instant beginOfToday = LocalDate.now( ).atStartOfDay( ).atZone( systemDefault( ) ).toInstant( );
        Query query = Query.query( where( "mobileEmail" ).is( mobileOrEmail ).and( "createdAt" ).gte( beginOfToday ) );
        return mongoTemplate.count( query, VerificationCode.class );
    }

    @Override
    public Optional< VerificationCode > findByMobileEmailAndCodeAndType( String mobileEmail, String code, VerificationCodeType type ) {
        return implementation.findByMobileEmailAndCodeAndType( mobileEmail, code, type );
    }
}
