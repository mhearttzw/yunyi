package com.zheng.ucenter.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * token缓存，存放token与对应的用户信息（这里存放的是用户手机号）
 */
public class TokenCache {
    private volatile static TokenCache instance;

    private Map<String, String> tokenMap;

    private TokenCache() {
        tokenMap = new HashMap<>();
    }

    public static TokenCache getInstance() {
        if (instance == null) {
            synchronized (TokenCache.class) {
                if (instance == null) {
                    instance = new TokenCache();
                }
            }
        }
        return instance;
    }

    /**
     *  保存token与对应的手机号
     * @param token
     * @param phone
     */
    public void save(String token, String phone) {
        tokenMap.put(token, phone);
    }

    /**
     *  根据token获取用户信息（手机号）
     * @param token
     * @return
     */
    public String getPhone(String token) {
        return tokenMap.get(token);
    }
}
