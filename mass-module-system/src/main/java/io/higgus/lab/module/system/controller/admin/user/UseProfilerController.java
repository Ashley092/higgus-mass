package io.higgus.lab.module.system.controller.admin.user;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag( name = "管理后台 - 用户")
@RestController
@RequestMapping("/admin/user")
@Validated
public class UseProfilerController {



//    @GetMapping("/get-simple-profile")
//    public void getSimpleUserProfile(@RequestParam("id") Long id) {
//        return null;
//    }
}
