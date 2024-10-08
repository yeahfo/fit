package io.github.yeahfo.fit.core.tenant.application;

import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import io.github.yeahfo.fit.core.member.domain.events.MemberDomainEvent;
import io.github.yeahfo.fit.core.tenant.domain.events.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TenantModuleEventsSubscriber {
    private final TenantCommandService tenantCommandService;

    public DomainEventHandlers domainEventHandlers( ) {
        return DomainEventHandlersBuilder
                .forAggregateType( TenantDomainEvent.aggregateType )
                .onEvent( TenantCreatedEvent.class, this::handleTenantCreatedEvent )
                .onEvent( TenantBaseSettingUpdatedEvent.class, this::handleTenantUpdatedEvent )
                .onEvent( TenantInvoiceTitleUpdatedEvent.class, this::handleTenantUpdatedEvent )
                .andForAggregateType( MemberDomainEvent.aggregateType )
                .onEvent( MemberDomainEvent.class, this::handleMemberDomainEvent )
                .build( );
    }

    private void handleTenantUpdatedEvent( DomainEventEnvelope< ? extends TenantUpdatedEvent > envelope ) {
        System.err.println( "[TenantModule]....EventsSubscriber.handleTenantUpdatedEvent: " + envelope.getAggregateId( ) );
        System.err.println( "[TenantModule]....EventsSubscriber.handleTenantUpdatedEvent: " + envelope.getEvent( ) );
    }

    private void handleMemberDomainEvent( DomainEventEnvelope< MemberDomainEvent > envelope ) {
        tenantCommandService.countTenantMembers( envelope.getEvent( ).releaser( ).tenantId( ) );
    }

    private void handleTenantCreatedEvent( DomainEventEnvelope< TenantCreatedEvent > envelope ) {
        tenantCommandService.countTenantMembers( envelope.getAggregateId( ) );
    }
}
