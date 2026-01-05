package com.tt.ttpictureserver.model.user.service;

import com.tt.ttpictureserver.common.BaseResponse;
import com.tt.ttpictureserver.model.user.domain.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author bianhongbin
 * @description 针对表【user(用户)】的数据库操作Service
 * @createDate 2026-01-05 14:44:03
 */
public interface UserService extends IService<User> {

     /**
      * 用户注册
      * 
      * @param userName
      * @return
      */
     BaseResponse<String> userRegister(String userName, String password, String surePassword);

}
