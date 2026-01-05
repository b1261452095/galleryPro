package com.tt.ttpictureserver.model.user.controller;

import com.tt.ttpictureserver.common.BaseResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author NoBug
 * @version 1.0
 * @project tt-picture-server
 * @description: 用户管理
 * @date 2026-01-05 17:52
 */

@Controller("/user")
public class UserController {

    @PostMapping('/register')
    BaseResponse<Boolean> Register(){
        return  true

    }

}
