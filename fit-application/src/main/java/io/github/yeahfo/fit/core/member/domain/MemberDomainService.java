package io.github.yeahfo.fit.core.member.domain;

import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import io.github.yeahfo.fit.common.password.PasswordEncoder;
import io.github.yeahfo.fit.core.common.domain.user.User;
import io.github.yeahfo.fit.core.member.domain.events.MemberDomainEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Service
@RequiredArgsConstructor
public class MemberDomainService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    public ResultWithDomainEvents< Member, MemberDomainEvent > register( String mobile, String email, String password,
                                                                         User user ) {
        return Member.register( mobile, email, passwordEncoder.encode( password ), user );
    }

    @Transactional( propagation = REQUIRES_NEW )//使用REQUIRES_NEW保证即便其他地方有异常，这里也能正常写库
    public void recordMemberFailedLogin( Member member ) {
        member.recordFailedLogin( );
        memberRepository.save( member );
    }
}
