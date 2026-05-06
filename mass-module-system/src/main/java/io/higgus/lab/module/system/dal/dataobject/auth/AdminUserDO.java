package io.higgus.lab.module.system.dal.dataobject.auth;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@TableName("system_users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserDO {

    @TableId
    private Long id;

    private String username;

    private String password;

    private String nickname;

    private Integer deleted;


}
