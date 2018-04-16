package com.zheng.ucenter.web.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zheng.common.base.BaseController;
import com.zheng.ucenter.common.constant.UcenterResult;
import com.zheng.ucenter.common.constant.UcenterResultConstant;
import com.zheng.ucenter.dao.model.CmsArticle;
import com.zheng.ucenter.dao.model.dto.CmsArticleDto;
import com.zheng.ucenter.rpc.api.UcenterApiService;
import com.zheng.ucenter.rpc.api.UcenterUserService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("api")
public class ArticleController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(SignController.class);

    @Autowired
    UcenterApiService ucenterApiService;

    @Autowired
    UcenterUserService ucenterUserService;


    /**
     * 上传文章
     * @param
     * @return
     */
    @RequestMapping(value = "/article/insert", method = RequestMethod.POST)
    public UcenterResult insertArticle(@RequestParam(value = "passage", required = false) String passage,
                                       HttpServletRequest request) {
        String userToken = request.getHeader("user_token");

        UcenterResult urs;
        if (userToken == null) {
            urs =  new UcenterResult(UcenterResultConstant.FAILED, "请登录！");
        } else {
            String userUuid = ucenterUserService.getToken(userToken);
            if (userUuid != null) {
                //System.out.println(article);
                //Json字符串转Json对象
                JSONObject passageObject = JSONObject.fromObject(passage);
                CmsArticle article = new CmsArticle();
                System.out.println("=============:" + passageObject.getString("coverImg"));
                if (passageObject.getString("coverImg") != null) {
                    article.setImage(passageObject.getJSONObject("coverImg").toString());
                }
                article.setTitle(passageObject.getString("title"));
                article.setContent(passageObject.getJSONArray("sections").toString());
                article.setAllowcomments(passageObject.getJSONObject("passageSetting").getInt("allowcomments"));
                article.setTopicId(passageObject.getJSONObject("passageSetting").getInt("classify"));
                article.setPrivateType(passageObject.getJSONObject("passageSetting").getInt("public"));
                article.setStatus(1);
                article.setUserUuid(userUuid);
                String passageUuid = ucenterApiService.insertArticle(article);
                if (passageUuid != null) {
                    urs = new UcenterResult<>(UcenterResultConstant.SUCCESS, "");
                } else {
                    urs = new UcenterResult<>(UcenterResultConstant.FAILED, "文章上传失败！");
                }
            } else {
                urs =  new UcenterResult(UcenterResultConstant.FAILED, "请登录！");
            }
        }
        return urs;
    }


    /**
     * 首页上拉刷新文章
     * @param cursor
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/articles/{cursor}/{pageSize}", method = RequestMethod.GET)
    public UcenterResult selectArticleByPage(@PathVariable("cursor") int cursor,
                                             @PathVariable("pageSize") int pageSize) {
        List<CmsArticleDto> cmsArticleList = ucenterApiService.selectArticleByPage(cursor, pageSize);
        if (cmsArticleList.size() <= pageSize) {
            return new UcenterResult(UcenterResultConstant.NO_MORE_PAGE, cmsArticleList);
        } else {
            return new UcenterResult(UcenterResultConstant.SUCCESS, cmsArticleList);
        }
    }


    /**
     * 上传图片
     * @param coverImg
     * @param imgs
     * @return
     *
     */
    @RequestMapping(value = "/img/upload", method = RequestMethod.POST)
    public UcenterResult imgUpload(@RequestParam(value = "coverImg", required = false) MultipartFile coverImg,
                                   @RequestParam(value = "imgs", required = false) MultipartFile[] imgs,
                                   HttpServletRequest request) throws IOException {
        String userToken = request.getHeader("user_token");
        System.out.println("==========:" + userToken);
        UcenterResult urs;
        if (userToken != null && ucenterUserService.getToken(userToken) != null) {
            JSONObject params = new JSONObject();
            byte[] bytes = coverImg.getBytes();
            String fileName = coverImg.getOriginalFilename();
            // TODO 异常处理
            try {
                Object coverImgPath = ucenterApiService.imgUpload(bytes, fileName);
                // 处理图片数组，采用LinkedHashMap有序存放
                LinkedHashMap<String, byte[]> imgMap = new LinkedHashMap<>();
                for (int i = 0; i < imgs.length; i++) {
                    imgMap.put(i + imgs[i].getOriginalFilename(), imgs[i].getBytes());
                }
                Object lst = ucenterApiService.imgUpload(imgMap);
                params.put("coverImg", coverImgPath);
                params.put("imgs", lst);
                urs = new UcenterResult<JSONObject>(UcenterResultConstant.SUCCESS, params);
            } catch(Exception e) {
                logger.error(e.getMessage(), e);
                urs = new UcenterResult(UcenterResultConstant.IMG_UPLOAD_FAILED, "");
            }
        } else {
            urs = new UcenterResult(UcenterResultConstant.FAILED, "请登录！");
        }
        return urs;
    }

}
