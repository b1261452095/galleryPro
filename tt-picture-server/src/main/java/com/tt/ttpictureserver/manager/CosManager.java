package com.tt.ttpictureserver.manager;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.*;
import com.qcloud.cos.model.ciModel.persistence.PicOperations;
import com.tt.ttpictureserver.config.CosClientConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author NoBug
 * @version 1.0
 * @project tt-picture-server
 * @description:
 * @date 2026-01-09 16:23
 */
@Slf4j
@Component
public class CosManager {

    @Resource
    private CosClientConfig cosClientConfig;

    @Resource
    private COSClient cosClient;

    /**
     * 上传文件到cos
     * 
     * @param key
     * @param file
     * @return PutObjectResult
     */
    public PutObjectResult putObject(String key, File file) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(cosClientConfig.getBucket(), key, file);
        return cosClient.putObject(putObjectRequest);
    }

    public COSObject getObject(String key) {
        GetObjectRequest getObjectRequest = new GetObjectRequest(cosClientConfig.getBucket(), key);
        return cosClient.getObject(getObjectRequest);
    }

    /**
     * 验证删除权限（安全检查）
     * 
     * @param key 文件路径
     */
    private void validateDeletePermission(String key) {
        if (cn.hutool.core.util.StrUtil.isBlank(key)) {
            throw new IllegalArgumentException("文件路径不能为空");
        }

        // 1. 只允许删除 public 目录下的文件
        if (!key.startsWith("public/") && !key.startsWith("/public/")) {
            log.error("❌ 安全拦截：尝试删除非 public 目录文件: {}", key);
            throw new SecurityException("只允许删除 public 目录下的文件");
        }

        // 2. 防止删除系统文件或配置文件
        String lowerKey = key.toLowerCase();
        if (lowerKey.contains("system/") ||
                lowerKey.contains("config/") ||
                lowerKey.contains("admin/") ||
                lowerKey.contains("..")) { // 防止路径穿越
            log.error("❌ 安全拦截：尝试删除系统文件: {}", key);
            throw new SecurityException("不允许删除系统文件");
        }

        // 3. 文件路径长度检查（防止异常长路径攻击）
        if (key.length() > 500) {
            log.error("❌ 安全拦截：文件路径过长: {}", key);
            throw new SecurityException("文件路径过长");
        }
    }

    /**
     * 上传图片到cos（附带图片信息）
     * 
     * @param key
     * @param file
     * @return PutObjectResult
     */
    public PutObjectResult putPictureObject(String key, File file) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(cosClientConfig.getBucket(), key, file);
        // 对图片进行处理（获取基本信息也视为一种处理）
        PicOperations picOperations = new PicOperations();
        // 1 表示返回原图信息
        picOperations.setIsPicInfo(1);
        putObjectRequest.setPicOperations(picOperations);
        return cosClient.putObject(putObjectRequest);
    }

    /**
     * 删除单个文件
     * 
     * @param key 文件路径
     */
    public void deleteObject(String key) {
        // 安全检查
        validateDeletePermission(key);

        log.warn("🗑️ 删除 COS 文件: {}", key);
        cosClient.deleteObject(cosClientConfig.getBucket(), key);
    }

    /**
     * 批量删除文件
     * 
     * @param keys 文件路径列表
     */
    public void deleteObjects(List<String> keys) {
        if (keys == null || keys.isEmpty()) {
            return;
        }

        // 批量安全检查
        keys.forEach(this::validateDeletePermission);

        log.info("准备批量删除 COS 文件，数量: {}", keys.size());
        log.info("待删除的文件 keys: {}", keys);

        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(cosClientConfig.getBucket());
        List<DeleteObjectsRequest.KeyVersion> keyVersions = keys.stream()
                .map(DeleteObjectsRequest.KeyVersion::new)
                .collect(Collectors.toList());
        deleteObjectsRequest.setKeys(keyVersions);

        try {
            DeleteObjectsResult deleteObjectsResult = cosClient.deleteObjects(deleteObjectsRequest);

            // 输出删除成功的文件
            if (deleteObjectsResult.getDeletedObjects() != null && !deleteObjectsResult.getDeletedObjects().isEmpty()) {
                log.info("✅ 成功删除的文件数: {}", deleteObjectsResult.getDeletedObjects().size());
                deleteObjectsResult.getDeletedObjects().forEach(obj -> log.info("  - 已删除: {}", obj.getKey()));
            } else {
                log.warn("⚠️ 删除结果中没有成功删除的文件信息");
            }

            log.info("COS 批量删除完成，完整结果: {}", deleteObjectsResult);
        } catch (Exception e) {
            log.error("❌ COS 批量删除异常", e);
            throw e;
        }
    }

    /**
     * 检查文件是否存在
     * 
     * @param key 文件路径
     * @return true-存在，false-不存在
     */
    public boolean doesObjectExist(String key) {
        return cosClient.doesObjectExist(cosClientConfig.getBucket(), key);
    }

}
