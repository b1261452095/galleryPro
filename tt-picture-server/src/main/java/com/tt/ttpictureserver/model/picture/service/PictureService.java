package com.tt.ttpictureserver.model.picture.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qcloud.cos.transfer.Upload;
import com.tt.ttpictureserver.common.BaseResponse;
import com.tt.ttpictureserver.model.picture.domain.dto.PictureQueryRequest;
import com.tt.ttpictureserver.model.picture.domain.dto.PictureUpdateRequest;
import com.tt.ttpictureserver.model.picture.domain.dto.PictureUploadRequest;
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
    /**
     * 上传图片
     * 
     * @param multipartFile 图片文件
     * @param pictureUploadRequest 图片上传请求 可修改图片
     * @param loginUser 当前登录用户
     * @return PictureVo
     */
    public PictureVo uploadPicture(MultipartFile multipartFile, PictureUploadRequest pictureUploadRequest,
            User loginUser);

    /**
     * 修改图片信息
     * @param pictureUpdateRequest
     * @param loginUser
     * @return PictureVo
     */
    public PictureVo updatePicture(PictureUpdateRequest pictureUpdateRequest, User loginUser);

    /**
     * 查询图片列表
     * 
     * @param pictureQueryRequest 图片查询请求
     * @return QueryWrapper<Picture>
     */
    public QueryWrapper<Picture> getQueryWrapper(PictureQueryRequest pictureQueryRequest);

    /**
     * 获取图片详情（脱敏）
     * 
     * @param picture
     * @param request
     * @return PictureVo
     */
    public PictureVo getPictureVo(Picture picture, HttpServletRequest request);

    /**
     * 获取图片列表（脱敏）
     * 
     * @param picturePage
     * @param request
     * @return Page<PictureVo>
     */
    public Page<PictureVo> getPictureVoPage(Page<Picture> picturePage, HttpServletRequest request);

    /**
     * 删除图片（根据配置策略）
     * 
     * @param id        图片ID
     * @param loginUser 当前登录用户
     */
    void deletePicture(Long id, User loginUser);

    /**
     * 清理过期的软删除图片（定时任务调用）
     */
    void cleanupExpiredPictures();

    /**
     * 从URL中提取OSS文件的key
     * 
     * @param url 图片URL
     * @return OSS文件key
     */
    String extractKeyFromUrl(String url);
}
