package io.higgus.lab.module.system.api.permission;


import io.higgus.lab.module.system.service.permission.PermissionService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class PermissionApiImpl implements PermissionApi {

    @Resource
    PermissionService permissionService;

    @Override
    public boolean hasAnyPermissions(Long userId, String... permissions) {
        return permissionService.hasAnyPermissions(userId, permissions);
    }


}
