package io.github.yeahfo.fit.core.plan.domain;

import lombok.Getter;

@Getter
public enum PlanType {
    FREE( "免费版", 0, 1 ),
    BASIC( "基础版", 680, 2 ),
    ADVANCED( "高级版", 1380, 3 ),
    PROFESSIONAL( "专业版", 6980, 4 ),
    FLAGSHIP( "旗舰版", 12800, 5 );

    private final String name;
    private final int price;
    private final int level;

    PlanType( String name, int price, int level ) {
        this.name = name;
        this.price = price;
        this.level = level;
    }

    public boolean covers( PlanType other ) {
        return this.getPrice( ) >= other.getPrice( );
    }
}
