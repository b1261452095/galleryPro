package com.tt.ttpictureserver.model.picture.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tt.ttpictureserver.annotation.AuthCheck;
import com.tt.ttpictureserver.common.BaseResponse;
import com.tt.ttpictureserver.common.DeleteRequest;
import com.tt.ttpictureserver.exception.BusinessException;
import com.tt.ttpictureserver.exception.ErrorCode;
import com.tt.ttpictureserver.model.picture.domain.dto.PictureQueryRequest;
import com.tt.ttpictureserver.model.picture.domain.dto.PictureUpdateRequest;
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

/**
 * @author NoBug
 * @version 1.0
 * @project tt-picture-server
 * @description:
 * @date 2026-01-09 16:27
 */
@Slf4j
@Api(tags = "图片管理")
@RequestMapping("/picture")
@RestController
public class PictureController {

    private final UserService userService;
    private final PictureService pictureService;

    public PictureController(UserService userService, PictureService pictureService) {
        this.userService = userService;
        this.pictureService = pictureService;
    }

    @ApiOperation(value = "上传图片", notes = "支持批量上传,最大10MB")
    @PostMapping("/upload")
    public BaseResponse<PictureVo> uploadPicture(
            @RequestParam("file") MultipartFile multipartFile,
            PictureUploadRequest pictureUploadRequest,
            HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        PictureVo pictureVo = pictureService.uploadPicture(multipartFile, pictureUploadRequest, loginUser);
        return BaseResponse.success(pictureVo);
    }

    @ApiOperation("编辑图片信息")
    @PutMapping("/update")
    public BaseResponse<PictureVo> updatePicture(
            @RequestBody PictureUpdateRequest pictureUpdateRequest,
            HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        PictureVo pictureVo = pictureService.updatePicture(pictureUpdateRequest, loginUser);
        return BaseResponse.success(pictureVo);
    }

    @ApiOperation("管理员查询图片列表(有敏感信息)")
    @AuthCheck(mustRole = "admin")
    @PostMapping("/admin/list")
    public BaseResponse<Page<Picture>> listPictureByPage(@RequestBody PictureQueryRequest pictureQueryRequest) {
        int current = pictureQueryRequest.getCurrent();
        int pageSize = pictureQueryRequest.getPageSize();

        Page<Picture> picturePage = pictureService.page(new Page<>(current, pageSize),
                pictureService.getQueryWrapper(pictureQueryRequest));
        return BaseResponse.success(picturePage);

    }

    @ApiOperation("用户查询图片列表(无敏感信息)")
    @PostMapping("/list")
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
    @DeleteMapping("/delete")
    public BaseResponse<String> deletePicture(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest.getId() == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Long id = deleteRequest.getId();

        // 使用新的删除方法（支持配置策略）
        pictureService.deletePicture(id, loginUser);

        return BaseResponse.success("删除成功");
    }
}
