package com.zheng.ucenter.rpc.api;

import com.zheng.common.base.BaseService;
import com.zheng.ucenter.dao.model.UcenterUser;
import com.zheng.ucenter.dao.model.UcenterUserExample;

/**
* UcenterUserService接口
* Created by shuzheng on 2017/4/27.
*/
public interface UcenterUserService extends BaseService<UcenterUser, UcenterUserExample> {

    /**
     * 判断用户是否存在
     * @param phone 手机号
     * @return true: 存在， false：不存在
     */
    boolean exists(String phone);

    /**
     * 判断昵称是否唯一
     * @param nickname
     * @return
     */
    boolean nicknameUnique(String nickname);

    /**
     *  判断用户密码是否一致
     * @param username
     * @param password
     * @return
     */
    boolean isUser(String username, String password);

    /**
     * 判断昵称密码是否一致
     * @param nickname
     * @param password
     * @return
     */
    boolean nickMatchPwd(String nickname, String password);

    /**
     * 保存手机号与对应的验证码的md5吗到缓存中
     * @param phone
     * @param code
     * @return
     */
    boolean saveCode(String phone, String code);


    /**
     *  下发短信验证码
     * @param phone 手机号
     * @param content 验证码
     * @return 是否发送成功：true：发送成功，false：发送失败
     */
    boolean sendCode(String phone, String content);

    /**
     *  根据手机号获取验证码
     * @param phone
     * @return 验证码
     */
    String getCode(String phone);

    /**
     * 根据手机号获取密码
     * @param phone
     * @return
     */
    String getPassword(String phone);

    /**
     * 用户密码修改
     * @param password
     * @param userName
     * @return
     */
    int updatePassword(String password, String userName);

    /**
     * 保存token与对应的手机号
     * @param token
     * @param phone 手机号
     */
    void saveToken(String token, String phone);

    /**
     * 存放短信验证码 phone:code
     * @param phone
     * @param code
     */
    void saveMessageCode(String phone, String code, String expire);

    /**
     * 存放用户uuid
     * @param userUuid
     * @return
     */
    String saveToken1(String userUuid);

    /**
     * 查询缓存中token对应的相关信息
     * @param token
     * @return
     */
    String getToken(String token);

    /**
     * 删除用户token
     * @param token
     * @return
     */
    Long deleteToken(String token);

    /**
     * 注册用户
     * @param phone
     * @param password
     * @return
     */
    int insertSimple(String phone, String password, String nickname, String userUuid);

    /**
     * 获取用户uuid
     * @param userName
     * @return
     */
    String getUserUuid(String userName);


}