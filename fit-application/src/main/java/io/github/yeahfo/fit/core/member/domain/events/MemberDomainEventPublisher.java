package io.github.yeahfo.fit.core.member.domain.events;

import io.eventuate.tram.events.aggregates.AbstractAggregateDomainEventPublisher;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.github.yeahfo.fit.core.member.domain.Member;
import org.springframework.stereotype.Component;

@Component
public class MemberDomainEventPublisher extends AbstractAggregateDomainEventPublisher< Member, MemberDomainEvent > {
    protected MemberDomainEventPublisher( DomainEventPublisher eventPublisher ) {
        super( eventPublisher, Member.class, Member::identifier );
    }
}
