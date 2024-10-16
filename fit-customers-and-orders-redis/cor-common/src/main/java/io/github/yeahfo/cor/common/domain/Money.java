package io.github.yeahfo.cor.common.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.math.BigDecimal;

public record Money( BigDecimal amount ) {
    public static final Money ZERO = new Money( 0 );

    public Money( int amount ) {
        this( new BigDecimal( amount ) );
    }

    public Money( String amount ) {
        this( new BigDecimal( amount ) );
    }

    @Override
    public String toString( ) {
        return ToStringBuilder.reflectionToString( this );
    }

    @Override
    public boolean equals( Object obj ) {
        return obj instanceof Money && EqualsBuilder.reflectionEquals( this, obj );
    }

    @Override
    public int hashCode( ) {
        return HashCodeBuilder.reflectionHashCode( this );
    }

    public boolean isGreaterThanOrEqual( Money other ) {
        return amount.compareTo( other.amount ) >= 0;
    }

    public Money add( Money other ) {
        return new Money( amount.add( other.amount ) );
    }

    public Money subtract( Money other ) {
        return new Money( amount.subtract( other.amount ) );
    }
}
