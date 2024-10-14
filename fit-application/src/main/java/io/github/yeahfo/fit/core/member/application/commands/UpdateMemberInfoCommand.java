package io.github.yeahfo.fit.core.member.application.commands;

import io.github.yeahfo.fit.core.common.validation.collection.NoBlankString;
import io.github.yeahfo.fit.core.common.validation.collection.NoDuplicatedString;
import io.github.yeahfo.fit.core.common.validation.email.Email;
import io.github.yeahfo.fit.core.common.validation.id.department.DepartmentId;
import io.github.yeahfo.fit.core.common.validation.mobile.Mobile;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

import static io.github.yeahfo.fit.core.common.utils.FitConstants.MAX_GENERIC_NAME_LENGTH;

@Builder
public record UpdateMemberInfoCommand( @NotBlank
                                       @Size( max = MAX_GENERIC_NAME_LENGTH )
                                       String name,

                                       @Valid
                                       @NotNull
                                       @NoBlankString
                                       @NoDuplicatedString
                                       @Size( max = 1000 )
                                       List< @DepartmentId String > departmentIds,

                                       @Mobile
                                       String mobile,

                                       @Email
                                       String email ) {
}
