package com.tt.ttpictureserver.model.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tt.ttpictureserver.common.BaseResponse;
import com.tt.ttpictureserver.model.user.domain.dto.UserRegisterRequest;
import com.tt.ttpictureserver.model.user.domain.entity.User;
import com.tt.ttpictureserver.model.user.service.UserService;
import com.tt.ttpictureserver.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * @author bianhongbin
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2026-01-05 14:44:03
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /**
     * 密码加密盐值
     */
    private static final String SALT = "tt_picture";

    @Override
    public BaseResponse<String> userRegister(UserRegisterRequest userRegisterRequest) {
        // 1. 参数校验
        String userAccount = userRegisterRequest.getUserAccount();
        String password = userRegisterRequest.getPassword();
        String surePassword = userRegisterRequest.getSurePassword();


        if (userAccount == null || userAccount.trim().isEmpty()) {
            return BaseResponse.error(40000, "账号不能为空");
        }
        if (password == null || password.trim().isEmpty()) {
            return BaseResponse.error(40000, "密码不能为空");
        }
        if (surePassword == null || surePassword.trim().isEmpty()) {
            return BaseResponse.error(40000, "确认密码不能为空");
        }

        // 2. 账号长度校验（4-16位）
        if (userAccount.length() < 4 || userAccount.length() > 16) {
            return BaseResponse.error(40000, "账号长度应为4-16位");
        }

        // 3. 账号格式校验（只能包含字母、数字、下划线）
        if (!userAccount.matches("^[a-zA-Z0-9_]+$")) {
            return BaseResponse.error(40000, "账号只能包含字母、数字和下划线");
        }

        // 4. 密码长度校验（至少8位）
        if (password.length() < 8) {
            return BaseResponse.error(40000, "密码长度至少8位");
        }

        // 5. 密码强度校验（必须包含字母和数字）
        if (!password.matches(".*[a-zA-Z].*") || !password.matches(".*\\d.*")) {
            return BaseResponse.error(40000, "密码必须包含字母和数字");
        }

        // 6. 两次密码是否一致
        if (!password.equals(surePassword)) {
            return BaseResponse.error(40000, "两次输入的密码不一致");
        }

        // 7. 检查账号是否已存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("useraccount", userAccount);
        long count = this.count(queryWrapper);
        if (count > 0) {
            return BaseResponse.error(40000, "账号已存在");
        }

        // 8. 密码加密（MD5 + 盐值）
        String encryptedPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes(StandardCharsets.UTF_8));

        // 9. 创建用户
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptedPassword);
        user.setUserName("用户_" + userAccount); // 默认昵称
        user.setUserRole("user"); // 默认角色

        // 10. 保存到数据库
        boolean saveResult = this.save(user);
        if (!saveResult) {
            return BaseResponse.error(50000, "注册失败，请稍后重试");
        }

        return BaseResponse.success("注册成功");
    }

}
