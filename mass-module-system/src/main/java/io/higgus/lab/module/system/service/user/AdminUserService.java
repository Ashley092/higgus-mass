package io.higgus.lab.module.system.service.user;

import io.higgus.lab.module.system.dal.dataobject.auth.AdminUserDO;

public interface AdminUserService {

    public AdminUserDO getUserByUsername(String username);

}
