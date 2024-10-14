package io.github.yeahfo.fit.core.member.application.imports;

import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import io.github.yeahfo.fit.core.common.domain.user.User;
import io.github.yeahfo.fit.core.member.domain.Member;
import io.github.yeahfo.fit.core.member.domain.MemberDomainService;
import io.github.yeahfo.fit.core.member.domain.MemberRepository;
import io.github.yeahfo.fit.core.member.domain.events.MemberDomainEvent;
import io.github.yeahfo.fit.core.member.domain.events.MemberDomainEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MemberImportSaver {
    private final MemberRepository memberRepository;
    private final MemberDomainService memberDomainService;
    private final MemberDomainEventPublisher domainEventPublisher;

    @Transactional
    public void save( MemberImportRecord record, User user ) {
        ResultWithDomainEvents< Member, MemberDomainEvent > resultWithDomainEvents = memberDomainService.create( record.getName( ),
                List.of( ),
                record.getMobile( ),
                record.getEmail( ),
                record.getPassword( ),
                record.getCustomId( ),
                user );
        Member member = memberRepository.save( resultWithDomainEvents.result );
        domainEventPublisher.publish( member, resultWithDomainEvents.events );
    }
}
