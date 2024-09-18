package io.github.yeahfo.fit.core.common.domain;

import io.github.yeahfo.fit.core.common.utils.Identified;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import static io.github.yeahfo.fit.core.common.utils.FitConstants.MAX_URL_LENGTH;

@Builder
public record UploadedFile( @NotBlank String id,//id，用于前端loop时作为key
                            @Size( max = 200 )
                            String name,//文件名称
                            @NotBlank
                            @Size( max = 500 )
                            String type,//文件类型
                            @NotBlank
                            @Size( max = MAX_URL_LENGTH )
                            String fileUrl,//文件url
                            @Size( max = 50 )
                            String ossKey,//阿里云的文件key
                            @Min( 0 )
                            int size//文件大小
) implements Identified< String > {
    @Override
    public String identifier( ) {
        return id;
    }
}
