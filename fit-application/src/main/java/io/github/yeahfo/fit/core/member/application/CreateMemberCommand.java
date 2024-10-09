package io.github.yeahfo.fit.core.member.application;

import io.github.yeahfo.fit.core.common.validation.collection.NoBlankString;
import io.github.yeahfo.fit.core.common.validation.collection.NoDuplicatedString;
import io.github.yeahfo.fit.core.common.validation.id.custom.CustomId;
import io.github.yeahfo.fit.core.common.validation.id.department.DepartmentId;
import io.github.yeahfo.fit.core.common.validation.mobile.Mobile;
import io.github.yeahfo.fit.core.common.validation.password.Password;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

import static io.github.yeahfo.fit.core.common.utils.FitConstants.MAX_GENERIC_NAME_LENGTH;

@Builder
public record CreateMemberCommand( @NotBlank
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
                                   String email,
                                   @CustomId
                                   String customId,
                                   @NotBlank
                                   @Password
                                   String password ) {
}
