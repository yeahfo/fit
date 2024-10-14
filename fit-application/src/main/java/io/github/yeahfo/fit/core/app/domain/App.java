package io.github.yeahfo.fit.core.app.domain;

import io.github.yeahfo.fit.core.common.domain.AggregateRoot;
import io.github.yeahfo.fit.core.common.domain.UploadedFile;

import java.util.List;

public class App extends AggregateRoot {
    private String name;//应用的名称
    private UploadedFile icon;//图标，保存时从setting同步
    private boolean active;//是否启用
    private boolean locked;//是否锁定，锁定之后无法编辑，但是可以正常使用
    private List< String > managers;//应用管理员
}
