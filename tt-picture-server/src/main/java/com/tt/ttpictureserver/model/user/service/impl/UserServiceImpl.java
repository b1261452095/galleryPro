package com.tt.ttpictureserver.model.user.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tt.ttpictureserver.common.BaseResponse;
import com.tt.ttpictureserver.common.DeleteRequest;
import com.tt.ttpictureserver.exception.BusinessException;
import com.tt.ttpictureserver.exception.ErrorCode;
import com.tt.ttpictureserver.exception.ThrowUtils;
import com.tt.ttpictureserver.model.user.domain.dto.*;
import com.tt.ttpictureserver.model.user.domain.entity.User;
import com.tt.ttpictureserver.model.user.domain.vo.LoginUserVo;
import com.tt.ttpictureserver.model.user.domain.vo.UserVo;
import com.tt.ttpictureserver.model.user.service.UserService;
import com.tt.ttpictureserver.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.tt.ttpictureserver.constant.userConstant.USER_LOGIN_STATE;

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
        String encryptedPassword = getEncryptPassword(password);

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

    @Override
    public LoginUserVo userLogin(String userAccount, String password, HttpServletRequest request) {
       //校验
        if (StrUtil.isEmpty(userAccount) || StrUtil.isEmpty(password)) {
           throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
       }
        //加密
        String encryptedPassword = getEncryptPassword(password);
        //查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptedPassword);
        User user = this.baseMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户不存在或密码错误");
        }
        //记住用户的状态
        request.getSession().setAttribute(USER_LOGIN_STATE,user);
        return this.getLoginUserVo(user);
    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        //先判断是否已登录
        Object userObj  = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }

        //从数据库查询（可注释）
        long userId = currentUser.getId();
        currentUser = this.getById(userId);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }

    @Override
    public LoginUserVo getLoginUserVo(User user){
        if(user == null){
            return null;
        }
        LoginUserVo loginUserVo = new LoginUserVo();
        BeanUtils.copyProperties(user,loginUserVo);
        return loginUserVo;
    }

    @Override
    public String getEncryptPassword(String password) {
        final String SALT = "tt_picture";
        return DigestUtils.md5DigestAsHex((SALT + password).getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public QueryWrapper<User> getQueryRequest(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = userQueryRequest.getId();
        String userName = userQueryRequest.getUserName();
        String userAvatar = userQueryRequest.getUserAvatar();
        String userProfile = userQueryRequest.getUserProfile();
        String userRole = userQueryRequest.getUserRole();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(ObjUtil.isNotNull(id),"id", id);
        queryWrapper.like(StrUtil.isNotBlank(userName),"user_name", userName);
        queryWrapper.like(StrUtil.isNotBlank(userAvatar),"user_avatar", userAvatar);
        queryWrapper.like(StrUtil.isNotBlank(userProfile),"user_profile", userProfile);
        queryWrapper.eq(StrUtil.isNotBlank(userRole),"user_role", userRole);
        // 只有当 sortField 不为空时才添加排序
        if (StrUtil.isNotBlank(sortField)) {
            queryWrapper.orderBy(true, "asc".equals(sortOrder), sortField);
        }
        return  queryWrapper;
    }

    @Override
    public BaseResponse<Long> addUser(UserAddRequest userAddRequest) {
        ThrowUtils.throwIf(userAddRequest == null, ErrorCode.PARAMS_ERROR);
        User user = new User();
        BeanUtils.copyProperties(userAddRequest,user);
        final String DEFAULT_PASSWORD = "12345678";
        String encryptPassword = getEncryptPassword(DEFAULT_PASSWORD);
        user.setUserPassword(encryptPassword);
        boolean saveResult = this.save(user);
        ThrowUtils.throwIf(saveResult, ErrorCode.SUCCESS);
        return BaseResponse.success(user.getId());
    }


    @Override
    public BaseResponse<Boolean> deleteUser(DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = this.removeById(deleteRequest.getId());
        return BaseResponse.success(b);
    }

    @Override
    public BaseResponse<Boolean> updateUser(UserUpdateRequest userUpdateRequest) {
        if (userUpdateRequest == null || userUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userUpdateRequest,user);
        boolean saveResult = this.updateById(user);
        ThrowUtils.throwIf(!saveResult,ErrorCode.OPERATION_ERROR);
        return BaseResponse.success(true);
    }

    @Override
    public UserVo getUserVo(User user) {
        if(user == null){
            return null;
        }
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user,userVo);
        return userVo;
    }

    @Override
    public List<UserVo> getUserVoList(List<User> userList) {
        if (CollUtil.isEmpty(userList)){
            return new ArrayList<>();
        };
        return userList.stream().map(this::getUserVo).collect(Collectors.toList());
    }


}
