package com.tt.ttpictureserver.model.user.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tt.ttpictureserver.common.BaseResponse;
import com.tt.ttpictureserver.model.user.domain.dto.UserLoginRequest;
import com.tt.ttpictureserver.model.user.domain.dto.UserQueryRequest;
import com.tt.ttpictureserver.model.user.domain.dto.UserRegisterRequest;
import com.tt.ttpictureserver.model.user.domain.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tt.ttpictureserver.model.user.domain.vo.LoginUserVo;
import org.springframework.beans.BeanUtils;

import javax.servlet.http.HttpServletRequest;

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
      * @param userAccount
      * @param password
      * @param request
      * @return BaseResponse<LoginUserVo>
      */
     LoginUserVo userLogin(String userAccount, String password, HttpServletRequest request);


     /**
      * 获取当前登录用户
      * @param request
      * @return User
      */
     User getLoginUser(HttpServletRequest request);

     /**
      * 用户数据脱敏
      * @param User
      * @return LoginUserVo
      */
     LoginUserVo  getLoginUserVo(User User);

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
}
