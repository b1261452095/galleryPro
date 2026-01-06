package com.tt.ttpictureserver.model.user.controller;
import com.tt.ttpictureserver.common.BaseResponse;
import com.tt.ttpictureserver.model.user.domain.dto.UserRegisterRequest;
import com.tt.ttpictureserver.model.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
//        BaseResponse<String> result =
        return userService.userRegister(userRegisterRequest);
    }

}
