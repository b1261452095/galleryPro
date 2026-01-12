package com.tt.ttpictureserver.model.picture.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qcloud.cos.transfer.Upload;
import com.tt.ttpictureserver.common.BaseResponse;
import com.tt.ttpictureserver.model.picture.domain.dto.UploadPictureResult;
import com.tt.ttpictureserver.model.picture.domain.entity.Picture;
import com.tt.ttpictureserver.model.picture.domain.vo.PictureVo;
import com.tt.ttpictureserver.model.user.domain.entity.User;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
* @author bianhongbin
* @description 针对表【picture(图片)】的数据库操作Service
* @createDate 2026-01-09 17:19:41
*/
public interface PictureService extends IService<Picture> {

    public BaseResponse<PictureVo> uploadPicture(MultipartFile multipartFile, UploadPictureResult uploadPictureResult, User loginUser);
}
