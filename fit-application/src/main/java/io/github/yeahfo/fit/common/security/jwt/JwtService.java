package io.github.yeahfo.fit.common.security.jwt;

import io.github.yeahfo.fit.common.security.AuthorizationServerPropertiesTokenCustomizer;
import io.github.yeahfo.fit.common.security.CustomizedAuthenticationToken;
import io.github.yeahfo.fit.core.member.domain.Member;
import io.github.yeahfo.fit.core.member.domain.MemberRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import static io.github.yeahfo.fit.core.common.domain.user.User.humanUser;
import static java.time.temporal.ChronoUnit.MILLIS;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final MemberRepository memberRepository;
    private final AuthorizationServerPropertiesTokenCustomizer properties;

    public String generateJwt( String memberId ) {
        Date now = new Date( );
        Date expirationDate = new Date( now.getTime( ) + properties.expiration( ).get( MILLIS ) );
        return generateJwt( memberId, expirationDate );
    }

    public String generateJwt( String memberId, Date expirationDate ) {
        Claims claims = Jwts.claims( ).subject( memberId ).build( );

        return Jwts.builder( )
                .claims( claims )
                .issuer( properties.issuer( ) )
                .issuedAt( new Date( ) )
                .expiration( expirationDate )
                .signWith( Keys.hmacShaKeyFor( properties.secret( ).getBytes( StandardCharsets.UTF_8 ) ), Jwts.SIG.HS256 )
                .compact( );
    }

    public CustomizedAuthenticationToken tokenFrom( String jwt ) {
        Claims claims = Jwts.parser( ).verifyWith( Keys.hmacShaKeyFor( properties.secret( ).getBytes( StandardCharsets.UTF_8 ) ) ).build( )
                .parseSignedClaims( jwt ).getPayload( );
        String memberId = claims.getSubject( );
        Member member = memberRepository.find( memberId );
        member.checkActive( );
        long expiration = claims.getExpiration( ).getTime( );
        return new CustomizedAuthenticationToken(
                humanUser( memberId, member.name( ), member.tenantId( ), member.role( ) ),
                expiration
        );
    }
}
