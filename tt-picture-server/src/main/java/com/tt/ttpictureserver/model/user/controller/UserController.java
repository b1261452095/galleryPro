package com.tt.ttpictureserver.model.user.controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tt.ttpictureserver.annotation.AuthCheck;
import com.tt.ttpictureserver.common.BaseResponse;
import com.tt.ttpictureserver.common.DeleteRequest;
import com.tt.ttpictureserver.constant.userConstant;
import com.tt.ttpictureserver.exception.ErrorCode;
import com.tt.ttpictureserver.exception.ThrowUtils;
import com.tt.ttpictureserver.model.user.domain.dto.*;
import com.tt.ttpictureserver.model.user.domain.entity.User;
import com.tt.ttpictureserver.model.user.domain.vo.LoginUserVo;
import com.tt.ttpictureserver.model.user.domain.vo.UserVo;
import com.tt.ttpictureserver.model.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
    public BaseResponse<String> Register(@RequestBody  UserRegisterRequest userRegisterRequest) {
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

    @ApiOperation("新增用户（仅管理员）")
    @PostMapping("/add")
    @AuthCheck(mustRole = userConstant.ADMIN_ROLE)  // 👈 只有管理员可以新增用户
    public BaseResponse<Long> addUser(UserAddRequest userAddRequest) {
        return userService.addUser(userAddRequest);
    }

    @ApiOperation("删除用户（仅管理员）")
    @DeleteMapping("/delete")
    @AuthCheck(mustRole = userConstant.ADMIN_ROLE)  // 👈 只有管理员可以删除用户
    public BaseResponse<Boolean> deleteUser(DeleteRequest deleteRequest){
        return userService.deleteUser(deleteRequest);
    }

    @ApiOperation("更新用户（仅管理员）")
    @PutMapping("/update")
    @AuthCheck(mustRole = userConstant.ADMIN_ROLE)  // 👈 只有管理员可以更新用户
    public BaseResponse<Boolean> updateUser(UserUpdateRequest userUpdateRequest){
        return userService.updateUser(userUpdateRequest);
    }

    @ApiOperation("获取用户列表（仅管理员）")
    @PostMapping("/list")
    @AuthCheck(mustRole = userConstant.ADMIN_ROLE)  // 👈 只有管理员可以查看用户列表
    public BaseResponse<Page<UserVo>> getUserList(@RequestBody UserQueryRequest userQueryRequest){
        ThrowUtils.throwIf(userQueryRequest == null, ErrorCode.PARAMS_ERROR);
        long current = userQueryRequest.getCurrent();
        long pageSize = userQueryRequest.getPageSize();
        Page<User> userPage = userService.page(new Page<>(current, pageSize), userService.getQueryRequest(userQueryRequest));
        
        // 添加 null 检查
        if (userPage == null) {
            return BaseResponse.error(50000, "查询失败");
        }
        
      // 打印调试信息
        System.out.println("userPage.getTotal(): " + userPage.getTotal());
        System.out.println("userPage.getRecords(): " + userPage.getRecords());
        System.out.println("userPage.getRecords() size: " + (userPage.getRecords() != null ? userPage.getRecords().size() : "null"));
        
        Page<UserVo> userVoPage = new Page<>(current, pageSize, userPage.getTotal());
        List<UserVo> userVoList = userService.getUserVoList(userPage.getRecords());
        userVoPage.setRecords(userVoList);
        
        return BaseResponse.success(userVoPage);
    }

}
