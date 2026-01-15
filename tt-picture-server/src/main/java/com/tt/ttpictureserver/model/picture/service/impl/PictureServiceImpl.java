package com.tt.ttpictureserver.model.picture.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.ciModel.persistence.ImageInfo;
import com.tt.ttpictureserver.common.BaseResponse;
import com.tt.ttpictureserver.config.CosClientConfig;
import com.tt.ttpictureserver.config.PictureDeleteConfig;
import com.tt.ttpictureserver.enums.DeleteStrategyEnum;
import com.tt.ttpictureserver.exception.BusinessException;
import com.tt.ttpictureserver.exception.ErrorCode;
import com.tt.ttpictureserver.exception.ThrowUtils;
import com.tt.ttpictureserver.manager.CosManager;
import com.tt.ttpictureserver.manager.FileManager;
import com.tt.ttpictureserver.mapper.PictureMapper;
import com.tt.ttpictureserver.model.picture.domain.dto.PictureQueryRequest;
import com.tt.ttpictureserver.model.picture.domain.dto.PictureUploadRequest;
import com.tt.ttpictureserver.model.picture.domain.dto.UploadPictureResult;
import com.tt.ttpictureserver.model.picture.domain.entity.Picture;

import com.tt.ttpictureserver.model.picture.domain.vo.PictureVo;
import com.tt.ttpictureserver.model.picture.service.PictureService;

import com.tt.ttpictureserver.model.user.domain.entity.User;
import com.tt.ttpictureserver.model.user.domain.vo.UserVo;
import com.tt.ttpictureserver.model.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author bianhongbin
 * @description 针对表【picture(图片)】的数据库操作Service实现
 * @createDate 2026-01-09 17:19:41
 */
