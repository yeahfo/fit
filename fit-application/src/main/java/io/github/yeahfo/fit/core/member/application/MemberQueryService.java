package io.github.yeahfo.fit.core.member.application;

import io.github.yeahfo.fit.core.common.domain.user.User;
import io.github.yeahfo.fit.core.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

import static org.apache.logging.log4j.util.Strings.isBlank;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.data.domain.Sort.by;

@Service
@RequiredArgsConstructor
public class MemberQueryService {
    private final static Set< String > ALLOWED_SORT_FIELDS = Set.of( "name", "active" );
    private final MemberRepository memberRepository;

    public Page< ListMember > listMyManagedMembers( ListMyManagedMembersCommand command, User user ) {
        return memberRepository.listTenantMembers( command.page( ), command.size( ), sort( command.sortedBy( ), command.ascSort( ) ),
                user.tenantId( ), command.departmentId( ), command.search( ) );
    }

    private Sort sort( String sortedBy, boolean ascSort ) {

        if ( isBlank( sortedBy ) || !ALLOWED_SORT_FIELDS.contains( sortedBy ) ) {
            return by( DESC, "createdAt" );
        }

        Sort.Direction direction = ascSort ? ASC : DESC;
        if ( Objects.equals( sortedBy, "createdAt" ) ) {
            return by( direction, "createdAt" );
        }

        return by( direction, sortedBy ).and( by( DESC, "createdAt" ) );
    }
}
