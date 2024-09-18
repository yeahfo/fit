package io.github.yeahfo.fit.core.member.domain;

import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import io.github.yeahfo.fit.common.password.FitPasswordEncoder;
import io.github.yeahfo.fit.core.common.domain.user.User;
import io.github.yeahfo.fit.core.member.domain.events.MemberDomainEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDomainService {
    private final FitPasswordEncoder passwordEncoder;

    public ResultWithDomainEvents< Member, MemberDomainEvent > register( String mobile, String email, String password,
                                                                         User user ) {
        return Member.register( mobile, email, passwordEncoder.encode( password ), user );
    }
}
