package com.zheng.ucenter.rpc.api;

import com.zheng.ucenter.dao.model.CmsArticle;
import com.zheng.ucenter.dao.model.dto.CmsArticleDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * upms系统接口
 * Created by shuzheng on 2017/6/19.
 */
public interface UcenterApiService {

    /**
     * 判断手机号是否存在
     * @param phone 手机号
     * @return true: 存在， false：不存在
     */
    boolean exists(Long phone);

    /**
     * 添加文章
     * @param article
     * @return
     */
    String  insertArticle(CmsArticle article);

    /**
     * 分页查询文章
     * @param cursor
     * @param pageSize
     * @return
     */
    List<CmsArticleDto> selectArticleByPage(int cursor, int pageSize);

    /**
     * 上传图片
     * @param file
     * @return
     */
    Object imgUpload(byte[] file, String fileName);

    /**
     * 上传图片数组
     * @param files
     * @return
     */
    Object imgUpload(LinkedHashMap files);


}
