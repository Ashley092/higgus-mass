package io.higgus.lab.module.system.service.user;


import io.higgus.lab.module.system.dal.dataobject.auth.AdminUserDO;
import io.higgus.lab.module.system.dal.mysql.user.AdminUserMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class AdminUserServiceImpl implements AdminUserService{


    @Resource
    private AdminUserMapper userMapper;

    @Override
    public AdminUserDO getUserByUsername(String username) {
        return userMapper.getUserByUsername(username);
    }
}
