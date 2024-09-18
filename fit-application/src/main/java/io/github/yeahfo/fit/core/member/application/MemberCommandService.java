package io.github.yeahfo.fit.core.member.application;

import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import io.github.yeahfo.fit.core.common.domain.user.User;
import io.github.yeahfo.fit.core.member.domain.Member;
import io.github.yeahfo.fit.core.member.domain.MemberDomainService;
import io.github.yeahfo.fit.core.member.domain.MemberRepository;
import io.github.yeahfo.fit.core.member.domain.events.MemberDomainEvent;
import io.github.yeahfo.fit.core.member.domain.events.MemberDomainEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberCommandService {
    private final MemberRepository memberRepository;
    private final MemberDomainService memberDomainService;
    private final MemberDomainEventPublisher domainEventPublisher;

    @Transactional
    public void registerMember( String mobile, String email, String password, User user ) {
        ResultWithDomainEvents< Member, MemberDomainEvent > resultWithDomainEvents = memberDomainService.register( mobile, email, password, user );
        Member member = memberRepository.save( resultWithDomainEvents.result );
        domainEventPublisher.publish( member, resultWithDomainEvents.events );
    }
}
