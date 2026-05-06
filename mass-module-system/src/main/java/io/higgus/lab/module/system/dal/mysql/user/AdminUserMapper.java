package io.higgus.lab.module.system.dal.mysql.user;


import io.higgus.lab.framework.mybatis.core.mapper.BaseMapperX;
import io.higgus.lab.module.system.dal.dataobject.auth.AdminUserDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminUserMapper extends BaseMapperX<AdminUserDO> {


    public default AdminUserDO getUserByUsername(String username) {
        return selectOne(AdminUserDO::getUsername, username);
    }
}
