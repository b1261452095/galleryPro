package com.tt.ttpictureserver.model.user.service;

import com.tt.ttpictureserver.common.BaseResponse;
import com.tt.ttpictureserver.model.user.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author bianhongbin
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2026-01-05 14:44:03
*/
public interface UserService extends IService<User> {


     BaseResponse<String> userRegister(String userName);

}
