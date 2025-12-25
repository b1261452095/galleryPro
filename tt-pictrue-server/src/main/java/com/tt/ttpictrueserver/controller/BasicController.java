package com.tt.ttpictrueserver.controller;

import com.tt.ttpictrueserver.common.BaseResponse;
import com.tt.ttpictrueserver.exception.BusinessException;
import com.tt.ttpictrueserver.exception.ErrorCode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BasicController {

    @GetMapping("/hello2222")
    @ResponseBody
    public String hello(@RequestParam(name = "name", defaultValue = "unknown user") String name) {

         return BaseResponse.success();
    }

}