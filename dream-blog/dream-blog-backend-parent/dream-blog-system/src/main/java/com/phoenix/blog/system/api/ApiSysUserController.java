package com.phoenix.blog.system.api;

import com.phoenix.blog.common.base.Result;
import com.phoenix.blog.system.request.RegisterRequest;
import com.phoenix.blog.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = "用户管理接口", description = "用户管理接口，不需要身份认证就可以调用下面接口")
@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class ApiSysUserController {

    private final ISysUserService sysUserService;

    @ApiImplicitParam(name = "username", value = "用户名", required = true)
    @ApiOperation("校验用户名是否存在接口")
    @GetMapping("/username/{username}") //  /api/user/username/{username}
    public Result checkUsername(@PathVariable("username") String username) {
        return sysUserService.checkUsername(username);
    }

    @ApiOperation("注册用户接口")
    @PostMapping("/register") // /api/user/register
    @ApiImplicitParam(name = "registerRequest", value = "用户注册信息", dataType = "RegisterRequest", required = true)
    public Result register(@RequestBody RegisterRequest registerRequest) {
        return sysUserService.register(registerRequest);
    }

}
