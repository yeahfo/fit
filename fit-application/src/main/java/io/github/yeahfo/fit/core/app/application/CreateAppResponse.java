package io.github.yeahfo.fit.core.app.application;

import lombok.Builder;

@Builder
public record CreateAppResponse( String appId,
                                 String defaultGroupId,
                                 String homePageId ) {
}