@Slf4j
@Service
public class PictureServiceImpl extends ServiceImpl<PictureMapper, Picture>
        implements PictureService {

    private final FileManager fileManager;
    private final UserService userService;
    private final CosManager cosManager;
    private final PictureDeleteConfig pictureDeleteConfig;
    private final com.tt.ttpictureserver.message.producer.PictureDeleteProducer pictureDeleteProducer;

    public PictureServiceImpl(FileManager fileManager, UserService userService,
            CosManager cosManager, PictureDeleteConfig pictureDeleteConfig,
            com.tt.ttpictureserver.message.producer.PictureDeleteProducer pictureDeleteProducer) {
        this.fileManager = fileManager;
        this.userService = userService;
        this.cosManager = cosManager;
        this.pictureDeleteConfig = pictureDeleteConfig;
        this.pictureDeleteProducer = pictureDeleteProducer;
    }

    @Override
    public PictureVo uploadPicture(MultipartFile multipartFile, PictureUploadRequest pictureUploadRequest,
            User loginUser) {
        // 1. 校验参数
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NO_AUTH_ERROR);
        Long pictureId = null;
        if (pictureUploadRequest.getId() != null) {
            pictureId = pictureUploadRequest.getId();
        }
        if (pictureId != null) {
            boolean exists = this.lambdaQuery().eq(Picture::getId, pictureId).exists();
            ThrowUtils.throwIf(!exists, ErrorCode.NOT_FOUND_ERROR, "图片不存在");
        }
        // 上传图片
        String uploadPathPrefix = String.format("public/%s", loginUser.getId());
        UploadPictureResult uploadPictureResult = fileManager.uploadPicture(multipartFile, uploadPathPrefix);
        log.info("uploadPictureResult: {}", uploadPictureResult);
        // 2. 保存图片信息
        Picture picture = new Picture();
        picture.setUserId(loginUser.getId());
        picture.setUrl(uploadPictureResult.getUrl());
        picture.setName(uploadPictureResult.getPicName());
        picture.setPicSize(uploadPictureResult.getPicSize());
        picture.setPicScale(uploadPictureResult.getPicScale());
        picture.setPicFormat(uploadPictureResult.getPicFormat());
        picture.setPicWidth(uploadPictureResult.getPicWidth());
        picture.setPicHeight(uploadPictureResult.getPicHeight());
        if (pictureId != null) {
            picture.setId(pictureId);
            // 若不为空 更新编辑时间
            picture.setEditTime(new Date());
        }
        boolean result = this.saveOrUpdate(picture);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "保存图片信息失败");
        return PictureVo.objToVo(picture);
    }

    @Override
    public QueryWrapper<Picture> getQueryWrapper(PictureQueryRequest pictureQueryRequest) {
        QueryWrapper<Picture> queryWrapper = new QueryWrapper<>();
        if (pictureQueryRequest == null) {
            return queryWrapper;
        }
        Long id = pictureQueryRequest.getId();
        String name = pictureQueryRequest.getName();
        String introduction = pictureQueryRequest.getIntroduction();
        String category = pictureQueryRequest.getCategory();
        List<String> tags = pictureQueryRequest.getTags();
        Long picSize = pictureQueryRequest.getPicSize();
        Integer picWidth = pictureQueryRequest.getPicWidth();
        Integer picHeight = pictureQueryRequest.getPicHeight();
        Double picScale = pictureQueryRequest.getPicScale();
        String picFormat = pictureQueryRequest.getPicFormat();
        Long userId = pictureQueryRequest.getUserId();
        String sortField = pictureQueryRequest.getSortField();
        String sortOrder = pictureQueryRequest.getSortOrder();

        queryWrapper.eq(ObjectUtil.isNotEmpty(id), "id", id);
        queryWrapper.like(ObjectUtil.isNotEmpty(name), "name", name);
        queryWrapper.like(ObjectUtil.isNotEmpty(introduction), "introduction", introduction);
        queryWrapper.like(ObjectUtil.isNotEmpty(category), "category", category);
        queryWrapper.eq(ObjectUtil.isNotEmpty(picSize), "pic_size", picSize);
        queryWrapper.eq(ObjectUtil.isNotEmpty(picWidth), "pic_width", picWidth);
        queryWrapper.eq(ObjectUtil.isNotEmpty(picHeight), "pic_height", picHeight);
        queryWrapper.eq(ObjectUtil.isNotEmpty(picScale), "pic_scale", picScale);
        queryWrapper.like(ObjectUtil.isNotEmpty(picFormat), "pic_format", picFormat);
        queryWrapper.eq(ObjectUtil.isNotEmpty(userId), "user_id", userId);
        if (ObjectUtil.isNotEmpty(tags)) {
            for (String tag : tags) {
                queryWrapper.like("tag", tag);
            }
        }
        if (ObjectUtil.isNotEmpty(sortField) && ObjectUtil.isNotEmpty(sortOrder)) {
            if (sortOrder.equals("asc")) {
                queryWrapper.orderByAsc(sortField);
            } else {
                queryWrapper.orderByDesc(sortField);
            }
        }
        return queryWrapper;
    }

    @Override
    public PictureVo getPictureVo(Picture picture, HttpServletRequest request) {
        PictureVo pictureVo = PictureVo.objToVo(picture);
        // 关联查询用户信息
        Long userId = picture.getUserId();
        if (userId != null && userId > 0) {
            User user = userService.getById(userId);
            UserVo userVo = userService.getUserVo(user);
            pictureVo.setUser(userVo);
        }
        return pictureVo;
    }

    @Override
    public Page<PictureVo> getPictureVoPage(Page<Picture> picturePage, HttpServletRequest request) {
        List<Picture> pictureList = picturePage.getRecords();
        Page<PictureVo> pictureVoPage = new Page<>(picturePage.getCurrent(), picturePage.getSize(),
                picturePage.getTotal());
        if (CollUtil.isEmpty(pictureList)) {
            return pictureVoPage;
        }
        // 对象列表 转 封装对象
        List<PictureVo> pictureVoList = pictureList.stream().map(PictureVo::objToVo).collect(Collectors.toList());
        // 关联查询用户信息
        Set<Long> userIdSet = pictureList.stream().map(Picture::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 填充信息
        pictureVoList.forEach(pictureVo -> {
            Long userId = pictureVo.getUserId();
            User user = null;
            if (userIdSet.contains(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            pictureVo.setUser(userService.getUserVo(user));
        });
        pictureVoPage.setRecords(pictureVoList);
        return pictureVoPage;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePicture(Long id, User loginUser) {
        // 1. 参数校验
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NO_AUTH_ERROR);

        // 2. 判断图片是否存在
        Picture picture = this.getById(id);
        ThrowUtils.throwIf(picture == null, ErrorCode.NOT_FOUND_ERROR, "图片不存在");

        // 3. 权限校验：仅本人或管理员可删除
        if (!picture.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限删除");
        }

        // 4. 根据配置的策略执行删除
        DeleteStrategyEnum strategy = pictureDeleteConfig.getStrategy();
        log.info("删除图片 ID: {}, 策略: {}", id, strategy.getDescription());

        switch (strategy) {
            case IMMEDIATE:
                // 立即删除：同时删除数据库记录和OSS文件
                deleteImmediately(picture);
                break;
            case SOFT_DELETE:
                // 软删除：只标记删除，不删除OSS文件
                deleteSoftly(picture);
                break;
            case ASYNC:
                // 异步删除：标记删除，发送消息到队列（暂未实现消息队列）
                deleteAsync(picture);
                break;
            default:
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "未知的删除策略");
        }
    }

    /**
     * 立即删除：同时删除数据库记录和OSS文件
     */
    private void deleteImmediately(Picture picture) {
        try {
            // 1. 先删除OSS文件
            String fileKey = extractKeyFromUrl(picture.getUrl());
            if (cn.hutool.core.util.StrUtil.isNotBlank(fileKey)) {
                cosManager.deleteObject(fileKey);
                log.info("OSS文件删除成功: {}", fileKey);
            }

            // 2. 删除数据库记录（物理删除）
            boolean removed = this.removeById(picture.getId());
            ThrowUtils.throwIf(!removed, ErrorCode.OPERATION_ERROR, "删除图片失败");

            log.info("图片删除成功 ID: {}", picture.getId());
        } catch (Exception e) {
            log.error("立即删除图片失败 ID: {}", picture.getId(), e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除图片失败: " + e.getMessage());
        }
    }

    /**
     * 软删除：只标记删除，不删除OSS文件
     */
    private void deleteSoftly(Picture picture) {
        // MyBatis-Plus 的逻辑删除会自动处理
        boolean removed = this.removeById(picture.getId());
        ThrowUtils.throwIf(!removed, ErrorCode.OPERATION_ERROR, "删除图片失败");
        log.info("图片软删除成功 ID: {}, 将在 {} 天后清理OSS文件",
                picture.getId(), pictureDeleteConfig.getRetentionDays());
    }

    /**
     * 异步删除：标记删除，后续通过消息队列异步删除OSS文件
     */
    private void deleteAsync(Picture picture) {
        // 1. 先执行软删除
        boolean removed = this.removeById(picture.getId());
        ThrowUtils.throwIf(!removed, ErrorCode.OPERATION_ERROR, "删除图片失败");

        // 2. 发送删除消息到消息队列
        pictureDeleteProducer.sendDeleteMessage(picture.getId(), picture.getUrl());

        log.info("✅ 异步删除消息已发送到队列: pictureId={}", picture.getId());
    }

    @Override
    public void cleanupExpiredPictures() {
        log.info("开始清理过期的软删除图片...");

        try {
            // 1. 计算过期时间点
            Integer retentionDays = pictureDeleteConfig.getRetentionDays();
            Date expireDate = cn.hutool.core.date.DateUtil.offsetDay(new Date(), -retentionDays);

            // 2. 查询过期的软删除记录（使用自定义 SQL，彻底绕过逻辑删除）
            List<Picture> expiredPictures = this.getBaseMapper().selectDeletedPicturesBeforeDate(expireDate);

            if (CollUtil.isEmpty(expiredPictures)) {
                log.info("没有需要清理的过期图片");
                return;
            }

            log.info("找到 {} 条过期图片记录", expiredPictures.size());
            // tt-pictures-1300273352/public/2008475666659536898/2026-01-15_JZkO4KIfLILMMpvS.png
            // tt-pictures-1300273352/public/2008475666659536898/2026-01-15_JZkO4KIfLILMMpvS.png
            // 3. 批量删除OSS文件
            List<String> fileKeys = expiredPictures.stream()
                    .map(Picture::getUrl)
                    .filter(cn.hutool.core.util.StrUtil::isNotBlank)
                    .map(this::extractKeyFromUrl)
                    .filter(cn.hutool.core.util.StrUtil::isNotBlank)
                    .collect(Collectors.toList());

            if (CollUtil.isNotEmpty(fileKeys)) {
                log.info("准备删除 {} 个OSS文件", fileKeys.size());
                log.debug("待删除的文件列表: {}", fileKeys);

                try {
                    cosManager.deleteObjects(fileKeys);
                    log.info("✅ 批量删除OSS文件成功，共 {} 个文件", fileKeys.size());
                } catch (Exception e) {
                    log.error("❌ 删除OSS文件失败", e);
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除OSS文件失败: " + e.getMessage());
                }
            } else {
                log.info("没有需要删除的OSS文件");
            }

            // 4. 物理删除数据库记录
            List<Long> ids = expiredPictures.stream()
                    .map(Picture::getId)
                    .collect(Collectors.toList());

            // 使用 SQL 直接删除，绕过逻辑删除
            this.getBaseMapper().deletePictureByIds(ids);
            log.info("清理过期图片完成，共清理 {} 条记录", ids.size());

        } catch (Exception e) {
            log.error("清理过期图片失败", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "清理过期图片失败: " + e.getMessage());
        }
    }

    @Override
    public String extractKeyFromUrl(String url) {
        if (cn.hutool.core.util.StrUtil.isBlank(url)) {
            return "";
        }

        try {
            // URL 格式示例: https://bucket-name.cos.region.myqcloud.com/public/123/image.jpg
            // 提取路径部分: public/123/image.jpg

            // 方法1: 使用 URL 类解析
            java.net.URL urlObj = new java.net.URL(url);
            String path = urlObj.getPath();

            // 去掉开头的 /
            if (path.startsWith("/")) {
                path = path.substring(1);
            }

            // 处理双斜杠的情况（兼容旧数据）
            // 例如: //public/123/image.jpg -> /public/123/image.jpg
            if (path.startsWith("/")) {
                path = path.substring(1);
            }

            return path;
        } catch (Exception e) {
            log.warn("从URL提取文件key失败: {}", url, e);

            // 方法2: 简单字符串处理作为降级方案
            int lastSlashIndex = url.lastIndexOf("/");
            if (lastSlashIndex > 0 && lastSlashIndex < url.length() - 1) {
                // 尝试提取域名后的路径
                String domain = url.substring(0, url.indexOf("/", 8)); // 8 是 "https://" 的长度
                return url.substring(domain.length() + 1);
            }

            return "";
        }
    }
}
