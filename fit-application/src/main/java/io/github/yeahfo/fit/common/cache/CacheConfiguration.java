package io.github.yeahfo.fit.common.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.yeahfo.fit.common.jackson.Jackson;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY;
import static com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping.NON_FINAL;
import static org.springframework.data.redis.cache.RedisCacheConfiguration.defaultCacheConfig;
import static org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair.fromSerializer;

@EnableCaching
@Configuration
public class CacheConfiguration {

    @Bean
    @ConditionalOnProperty( value = "spring.cache.type", havingValue = "redis" )
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer( CacheProperties cacheProperties ) {
        ObjectMapper defaultObjectMapper = Jackson.MAPPER;
        defaultObjectMapper.activateDefaultTyping( defaultObjectMapper.getPolymorphicTypeValidator( ), NON_FINAL, PROPERTY );
        return builder -> builder.cacheDefaults( defaultCacheConfig( )
                .prefixCacheNameWith( cacheProperties.getRedis( ).getKeyPrefix( ) )
                .serializeValuesWith( fromSerializer( new GenericJackson2JsonRedisSerializer( defaultObjectMapper ) ) )
                .entryTtl( cacheProperties.getRedis( ).getTimeToLive( ) ) );
    }

}
