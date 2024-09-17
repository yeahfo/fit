package io.github.yeahfo.fit.common.mongo;

import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mapping.model.FieldNamingStrategy;
import org.springframework.data.mapping.model.Property;
import org.springframework.data.mapping.model.PropertyNameFieldNamingStrategy;
import org.springframework.data.mapping.model.SimpleTypeHolder;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoManagedTypes;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoPersistentProperty;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class MongoConfiguration {
    @Bean
    MongoTransactionManager mongoTransactionManager( MongoDatabaseFactory mongoDatabaseFactory ) {
        return new MongoTransactionManager( mongoDatabaseFactory );
    }

    @Bean
    @ConditionalOnMissingBean
    MongoMappingContext mongoMappingContext( MongoProperties properties, MongoCustomConversions conversions,
                                             MongoManagedTypes managedTypes ) {
        PropertyMapper map = PropertyMapper.get( ).alwaysApplyingWhenNonNull( );
        MongoMappingContext context = new MongoMappingContext( ) {
            private FieldNamingStrategy fieldNamingStrategy = null;

            @Override
            public void setFieldNamingStrategy( FieldNamingStrategy fieldNamingStrategy ) {
                if ( fieldNamingStrategy == null ) {
                    this.fieldNamingStrategy = PropertyNameFieldNamingStrategy.INSTANCE;
                    super.setFieldNamingStrategy( PropertyNameFieldNamingStrategy.INSTANCE );
                } else {
                    this.fieldNamingStrategy = fieldNamingStrategy;
                    super.setFieldNamingStrategy( fieldNamingStrategy );
                }
            }

            @NonNull
            @Override
            public MongoPersistentProperty createPersistentProperty( @NonNull Property property,
                                                                     @NonNull MongoPersistentEntity< ? > owner,
                                                                     @NonNull SimpleTypeHolder simpleTypeHolder ) {
                return new CachingMongoPersistentPropertyCustomizer( property, owner, simpleTypeHolder, fieldNamingStrategy );
            }
        };
        map.from( properties.isAutoIndexCreation( ) ).to( context::setAutoIndexCreation );
        context.setManagedTypes( managedTypes );
        Class< ? > strategyClass = properties.getFieldNamingStrategy( );
        if ( strategyClass != null ) {
            context.setFieldNamingStrategy( ( FieldNamingStrategy ) BeanUtils.instantiateClass( strategyClass ) );
        }
        context.setSimpleTypeHolder( conversions.getSimpleTypeHolder( ) );
        return context;
    }
}
