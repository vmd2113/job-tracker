package com.duongw.commonservice.model.projection;

public interface DepartmentProjection {

    Long getId();

    String getName();

    String getCode();

    Long getParentId();

    Long getStatus();

    Integer getDepth();

    Integer getIsLeaf();

    String getParentName();

    String getParentCode();


}
