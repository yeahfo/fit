package io.github.yeahfo.fit.core.group.domain;

import io.github.yeahfo.fit.core.common.domain.AggregateRoot;

import java.util.List;

public class Group extends AggregateRoot {
    private String name;//名称
    private String appId;//所在的app
    private List< String > managers;//管理员
    private List< String > members;//普通成员
    private boolean archived;//是否归档，归档后group下的资源将不再显示在运营端，但是依然可以完成其下qr的扫码
    private String customId;//自定义编号，在App下唯一
    private boolean active;//是否启用
    private String departmentId;//由哪个部门同步而来
}
