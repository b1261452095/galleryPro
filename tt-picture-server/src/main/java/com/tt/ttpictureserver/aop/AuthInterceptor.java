package com.tt.ttpictureserver.aop;

import com.tt.ttpictureserver.annotation.AuthCheck;
import com.tt.ttpictureserver.exception.BusinessException;
import com.tt.ttpictureserver.exception.ErrorCode;
import com.tt.ttpictureserver.model.user.domain.entity.User;
import com.tt.ttpictureserver.model.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 权限校验 AOP 切面
 * 
 * 工作原理：
 * 1. 拦截所有带 @AuthCheck 注解的方法
 * 2. 获取当前登录用户
 * 3. 校验用户角色是否满足要求
 * 4. 不满足则抛出异常，满足则放行
 */
@Aspect
@Component
@Slf4j
public class AuthInterceptor {

    @Resource
    private UserService userService;

    /**
     * 执行权限校验
     * 
     * @Around：环绕通知，在方法执行前后都可以执行逻辑
     * @annotation(authCheck)：拦截带 @AuthCheck 注解的方法，并获取注解对象
     */
    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        // 1. 获取注解中配置的角色要求
        String mustRole = authCheck.mustRole();
        String[] anyRole = authCheck.anyRole();
        
        // 2. 获取当前请求
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        
        // 3. 获取当前登录用户
        User loginUser = userService.getLoginUser(request);
        String userRole = loginUser.getUserRole();
        
        // 4. 校验权限
        
        // 4.1 如果配置了 mustRole，必须完全匹配
        if (mustRole != null && !mustRole.isEmpty()) {
            if (!mustRole.equals(userRole)) {
                log.warn("权限不足，需要角色: {}, 当前角色: {}", mustRole, userRole);
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限访问");
            }
        }
        
        // 4.2 如果配置了 anyRole，满足其中一个即可
        if (anyRole != null && anyRole.length > 0) {
            boolean hasRole = Arrays.asList(anyRole).contains(userRole);
            if (!hasRole) {
                log.warn("权限不足，需要角色之一: {}, 当前角色: {}", Arrays.toString(anyRole), userRole);
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限访问");
            }
        }
        
        // 5. 权限校验通过，执行原方法
        log.info("权限校验通过，用户: {}, 角色: {}", loginUser.getUserAccount(), userRole);
        return joinPoint.proceed();
    }
}
