package com.tt.ttpictureserver.model.user.controller;
import com.tt.ttpictureserver.common.BaseResponse;
import com.tt.ttpictureserver.model.user.domain.dto.UserLoginRequest;
import com.tt.ttpictureserver.model.user.domain.dto.UserRegisterRequest;
import com.tt.ttpictureserver.model.user.domain.entity.User;
import com.tt.ttpictureserver.model.user.domain.vo.LoginUserVo;
import com.tt.ttpictureserver.model.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author NoBug
 * @version 1.0
 * @project tt-picture-server
 * @description: 用户管理
 * @date 2026-01-05 17:52
 */

@RestController()
@RequestMapping("/user")
@Api(tags = "用户管理")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    @ApiOperation("用户注册")
    public BaseResponse<String> Register(UserRegisterRequest userRegisterRequest) {
        return userService.userRegister(userRegisterRequest);
    }


    @PostMapping("/login")
    @ApiOperation("用户登录")
    public BaseResponse<LoginUserVo> Register(@RequestBody UserLoginRequest UserLoginRequest, HttpServletRequest request) {
        String userAccount = UserLoginRequest.getUserAccount();
        String password = UserLoginRequest.getPassword();
        LoginUserVo loginUserVo = userService.userLogin(userAccount,password,request);
       return  BaseResponse.success(loginUserVo);
    }

    @GetMapping("/loginUserInfo")
    @ApiOperation("获取登录的用户信息")
    public BaseResponse<LoginUserVo> getLoginUserInfo( HttpServletRequest request) {
        User user =  userService.getLoginUser(request);
        return BaseResponse.success(userService.getLoginUserVo(user));
    }

}
