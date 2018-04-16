package com.zheng.ucenter.rpc.service.impl;

import com.zheng.common.annotation.BaseService;
import com.zheng.common.base.BaseServiceImpl;
import com.zheng.common.util.MD5Util;
import com.zheng.ucenter.cache.CodeCache;
import com.zheng.ucenter.cache.TokenCache;
import com.zheng.ucenter.dao.cache.RedisDao;
import com.zheng.ucenter.dao.mapper.UcenterUserMapper;
import com.zheng.ucenter.dao.model.UcenterUser;
import com.zheng.ucenter.dao.model.UcenterUserExample;
import com.zheng.ucenter.rpc.api.UcenterUserService;
import com.zheng.ucenter.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
* UcenterUserService实现
* Created by shuzheng on 2017/4/27.
*/
@Service
@Transactional
@BaseService
public class UcenterUserServiceImpl extends BaseServiceImpl<UcenterUserMapper, UcenterUser, UcenterUserExample> implements UcenterUserService {

    private static final Logger logger = LoggerFactory.getLogger(UcenterUserServiceImpl.class);

    @Autowired
    UcenterUserMapper ucenterUserMapper;

    @Autowired
    RedisDao redisDao;

    @Override
    public boolean exists(String phone) {
        UcenterUser ucenterUser = new UcenterUser();
        ucenterUser.setPhone(phone);
        List<UcenterUser> list = ucenterUserMapper.selectByPhone(ucenterUser);
        return list != null && list.size() >= 1;
    }

    /**
     * 判断昵称是否唯一
     * @param nickname
     * @return
     */
    @Override
    public boolean nicknameUnique(String nickname) {
        System.out.println("======nicknameUnique method!");
        List<UcenterUser> list = ucenterUserMapper.selectByNickname(nickname);
        if ( list != null &&list.size() >= 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断用户名、密码是否匹配
     * @param username
     * @param password
     * @return
     */
    @Override
    public boolean isUser(String username, String password) {
        List<UcenterUser> list = ucenterUserMapper.selectByPassword(username, password);
        return list != null && list.size() >= 1;
    }

    /**
     * 判断昵称、密码是否匹配
     * @param nickname
     * @param password
     * @return
     */
    @Override
    public boolean nickMatchPwd(String nickname, String password) {
        List<UcenterUser> list = ucenterUserMapper.selectByNickAndPwd(nickname, password);
        return list != null && list.size() >= 1;
    }

    //TODO 该方法未使用
    @Override
    public boolean saveCode(String phone, String code) {
        //TODO 在真实环境中，改成借助第三方实现
        CodeCache codeCache = CodeCache.getInstance();
        return codeCache.save(phone, MD5Util.md5(code));
    }

    //TODO 该方法未使用
    @Override
    public void saveToken(String token, String userUuid) {
        //使用redis存储token
        redisDao.putToken(token, userUuid);
    }

    @Override
    public boolean sendCode(String phone, String content) {
        logger.info(phone + "  |  " + content);
        return true;
    }

    /**
     * 从redis缓存中返回手机验证码
     * @param phone
     * @return
     */
    @Override
    public String getCode(String phone) {
        return redisDao.getMessageCode(phone);
    }

    /**
     * 获取用户密码
     * @param phone
     * @return
     */
    @Override
    public String getPassword(String phone) {
        return null;
    }

    /**
     * 修改用户密码
     * @param password
     * @param userName
     * @return
     */
    public int updatePassword(String password, String userName) {
        return ucenterUserMapper.updatePassword(password, userName);
    }

    /**
     * 存储短信验证码
     * @param phone
     * @param code
     */
    public void saveMessageCode(String phone, String code, String expire) {
        redisDao.saveMessageCode(phone, code, expire);
    }
    /**
     * 存储用户token
     * @param userUuid
     * @return
     */
    @Override
    public String saveToken1(String userUuid) {
        //1、如果校验通过，生成一个32位的token
        String token = CommonUtil.getUUID();
        //2、保存token与对应的userUuid
        redisDao.putToken(token, userUuid);
        //3、将这个token返回给前端
        return token;
    }

    /**
     * 获得token相关信息
     * @param token
     * @return 成功返回token，失败返回null
     */
    public String getToken(String token) {
        // 1、访问redis
        String tokenInfo = redisDao.getToken(token);
        return tokenInfo;
    }

    /**
     * 删除用户token
     * @param token
     * @return
     */
    @Override
    public Long deleteToken(String token) {
        Long result = redisDao.deleteToken(token);
        return result;
    }

    /**
     * 添加注册用户
     * @param phone
     * @param password
     * @return
     */
    @Override
    public int insertSimple(String phone, String password, String nickname, String userUuid) {
        UcenterUser ucenterUser = new UcenterUser();
        ucenterUser.setPhone(phone);
        ucenterUser.setPassword(password);
        ucenterUser.setUserUuid(userUuid);
        ucenterUser.setNickname(nickname);
        return ucenterUserMapper.insert(ucenterUser);
    }

    /**
     * 获取用户uuid
     * @param userName
     * @return
     */
    @Override
    public String getUserUuid(String userName) {
        //TODO
        return ucenterUserMapper.selectUserUuidByUserName(userName);
    }


}