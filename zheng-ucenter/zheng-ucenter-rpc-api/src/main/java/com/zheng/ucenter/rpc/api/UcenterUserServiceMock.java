package com.zheng.ucenter.rpc.api;

import com.zheng.common.base.BaseServiceMock;
import com.zheng.ucenter.dao.mapper.UcenterUserMapper;
import com.zheng.ucenter.dao.model.UcenterUser;
import com.zheng.ucenter.dao.model.UcenterUserExample;

/**
* 降级实现UcenterUserService接口
* Created by shuzheng on 2017/4/27.
*/
public class UcenterUserServiceMock extends BaseServiceMock<UcenterUserMapper, UcenterUser, UcenterUserExample> implements UcenterUserService {

    @Override
    public boolean exists(String phone) {
        return false;
    }

    @Override
    public boolean nicknameUnique(String nickname) {
        return false;
    }

    @Override
    public boolean isUser(String username, String password) {
        return false;
    }

    @Override
    public boolean nickMatchPwd(String nickname, String password) {
        return false;
    }

    @Override
    public boolean saveCode(String phone, String code) {
        return false;
    }

    @Override
    public boolean sendCode(String phone, String content) {
        return false;
    }

    @Override
    public String getCode(String phone) {
        return null;
    }

    @Override
    public String getPassword(String phone) {
        return null;
    }

    @Override
    public int updatePassword(String password, String userName) {
        return 0;
    }

    @Override
    public void saveToken(String token, String phone) {

    }

    @Override
    public void saveMessageCode(String phone, String code, String expire) {

    }

    @Override
    public String saveToken1(String phone) {
        return null;
    }

    @Override
    public String getToken(String phone) {
        return null;
    }

    @Override
    public Long deleteToken(String token) {
        return null;
    }

    @Override
    public int insertSimple(String phone, String password, String nickname, String userUuid) {
        return 0;
    }

    @Override
    public String getUserUuid(String userName) {
        return null;
    }

    /*@Override
    public int insert(String username, String password) {
        return 0;
    }*/
}
