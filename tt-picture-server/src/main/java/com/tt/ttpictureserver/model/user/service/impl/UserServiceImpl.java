package com.tt.ttpictureserver.model.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tt.ttpictureserver.common.BaseResponse;
import com.tt.ttpictureserver.model.user.domain.User;
import com.tt.ttpictureserver.model.user.service.UserService;
import com.tt.ttpictureserver.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author bianhongbin
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2026-01-05 14:44:03
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Override
    public BaseResponse<String> userRegister(String userName);
}




