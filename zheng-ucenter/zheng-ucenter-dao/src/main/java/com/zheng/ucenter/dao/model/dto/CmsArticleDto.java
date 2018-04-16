package com.zheng.ucenter.dao.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zheng.ucenter.dao.model.CmsArticle;
import com.zheng.ucenter.dao.model.UcenterUser;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CmsArticleDto extends CmsArticle {

    /**
     * 昵称
     *
     * @mbg.generated
     */
    private String nickname;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Byte getSex() {
        return sex;
    }

    public void setSex(Byte sex) {
        this.sex = sex;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    /**
     * 性别(0:未知,1:男,2:女)
     *

     * @mbg.generated
     */
    private Byte sex;

    /**
     * 头像
     *
     * @mbg.generated
     */
    private String avatar;


}
