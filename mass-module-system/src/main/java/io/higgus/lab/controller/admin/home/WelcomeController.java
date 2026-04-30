package io.higgus.lab.controller.admin.home;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/home/welcome")
@Tag(name = "首页管理", description = "系统欢迎页相关接口")
public class WelcomeController {

    @GetMapping("/hello")
    @Operation(summary = "获取欢迎语", description = "返回系统欢迎信息，用于测试接口是否正常运行")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "成功返回欢迎语"),
            @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    public String sayHello() {
        return "欢迎使用 Mass 系统！";
    }

    @GetMapping("/user/{id}")
    @Operation(summary = "根据ID查询用户", description = "传入用户ID，返回用户基本信息")
    public String getUserById(
            @Parameter(description = "用户ID", required = true, example = "1001")
            @PathVariable("id") Long id) {
        return "用户ID: " + id;
    }

    @PostMapping("/message")
    @Operation(summary = "发送消息", description = "向系统发送一条消息")
    public String sendMessage(
            @Parameter(description = "消息内容", required = true, example = "Hello Knife4j")
            @RequestParam String message) {
        return "消息已发送: " + message;
    }
}