package io.higgus.lab.controller.admin.user;

import io.higgus.lab.controller.admin.user.vo.UserProfileRespVO;
import io.higgus.lab.mass.framework.common.pojo.CommonResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag( name = "管理后台 - 用户")
@RestController
@RequestMapping("/admin/user")
@Validated
public class UseProfilerController {



    @GetMapping("/get-simple-profile")
    public void getSimpleUserProfile(@RequestParam("id") Long id) {
        return null;
    }
}
