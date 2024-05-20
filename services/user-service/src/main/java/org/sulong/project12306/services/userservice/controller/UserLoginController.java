package org.sulong.project12306.services.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.sulong.project12306.framework.convention.result.Result;
import org.sulong.project12306.framework.web.Results;
import org.sulong.project12306.services.userservice.dto.req.UserLoginReqDTO;
import org.sulong.project12306.services.userservice.dto.resp.UserLoginRespDTO;
import org.sulong.project12306.services.userservice.service.UserLoginService;

@RestController
@RequiredArgsConstructor
public class UserLoginController {
    private final UserLoginService userLoginService;
    /**
     * 用户登录
     */
    @PostMapping("/api/short-link/user/v1/login")
    public Result<UserLoginRespDTO> login(@RequestBody UserLoginReqDTO requestParam){
        return Results.success(userLoginService.login(requestParam));
    }

    /**
     * 通过 Token 检查用户是否登录
     */
    @GetMapping("/api/short-link/user/v1/check-login")
    public Result<UserLoginRespDTO> checkLogin(@RequestParam("accessToken") String accessToken){
        return Results.success(userLoginService.checkLogin(accessToken));
    }

    /**
     * 用户退出登录
     */
    @GetMapping("/api/short-link/user/v1/logout")
    public Result<Void> logout(@RequestParam(required = false) String accessToken) {
        userLoginService.logout(accessToken);
        return Results.success();
    }
}
