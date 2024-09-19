package io.github.yeahfo.fit.common.jackson;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import io.github.yeahfo.fit.core.common.utils.CustomizedObjectMapper;

import java.io.InputStream;
import java.io.Writer;


public interface Jackson {
    CustomizedObjectMapper MAPPER = new CustomizedObjectMapper( );


    static String writeValueAsString( Object value ) {
        return MAPPER.writeValueAsString( value );
    }

    static String writeValueAsPrettyString( Object value ) {
        return MAPPER.writeValueAsPrettyString( value );
    }

    static byte[] writeValueAsBytes( Object value ) {
        return MAPPER.writeValueAsBytes( value );
    }

    static void writeValue( Writer w, Object value ) {
        MAPPER.writeValue( w, value );
    }


    static < T > T readValue( String content, Class< T > valueType ) {
        return MAPPER.readValue( content, valueType );
    }

    static < T > T readValue( String content, TypeReference< T > valueTypeRef ) {
        return MAPPER.readValue( content, valueTypeRef );
    }


    static < T > T readValue( InputStream src, Class< T > valueType ) {
        return MAPPER.readValue( src, valueType );
    }

    static JsonNode readTree( String content ) {
        return MAPPER.readTree( content );
    }
}
