package io.higgus.lab.mass.framework.common.biz.system.permission;

public interface PermissionCommonApi {

    boolean hasAnyPermissions(Long userId, String... permissions);
}
