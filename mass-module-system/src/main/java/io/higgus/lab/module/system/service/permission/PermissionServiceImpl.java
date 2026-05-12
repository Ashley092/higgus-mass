package io.higgus.lab.module.system.service.permission;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PermissionServiceImpl implements PermissionService {


    @Override
    public boolean hasAnyPermissions(Long userId, String... permissions) {
        return false;
    }
}
