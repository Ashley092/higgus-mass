package io.higgus.lab.module.system.dal.dataobject.oauth2;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@TableName(value = "system_oauth2_access_token2", autoResultMap = true)
@Data
public class OAuth2AccessTokenDO {


    /**
     * 编号，数据库递增
     */
    @TableId
    private Long id;
    /**
     * 访问令牌
     */
    private String accessToken;
    /**
     * 用户编号
     */
    private Long userId;
    /**
     * 用户编号
     */
    private Integer userType;
    /**
     * 过期时间
     */
    private LocalDateTime expiresTime;



    private Integer deleted;

}
