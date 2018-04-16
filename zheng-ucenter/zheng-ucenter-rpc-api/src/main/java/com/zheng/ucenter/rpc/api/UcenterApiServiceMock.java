package com.zheng.ucenter.rpc.api;

import com.zheng.ucenter.dao.model.CmsArticle;
import com.zheng.ucenter.dao.model.dto.CmsArticleDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
* 降级实现UcenterApiService接口
* Created by shuzheng on 2017/6/19.
*/
public class UcenterApiServiceMock implements UcenterApiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UcenterApiServiceMock.class);

    @Override
    public boolean exists(Long phone) {
        return false;
    }

    /**
     * 添加文章
     * @param article
     * @return
     */
    @Override
    public String insertArticle(CmsArticle article) {
        return null;
    }

    @Override
    public List<CmsArticleDto> selectArticleByPage(int cursor, int pageSize) {
        return null;
    }

    /**
     * 上传图片
     * @param file
     * @return
     */
    @Override
    public Object imgUpload(byte[] file, String fileName) {
        return null;
    }

    @Override
    public Object imgUpload(LinkedHashMap files) {
        return null;
    }

}
