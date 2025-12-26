package com.tt.ttpictrueserver.controller;

import com.tt.ttpictrueserver.common.BaseResponse;
import com.tt.ttpictrueserver.exception.BusinessException;
import com.tt.ttpictrueserver.exception.ErrorCode;
import com.tt.ttpictrueserver.exception.ThrowUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Objects;

@Controller
public class BasicController {

    @GetMapping("/hello222245")
    @ResponseBody
    public BaseResponse<Object> hello(@RequestParam(name = "name") String name) {
        ThrowUtils.throwIf(Objects.equals(name, "龙傲天"),ErrorCode.SYSTEM_ERROR,"我异常了");
        return BaseResponse.success("Hello " + name);
    }

    @GetMapping("/test")
    @ResponseBody
    public String test(@RequestParam(name = "name") String name) {
        ThrowUtils.throwIfNot(Objects.equals(name,"龙傲天"),ErrorCode.SYSTEM_ERROR);
        return name;
    }
    @GetMapping("/test2")
    @ResponseBody
    public String test2(@RequestParam(name = "name") String name) {
        ThrowUtils.throwIfNot(Objects.equals(name,"龙傲天"),ErrorCode.SYSTEM_ERROR,"你好啊");
        return name;
    }
}