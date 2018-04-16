package com.zheng.ucenter.dao.mapper;

import com.zheng.ucenter.dao.model.CmsArticle;
import com.zheng.ucenter.dao.model.UcenterUser;
import com.zheng.ucenter.dao.model.UcenterUserExample;
import java.util.List;

import com.zheng.ucenter.dao.model.dto.CmsArticleDto;
import org.apache.ibatis.annotations.Param;

public interface UcenterUserMapper {
    long countByExample(UcenterUserExample example);

    int deleteByExample(UcenterUserExample example);

    int deleteByPrimaryKey(Integer userId);

    /**
     * 添加注册用户
     * @param record
     * @return
     */
    int insert(UcenterUser record);

    /**
     * 添加文章
     * @param article
     * @return
     */
    int insertArticle(CmsArticle article);

    int insertSelective(UcenterUser record);

    List<UcenterUser> selectByExample(UcenterUserExample example);

    UcenterUser selectByPrimaryKey(Integer userId);

    /**
     * 根据手机号查询用户
     * @param ucenterUser
     * @return
     */
    List<UcenterUser> selectByPhone(UcenterUser ucenterUser);

    /**
     * 根据昵称查询用户
     * @param nickname
     * @return
     */
    List<UcenterUser> selectByNickname(@Param("nickname") String nickname);

    /**
     * 根据手机号和密码查询用户
     * @param phone
     * @param password
     * @return
     */
    List<UcenterUser> selectByPassword(@Param("phone") String phone, @Param("password") String password);

    /**
     * 根据昵称和密码查询用户
     * @param nickname
     * @param password
     * @return
     */
    List<UcenterUser> selectByNickAndPwd(@Param("nickname") String nickname, @Param("password") String password);

    /**
     * 根据用户名（手机号）查询用户uuid
     * @param userName
     * @return
     */
    String selectUserUuidByUserName(@Param("userName") String userName);

    /**
     * 分页文章查询
     * @param cursor
     * @param pageSize
     * @return
     */
    List<CmsArticleDto> selectArticleByPage(@Param("cursor") int cursor, @Param("pageSize") int pageSize);

    /**
     * 修改用户密码
     * @param password
     * @return
     */
    int updatePassword(@Param("password") String password, @Param("phone") String userName);

    int updateByExampleSelective(@Param("record") UcenterUser record, @Param("example") UcenterUserExample example);

    int updateByExample(@Param("record") UcenterUser record, @Param("example") UcenterUserExample example);

    int updateByPrimaryKeySelective(UcenterUser record);

    int updateByPrimaryKey(UcenterUser record);
}