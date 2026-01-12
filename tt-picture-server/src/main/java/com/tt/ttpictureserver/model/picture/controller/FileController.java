package com.tt.ttpictureserver.model.picture.controller;

import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.COSObjectInputStream;
import com.qcloud.cos.utils.IOUtils;
import com.tt.ttpictureserver.common.BaseResponse;
import com.tt.ttpictureserver.exception.BusinessException;
import com.tt.ttpictureserver.exception.ErrorCode;
import com.tt.ttpictureserver.manager.CosManager;
import com.tt.ttpictureserver.model.picture.domain.dto.PictureUploadRequest;
import com.tt.ttpictureserver.model.picture.domain.dto.UploadPictureResult;
import com.tt.ttpictureserver.model.picture.domain.vo.PictureVo;
import com.tt.ttpictureserver.model.picture.service.PictureService;
import com.tt.ttpictureserver.model.user.domain.entity.User;
import com.tt.ttpictureserver.model.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * @author NoBug
 * @version 1.0
 * @project tt-picture-server
 * @description:
 * @date 2026-01-09 16:27
 */
@Slf4j
@Api(tags = "文件基础操作")
@RestController
public class FileController {

    private final CosManager cosManager;
    private final UserService userService;
    private final PictureService pictureService;

    public FileController(CosManager cosManager, UserService userService, PictureService pictureService) {
        this.cosManager = cosManager;
        this.userService = userService;
        this.pictureService = pictureService;
    }

    @ApiOperation(value = "测试上传文件")
    @PostMapping("/test/upload")
    public BaseResponse<String> testUpload(@RequestParam("file") MultipartFile multipartFile){
        String filename = multipartFile.getOriginalFilename();
        String filePath = String.format("test/%s", filename);
       File file = null;
        try {
            file = File.createTempFile(filePath,null);
            multipartFile.transferTo(file);
            cosManager.putObject(filePath, file);
            return BaseResponse.success(filePath);
        }catch (Exception e){
            log.error("file upload exception,filepath="+filePath, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"上传失败");
        }finally {
            if(file!= null){
                //删除临时文件
                boolean delete = file.delete();
                if(!delete){
                    log.error("file delete fail,filepath="+filePath);
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
            //处理下载到的流
            byte[] bytes = IOUtils.toByteArray(cosObjectInputStream);
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + filepath + "\"");
            response.getOutputStream().write(bytes);
            response.getOutputStream().flush();

        }catch (Exception e){
            log.error("file upload exception,filepath="+filepath, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"下载失败");
        }finally {
            if(cosObjectInputStream != null){
                cosObjectInputStream.close();
            }
        }
    }

    @ApiOperation("上传图片（可重复）")
    @PostMapping("/picture/upload")
    public BaseResponse<PictureVo> uploadPicture(
            @RequestParam("file") MultipartFile multipartFile,
            PictureUploadRequest pictureUploadRequest,
            HttpServletRequest request
    ) {
        User loginUser = userService.getLoginUser(request);
        PictureVo pictureVo = pictureService.uploadPicture(multipartFile, pictureUploadRequest, loginUser);
        return BaseResponse.success(pictureVo);
    }

}
