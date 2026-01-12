package com.tt.ttpictureserver.manager;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.GetObjectRequest;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.ciModel.persistence.ImageInfo;
import com.qcloud.cos.model.ciModel.persistence.PicOperations;
import com.tt.ttpictureserver.config.CosClientConfig;
import com.tt.ttpictureserver.exception.BusinessException;
import com.tt.ttpictureserver.exception.ErrorCode;
import com.tt.ttpictureserver.exception.ThrowUtils;
import com.tt.ttpictureserver.model.picture.domain.dto.UploadPictureResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;

/**
 * @author NoBug
 * @version 1.0
 * @project tt-picture-server
 * @description:
 * @date 2026-01-09 16:23
 */
@Slf4j
@Component
public class FileManager {

    @Resource
    private CosClientConfig cosClientConfig;

    @Resource
    private COSClient cosClient;

    @Resource
    private CosManager cosManager;

    /**
     * 上传图片到cos（附带图片信息）
     * @param key
     * @param file
     * @return PutObjectResult
     */
    public PutObjectResult putPictureObject(String key, File file) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(cosClientConfig.getBucket(), key, file);
        //对图片进行处理（获取基本信息也视为一种处理）
        PicOperations picOperations = new PicOperations();
        //1 表示返回原图信息
        picOperations.setIsPicInfo(1);
        putObjectRequest.setPicOperations(picOperations);
        return cosClient.putObject(putObjectRequest);
    }

    public UploadPictureResult uploadPictureInfo(MultipartFile multipartFile, String uploadPathPrefix) {
        // 校验图片
        validPicture(multipartFile);
        // 上传图片到cos
        String uuid = RandomUtil.randomString(16);
        String filename = multipartFile.getOriginalFilename();
        String uploadFileName = String.format("%s_%s.%s", DateUtil.formatDate(new Date()), uuid, FileUtil.getSuffix(filename));
        String uploadPath = String.format("/%s/%s", uploadPathPrefix, uploadFileName);
        File file = null;
        try{
            file=File.createTempFile(uploadPath,null);
            multipartFile.transferTo(file);
            //上传图片
            PutObjectResult putObjectResult = cosManager.putPictureObject(uploadPath, file);
            ImageInfo imageInfo = putObjectResult.getCiUploadResult().getOriginalInfo().getImageInfo();
            //封装返回结果
            UploadPictureResult uploadPictureResult = new UploadPictureResult();
            int width = imageInfo.getWidth();
            int height = imageInfo.getHeight();
            double picScale = NumberUtil.round(width * 1.0 / height, 2).doubleValue();
            uploadPictureResult.setPicName(FileUtil.mainName(filename));
            uploadPictureResult.setPicWidth(width);
            uploadPictureResult.setPicHeight(height);
            uploadPictureResult.setPicScale(picScale);
            uploadPictureResult.setPicFormat(imageInfo.getFormat());
            uploadPictureResult.setPicSize(FileUtil.size(file));
            uploadPictureResult.setUrl(cosClientConfig.getHost() +"/" + uploadPath);
            return uploadPictureResult;
        }catch (Exception e){
            log.error("上传图片到cos失败：{}",e.getMessage());
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"上传失败");
        }finally {
            this.deleteTempFile(file);
        }
    }

    /**
     * 校验文件
     *
     * @param multipartFile 文件
     */
    public void validPicture(MultipartFile multipartFile) {
        ThrowUtils.throwIf(multipartFile == null, ErrorCode.PARAMS_ERROR, "文件不能为空");
        // 1.校验文件大小
        long size = multipartFile.getSize();
        final long maxSize = cosClientConfig.getMaxSize();
        ThrowUtils.throwIf(size > maxSize, ErrorCode.PARAMS_ERROR, "文件大小不能超过" + (maxSize / 1024 / 1024) + "M");

        // 2.校验文件后缀
        String suffix = FileUtil.getSuffix(multipartFile.getOriginalFilename());
        if (!cosClientConfig.getSuffixList().contains(suffix)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"文件类型错误");
        }

    }

    public void deleteTempFile(File file) {
        if (file == null){
            return;
        }
        //删除临时文件
        boolean delete = file.delete();
        if (!delete){
            log.error("删除临时文件失败：filepath = {}",file.getAbsolutePath());
        }
    }
}
