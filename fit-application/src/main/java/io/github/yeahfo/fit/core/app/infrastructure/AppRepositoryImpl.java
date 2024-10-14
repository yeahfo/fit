package io.github.yeahfo.fit.core.app.infrastructure;

import io.github.yeahfo.fit.core.app.domain.App;
import io.github.yeahfo.fit.core.app.domain.AppRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository
@RequiredArgsConstructor
public class AppRepositoryImpl implements AppRepository {
    private final MongoTemplate mongoTemplate;
    private final AppRepositoryImplementation implementation;

    @Override
    public App save( App app ) {
        return implementation.save( app );
    }

    @Override
    public Optional< App > findById( String id ) {
        return implementation.findById( id );
    }

    @Override
    public void deleteById( String id ) {
        implementation.deleteById( id );
    }

    @Override
    public boolean existsByTenantIdAndManagers( String tenantId, String managers ) {
        Query query = query( where( "tenantId" ).is( tenantId ).and( "managers" ).is( managers ) );
        return mongoTemplate.exists( query, App.class );
    }
}
