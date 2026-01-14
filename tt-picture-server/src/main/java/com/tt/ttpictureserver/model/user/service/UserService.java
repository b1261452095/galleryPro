package com.tt.ttpictureserver.model.user.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tt.ttpictureserver.common.BaseResponse;
import com.tt.ttpictureserver.common.DeleteRequest;
import com.tt.ttpictureserver.model.user.domain.dto.*;
import com.tt.ttpictureserver.model.user.domain.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tt.ttpictureserver.model.user.domain.vo.LoginUserVo;
import com.tt.ttpictureserver.model.user.domain.vo.UserVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author bianhongbin
 * @description 针对表【user(用户)】的数据库操作Service
 * @createDate 2026-01-05 14:44:03
 */
public interface UserService extends IService<User> {

     /**
      * 用户注册
      * 
      * @param userRegisterRequest
      * @return BaseResponse<String>
      */
     BaseResponse<String> userRegister(UserRegisterRequest userRegisterRequest);

     /**
      * 用户登录
      * 
      * @param userAccount
      * @param password
      * @param request
      * @return BaseResponse<LoginUserVo>
      */
     LoginUserVo userLogin(String userAccount, String password, HttpServletRequest request);

     /**
      * 获取当前登录用户
      * 
      * @param request
      * @return User
      */
     User getLoginUser(HttpServletRequest request);

     /**
      * 用户数据脱敏
      * 
      * @param User
      * @return LoginUserVo
      */
     LoginUserVo getLoginUserVo(User User);

     /**
      * 密码加盐
      *
      * @param password
      * @return String
      */
     String getEncryptPassword(String password);

     /**
      * 用户列表查询
      *
      * @param userQueryRequest
      * @return QueryWrapper<User>
      */
     QueryWrapper<User> getQueryRequest(UserQueryRequest userQueryRequest);

     /**
      * 用户新增
      * 
      * @param userAddRequest
      * @return BaseResponse<Long>
      */
     BaseResponse<Long> addUser(UserAddRequest userAddRequest);

     /**
      * 用户删除
      * 
      * @param deleteRequest
      * @return BaseResponse<Boolean>
      */
     BaseResponse<Boolean> deleteUser(DeleteRequest deleteRequest);

     /**
      * 用户修修改
      * 
      * @param userUpdateRequest
      * @return BaseResponse<Boolean>
      */
     BaseResponse<Boolean> updateUser(UserUpdateRequest userUpdateRequest);

     /**
      * 获取单个脱敏的用户信息
      * 
      * @param user
      * @return UserVo
      */
     public UserVo getUserVo(User user);

     /**
      * 获取用户列表(脱敏)
      * 
      * @param userList 用户列表
      * @return UserVo
      */
     public List<UserVo> getUserVoList(List<User> userList);

     /**
      * 是否为管理员
      * 
      * @param user 用户
      * @return boolean
      */
     public boolean isAdmin(User user);
}
