package com.tt.ttpictureserver.controller;

import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.COSObjectInputStream;
import com.qcloud.cos.utils.IOUtils;
import com.tt.ttpictureserver.common.BaseResponse;
import com.tt.ttpictureserver.exception.BusinessException;
import com.tt.ttpictureserver.exception.ErrorCode;
import com.tt.ttpictureserver.exception.ThrowUtils;
import com.tt.ttpictureserver.manager.CosManager;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
public class BasicController {

    private final CosManager cosManager;

    public BasicController(CosManager cosManager) {
        this.cosManager = cosManager;
    }

    @GetMapping("/hello222245")
    @ResponseBody
    public BaseResponse<Object> hello(@RequestParam(name = "name") String name) {
        ThrowUtils.throwIf(Objects.equals(name, "龙傲天"), ErrorCode.SYSTEM_ERROR, "我异常了");
        return BaseResponse.success("Hello " + name);
    }

    /**/
    @GetMapping("/test")
    @ResponseBody
    public String test(@RequestParam(name = "name") String name) {
        ThrowUtils.throwIfNot(Objects.equals(name, "龙傲天"), ErrorCode.SYSTEM_ERROR);
        return name;
    }

    @GetMapping("/test2")
    @ResponseBody
    public String test2(@RequestParam(name = "name") String name) {
        ThrowUtils.throwIfNot(Objects.equals(name, "龙傲天"), ErrorCode.SYSTEM_ERROR, "你好啊");
        return name;
    }

    @ApiOperation(value = "测试上传文件")
    @PostMapping("/test/upload")
    public BaseResponse<String> testUpload(@RequestParam("file") MultipartFile multipartFile) {
        String filename = multipartFile.getOriginalFilename();
        String filePath = String.format("test/%s", filename);
        File file = null;
        try {
            file = File.createTempFile(filePath, null);
            multipartFile.transferTo(file);
            cosManager.putObject(filePath, file);
            return BaseResponse.success(filePath);
        } catch (Exception e) {
            log.error("file upload exception,filepath=" + filePath, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败");
        } finally {
            if (file != null) {
                // 删除临时文件
                boolean delete = file.delete();
                if (!delete) {
                    log.error("file delete fail,filepath=" + filePath);
                }
            }
        }
    }

    @ApiOperation(value = "测试下载文件")
    @GetMapping("/test/download")
    public void testDownload(String filepath, HttpServletResponse response) throws IOException {
        COSObjectInputStream cosObjectInputStream = null;
        try {
            COSObject object = cosManager.getObject(filepath);
            cosObjectInputStream = object.getObjectContent();
            // 处理下载到的流
            byte[] bytes = IOUtils.toByteArray(cosObjectInputStream);
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + filepath + "\"");
            response.getOutputStream().write(bytes);
            response.getOutputStream().flush();

        } catch (Exception e) {
            log.error("file upload exception,filepath=" + filepath, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "下载失败");
        } finally {
            if (cosObjectInputStream != null) {
                cosObjectInputStream.close();
            }
        }
    }

}