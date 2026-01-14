package com.tt.ttpictureserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tt.ttpictureserver.model.picture.domain.entity.Picture;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author bianhongbin
 * @description 针对表【picture(图片)】的数据库操作Mapper
 * @createDate 2026-01-09 17:19:41
 * @Entity generator.domain.Picture
 */
public interface PictureMapper extends BaseMapper<Picture> {

    /**
     * 查询已软删除且过期的图片（绕过逻辑删除）
     * 
     * @param expireDate 过期时间
     * @return 过期的图片列表
     */
    List<Picture> selectDeletedPicturesBeforeDate(@Param("expireDate") Date expireDate);

    /**
     * 物理删除已逻辑删除的图片数据
     * 
     * @param ids 图片id列表
     * @return 删除的记录数
     */
    int deletePictureByIds(@Param("ids") List<Long> ids);
}
