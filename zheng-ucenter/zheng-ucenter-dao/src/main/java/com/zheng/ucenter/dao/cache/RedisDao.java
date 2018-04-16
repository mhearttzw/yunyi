package com.zheng.ucenter.dao.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisDao {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final JedisPool jedisPool;

    private RuntimeSchema<String> schema = RuntimeSchema.createFrom(String.class);

    public RedisDao(String ip, int port) {
        jedisPool = new JedisPool(ip, port);
    }

    /**
     * 获得token相关信息
     * @param token
     * @return
     */
    public String getToken(String token) {
        //redis操作逻辑
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "token:" + token;
                byte[] bytes = jedis.get(key.getBytes());
                if (bytes != null) {
                    String tokenInfo = schema.newMessage();
                    ProtostuffIOUtil.mergeFrom(bytes, tokenInfo, schema);
                    return tokenInfo;
                }
            } finally {
                jedis.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 返回手机号对应的验证码
     * @param phone
     * @return
     */
    public String getMessageCode(String phone) {
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "phone:" + phone;
                byte[] bytes = jedis.get(key.getBytes());
                if (bytes != null) {
                    String tokenInfo = schema.newMessage();
                    ProtostuffIOUtil.mergeFrom(bytes, tokenInfo, schema);
                    return tokenInfo;
                }
            } finally {
                jedis.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }


    public Long deleteToken(String token) {
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "token:" + token;
                Long result = jedis.del(key);
                return result;
            } finally {
                jedis.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 存放token：userUuid键值对
     * @param token
     * @param userUuid
     * @return
     */
    public Long putToken(String token, String userUuid) {
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "token:" + token;
                byte[] bytes = ProtostuffIOUtil.toByteArray(userUuid, schema,
                        LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                Long result = jedis.setnx(key.getBytes(), bytes);
                return result;
            } finally {
                jedis.close();
            }
        } catch(Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 存放phone:code键值对
     * @param phone
     * @param code
     * @return
     */
    public String saveMessageCode(String phone, String code, String expire) {
        try {
            Jedis jedis = jedisPool.getResource();
            String result = null;
            try {
                String key = "phone:" + phone;
                byte[] bytes = ProtostuffIOUtil.toByteArray(code, schema,
                        LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                // 设置过期时间为5分钟

                if (expire.equals("expire")) {
                    result = jedis.setex(key.getBytes(), 5*60, bytes);
                } else {
                    result = jedis.set(key.getBytes(), bytes);
                }
                return result;
            } finally {
                jedis.close();
            }
        } catch(Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

}
