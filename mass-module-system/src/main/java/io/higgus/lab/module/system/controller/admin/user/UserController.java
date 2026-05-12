package io.higgus.lab.module.system.controller.admin.user;


import io.higgus.lab.mass.framework.common.pojo.CommonResult;
import io.higgus.lab.module.system.controller.admin.user.vo.UserSaveReqVO;
import io.higgus.lab.module.system.service.user.AdminUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static io.higgus.lab.mass.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 用户管理")
@RestController
@RequestMapping("/admin/user")
@Validated
public class UserController {

    @Resource
    AdminUserService userService;

    @Operation(summary = "新增用户")
    @PostMapping("/create")
    public CommonResult<Long> addUser(@RequestBody @Valid UserSaveReqVO reqVO){

        Long id = userService.createUser(reqVO);
        return success(id);
    }

    @Operation(summary = "更新用户")
    @PutMapping("/update")
    public CommonResult<Boolean> updateUser(@RequestBody @Valid UserSaveReqVO reqVO) {
        userService.updateUser(reqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除用户")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<Boolean> deleteUser(@RequestParam Long id) {

        userService.deleteUserById(id);
        return success(true);
    }

//    @Operation(summary = "获取用户信息")
//    @GetMapping("/get")
//    public CommonResult<> getUser() {
//
//        userService.getUserById();
//        return success();
//    }
}
