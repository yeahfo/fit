package io.github.yeahfo.fit.core.member.infrastructure;

import io.github.yeahfo.fit.core.member.domain.Member;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MemberRepositoryImplementation extends MongoRepository< Member, String > {
    boolean existsByMobileOrEmail( String mobile, String email );

    boolean existsByMobile( String mobile );

    boolean existsByEmail( String email );

    Optional< Member > findByMobileOrEmail( String mobile, String email );
}
