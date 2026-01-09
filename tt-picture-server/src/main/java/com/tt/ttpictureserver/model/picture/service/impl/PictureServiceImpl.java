package com.tt.ttpictureserver.model.picture.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tt.ttpictureserver.model.picture.domain.entity.Picture;
import com.tt.ttpictureserver.model.picture.mapper.PictureMapper;
import com.tt.ttpictureserver.model.picture.service.PictureService;
import org.springframework.stereotype.Service;

/**
* @author bianhongbin
* @description 针对表【picture(图片)】的数据库操作Service实现
* @createDate 2026-01-09 17:19:41
*/
@Service
public class PictureServiceImpl extends ServiceImpl<PictureMapper, Picture>
    implements PictureService {

}




