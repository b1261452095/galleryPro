package com.tt.ttpictureserver.model.picture.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.COSObjectInputStream;
import com.qcloud.cos.utils.IOUtils;
import com.tt.ttpictureserver.common.BaseResponse;
import com.tt.ttpictureserver.common.DeleteRequest;
import com.tt.ttpictureserver.exception.BusinessException;
import com.tt.ttpictureserver.exception.ErrorCode;
import com.tt.ttpictureserver.exception.ThrowUtils;
import com.tt.ttpictureserver.manager.CosManager;
import com.tt.ttpictureserver.model.picture.domain.dto.PictureQueryRequest;
import com.tt.ttpictureserver.model.picture.domain.dto.PictureUploadRequest;
import com.tt.ttpictureserver.model.picture.domain.entity.Picture;
import com.tt.ttpictureserver.model.picture.domain.vo.PictureVo;
import com.tt.ttpictureserver.model.picture.service.PictureService;
import com.tt.ttpictureserver.model.user.domain.entity.User;
import com.tt.ttpictureserver.model.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
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
@Api(tags = "图片管理")
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

    @ApiOperation("上传图片（可重复）")
    @PostMapping("/picture/upload")
    public BaseResponse<PictureVo> uploadPicture(
            @RequestParam("file") MultipartFile multipartFile,
            PictureUploadRequest pictureUploadRequest,
            HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        PictureVo pictureVo = pictureService.uploadPicture(multipartFile, pictureUploadRequest, loginUser);
        return BaseResponse.success(pictureVo);
    }

    @ApiOperation("获取图片列表(有敏感信息)")
    @PostMapping("/list/page")
    public BaseResponse<Page<Picture>> listPictureByPage(@RequestBody PictureQueryRequest pictureQueryRequest) {
        int current = pictureQueryRequest.getCurrent();
        int pageSize = pictureQueryRequest.getPageSize();

        Page<Picture> picturePage = pictureService.page(new Page<>(current, pageSize),
                pictureService.getQueryWrapper(pictureQueryRequest));
        return BaseResponse.success(picturePage);

    }

    @ApiOperation("获取图片列表(无敏感信息)")
    @PostMapping("/list/page/simple")
    public BaseResponse<Page<PictureVo>> listPictureByPageSimple(@RequestBody PictureQueryRequest pictureQueryRequest,
            HttpServletRequest request) {
        int current = pictureQueryRequest.getCurrent();
        int pageSize = pictureQueryRequest.getPageSize();
        Page<Picture> picturePage = pictureService.page(new Page<>(current, pageSize),
                pictureService.getQueryWrapper(pictureQueryRequest));
        Page<PictureVo> pictureVoPage = pictureService.getPictureVoPage(picturePage, request);
        return BaseResponse.success(pictureVoPage);

    }

    @ApiOperation("删除图片")
    @DeleteMapping("/picture/delete")
    public BaseResponse<String> deletePicture(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest.getId() == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Long id = deleteRequest.getId();
        //判断是否存在
        Picture oldPicture = pictureService.getById(id);
        ThrowUtils.throwIf(oldPicture == null, ErrorCode.NOT_FOUND_ERROR);
        //本人和管理员可删除
        if (!oldPicture.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = pictureService.removeById(id);
        ThrowUtils.throwIf(!b,ErrorCode.SYSTEM_ERROR);
        return BaseResponse.success("删除成功");
    }
}
