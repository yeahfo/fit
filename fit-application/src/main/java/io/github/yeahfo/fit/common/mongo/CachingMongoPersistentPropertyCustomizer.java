package io.github.yeahfo.fit.common.mongo;

import org.springframework.data.mapping.model.FieldNamingStrategy;
import org.springframework.data.mapping.model.Property;
import org.springframework.data.mapping.model.SimpleTypeHolder;
import org.springframework.data.mongodb.core.mapping.CachingMongoPersistentProperty;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;

import java.util.Set;

public class CachingMongoPersistentPropertyCustomizer extends CachingMongoPersistentProperty {
    public static Set< String > CUSTOMIZED_VERSION_PROPERTIES = Set.of( "version", "_version", "version_" );

    /**
     * Creates a new {@link CachingMongoPersistentProperty}.
     *
     * @param property            must not be {@literal null}.
     * @param owner               must not be {@literal null}.
     * @param simpleTypeHolder    must not be {@literal null}.
     * @param fieldNamingStrategy can be {@literal null}.
     */
    public CachingMongoPersistentPropertyCustomizer( Property property,
                                                     MongoPersistentEntity< ? > owner,
                                                     SimpleTypeHolder simpleTypeHolder,
                                                     FieldNamingStrategy fieldNamingStrategy ) {
        super( property, owner, simpleTypeHolder, fieldNamingStrategy );
    }

    @Override
    public boolean isVersionProperty( ) {
        Class< ? > fieldType = super.getFieldType( );
        return CUSTOMIZED_VERSION_PROPERTIES.contains( super.getFieldName( ) )
                && ( fieldType == long.class || fieldType == Long.class );
    }
}
