package io.higgus.lab.module.system.service.user;


import cn.hutool.core.bean.BeanUtil;
import io.higgus.lab.mass.framework.common.util.object.BeanUtils;
import io.higgus.lab.module.system.controller.admin.user.vo.UserSaveReqVO;
import io.higgus.lab.module.system.dal.dataobject.auth.AdminUserDO;
import io.higgus.lab.module.system.dal.mysql.user.AdminUserMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminUserServiceImpl implements AdminUserService{


    @Resource
    private AdminUserMapper userMapper;

    @Override
    public AdminUserDO getUserByUsername(String username) {
        return userMapper.getUserByUsername(username);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long createUser(UserSaveReqVO reqVO) {

        AdminUserDO user = BeanUtil.toBean(reqVO, AdminUserDO.class);

        user.setStatus(1); // 默认开启
//        user.setPassword(encodePassword(createReqVO.getPassword())); // 加密密码

        userMapper.insert(user);

        return user.getId();

    }

    @Override
    public void deleteUserById(Long id) {
        userMapper.deleteById(id);
    }

    @Override
    public void updateUser(UserSaveReqVO reqVO) {
        AdminUserDO updateObj = BeanUtils.toBean(reqVO, AdminUserDO.class);
        userMapper.updateById(updateObj);

    }


}
