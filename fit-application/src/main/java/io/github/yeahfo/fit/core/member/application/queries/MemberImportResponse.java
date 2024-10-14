package io.github.yeahfo.fit.core.member.application.queries;

import io.github.yeahfo.fit.core.member.application.imports.MemberImportRecord;
import lombok.Builder;

import java.util.List;

@Builder
public record MemberImportResponse( int readCount,
                                    int importedCount,
                                    List< MemberImportRecord > errorRecords ) {
}
