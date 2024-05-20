package org.sulong.project12306.services.userservice.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.sulong.project12306.framework.convention.result.Result;
import org.sulong.project12306.framework.web.Results;
import org.sulong.project12306.services.userservice.dto.req.UserDeletionReqDTO;
import org.sulong.project12306.services.userservice.dto.req.UserRegisterReqDTO;
import org.sulong.project12306.services.userservice.dto.req.UserUpdateReqDTO;
import org.sulong.project12306.services.userservice.dto.resp.UserQueryActualRespDTO;
import org.sulong.project12306.services.userservice.dto.resp.UserQueryRespDTO;
import org.sulong.project12306.services.userservice.dto.resp.UserRegisterRespDTO;
import org.sulong.project12306.services.userservice.service.UserLoginService;
import org.sulong.project12306.services.userservice.service.UserService;

@RequiredArgsConstructor
@RestController
public class UserInfoController {
    private final UserService userService;
    private final UserLoginService userLoginService;

    @GetMapping("/api/short-link/user/v1/query")
    public Result<UserQueryRespDTO> queryUserByUsername(@RequestParam("username") @NotEmpty String username){
        return Results.success(userService.queryUserByUsername(username));
    }

    /**
     * 根据用户名查询用户无脱敏信息
     */
    @GetMapping("/api/short-link/user/v1/actual/query")
    public Result<UserQueryActualRespDTO> queryActualUserByUsername(@RequestParam("username") @NotEmpty String username) {
        return Results.success(userService.queryActualUserByUsername(username));
    }

    /**
     * 检查用户名是否已存在
     */
    @GetMapping("/api/short-link/user/v1/has-username")
    public Result<Boolean> hasUsername(@RequestParam("username") @NotEmpty String username) {
        return Results.success(userLoginService.hasUsername(username));
    }

    /**
     * 注册用户
     */
    @PostMapping("/api/short-link/user/v1")
    public Result<UserRegisterRespDTO> register(@RequestBody @Valid UserRegisterReqDTO requestParam) {
        return Results.success(userLoginService.register(requestParam));
    }

    /**
     * 修改用户
     */
    @PutMapping("/api/short-link/user/v1")
    public Result<Void> update(@RequestBody @Valid UserUpdateReqDTO requestParam) {
        userService.update(requestParam);
        return Results.success();
    }

    /**
     * 注销用户
     */
    @PostMapping("/api/short-link/user/v1/deletion")
    public Result<Void> deletion(@RequestBody @Valid UserDeletionReqDTO requestParam) {
        userLoginService.deletion(requestParam);
        return Results.success();
    }
}
