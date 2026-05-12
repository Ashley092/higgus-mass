package io.higgus.lab.module.system.service.permission;

public interface PermissionService {


    boolean hasAnyPermissions(Long userId, String[] permissions);
}
