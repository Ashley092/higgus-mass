package io.higgus.lab.module.system.service.user;

import io.higgus.lab.module.system.controller.admin.user.vo.UserSaveReqVO;
import io.higgus.lab.module.system.dal.dataobject.auth.AdminUserDO;

public interface AdminUserService {

    public AdminUserDO getUserByUsername(String username);

    public Long createUser(UserSaveReqVO userVO);

    public void deleteById(Long id);

    public void updateUser(UserSaveReqVO reqVO);

}
