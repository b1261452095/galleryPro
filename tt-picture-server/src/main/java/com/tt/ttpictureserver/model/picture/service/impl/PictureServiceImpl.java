package com.tt.ttpictureserver.model.picture.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.ciModel.persistence.ImageInfo;
import com.tt.ttpictureserver.common.BaseResponse;
import com.tt.ttpictureserver.config.CosClientConfig;
import com.tt.ttpictureserver.exception.BusinessException;
import com.tt.ttpictureserver.exception.ErrorCode;
import com.tt.ttpictureserver.exception.ThrowUtils;
import com.tt.ttpictureserver.manager.CosManager;
import com.tt.ttpictureserver.mapper.PictureMapper;
import com.tt.ttpictureserver.model.picture.domain.dto.UploadPictureResult;
import com.tt.ttpictureserver.model.picture.domain.entity.Picture;

import com.tt.ttpictureserver.model.picture.domain.vo.PictureVo;
import com.tt.ttpictureserver.model.picture.service.PictureService;

import com.tt.ttpictureserver.model.user.domain.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;

/**
 * @author bianhongbin
 * @description 针对表【picture(图片)】的数据库操作Service实现
 * @createDate 2026-01-09 17:19:41
 */
@Slf4j
@Service
public class PictureServiceImpl extends ServiceImpl<PictureMapper, Picture>
        implements PictureService {


    @Override
    public BaseResponse<PictureVo> uploadPicture(MultipartFile multipartFile, UploadPictureResult uploadPictureResult, User loginUser) {
        return null;
    }
}
