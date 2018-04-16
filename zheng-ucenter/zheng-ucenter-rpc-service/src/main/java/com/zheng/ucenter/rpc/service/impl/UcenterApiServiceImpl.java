package com.zheng.ucenter.rpc.service.impl;

import com.zheng.ucenter.dao.cache.RedisDao;
import com.zheng.ucenter.dao.mapper.UcenterUserMapper;
import com.zheng.ucenter.dao.model.CmsArticle;
import com.zheng.ucenter.dao.model.dto.CmsArticleDto;
import com.zheng.ucenter.rpc.api.UcenterApiService;
import com.zheng.ucenter.util.FileUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

/**
 * UcenterApiService实现
 * Created by shuzheng on 2017/6/19.
 */
@Service
@Transactional
public class UcenterApiServiceImpl implements UcenterApiService, Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(UcenterApiServiceImpl.class);

    @Value("F:\\workspace\\idea\\zheng-master\\zheng-ucenter\\zheng-ucenter-web\\src\\main\\webapp\\resources")
    private String imgSavePath;

    @Autowired
    UcenterUserMapper ucenterUserMapper;

    @Autowired
    RedisDao redisDao;

    @Override
    public boolean exists(Long phone) {
        return false;
    }

    /**
     * 插入文章
     * @param article
     * @return
     */
    @Override
    public String insertArticle(CmsArticle article) {
        String uuid = UUID.randomUUID().toString();
        article.setPassageUuid(uuid);
        int temp = ucenterUserMapper.insertArticle(article);
        if (temp != 0) {
            return uuid;
        } else {
            return null;
        }
    }

    @Override
    public List<CmsArticleDto> selectArticleByPage(int cursor, int pageSize) {
        List<CmsArticleDto> cmsArticleDtoList = new ArrayList<>();
        cmsArticleDtoList = ucenterUserMapper.selectArticleByPage(cursor, pageSize+1);
        return cmsArticleDtoList;
    }

    /**
     * 上传图片
     * @param file
     * @return
     */
    @Override
    public Object imgUpload(byte[] file, String fileName) {
        String fileName1 = null;
        if (file != null && file.length > 0) {
            try {
                fileName1 = FileUtil.save(file, imgSavePath + "\\coverImgs", fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "resources/coverImgs/" + fileName1;
    }

    /**
     * 处理上传图片数组
     * @param files
     * @return
     */
    @Override
    public Object imgUpload(LinkedHashMap files) {
        List lst = new ArrayList();
        String fileName;
        String fileName1;
        for (Object key : files.keySet()) {
            fileName = key.toString();
            try {
                byte[] bytes = (byte[]) files.get(fileName);
                fileName1 = FileUtil.save(bytes, imgSavePath + "\\passageImgs", fileName);
                lst.add("resources/passageImgs/" + fileName1);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return lst;
    }

}