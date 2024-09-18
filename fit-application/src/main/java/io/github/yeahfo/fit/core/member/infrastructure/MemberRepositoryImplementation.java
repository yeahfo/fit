package io.github.yeahfo.fit.core.member.infrastructure;

import io.github.yeahfo.fit.core.member.domain.Member;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MemberRepositoryImplementation extends MongoRepository< Member, String > {
}
