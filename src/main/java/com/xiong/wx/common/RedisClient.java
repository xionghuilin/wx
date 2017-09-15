/*
package com.xiong.wx.common;

import java.lang.reflect.Type;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;

import com.fasterxml.jackson.databind.JavaType;

*/
/**
 * Redis方法
 *//*

public class RedisClient {

    private RedisTemplate<String, String> redisTemplate;

    public RedisClient(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    */
/**
     * 从redis中取得数据（简单key-value型）
     *
     * @description
     * @param key
     *            redis中的key
     * @return
     * @version 3.4.0
     * @author wangws
     * @date 2014年11月3日
     *//*

    @SuppressWarnings("unchecked")
    public Object get(String key, Type type) {
        if (key == null || (!(type instanceof Class) && !(type instanceof JavaType))) {
            return null;
        }

        ValueOperations<String, String> valueOper = redisTemplate.opsForValue();
        String json = valueOper.get(key);

        if (type instanceof Class) {
            return JsonUtils.json2Object(json, (Class<Object>) type);
        } else if (type instanceof JavaType) {
            return JsonUtils.json2Object(json, (JavaType) type);
        } else {
            return null;
        }
    }

    */
/**
     * 存入数据至redis（简单key-value型）
     *
     * @description
     * @param key
     *            redis中的key
     * @param value
     *            要存入的数据，会转换成json字符串存入redis
     * @param expire
     *            过期时间(单位：秒)，如果<0,则为无过期时间，如果等于零，会删除当前数据，如果为null，则延续之前的过期时间
     * @version 3.4.0
     * @author wangws
     * @date 2014年11月3日
     *//*

    public void set(final String key, final Object value, Long expire) {
        if (key == null || value == null) {
            return;
        }

        ValueOperations<String, String> valueOper = redisTemplate.opsForValue();
        expire = expire == null ? redisTemplate.getExpire(key, TimeUnit.SECONDS) : expire;

        if (expire == null || expire < 0) {
            valueOper.set(key, JsonUtils.object2Json(value));
        } else if (expire == 0L) {
            redisTemplate.delete(key);
        } else {
            valueOper.set(key, JsonUtils.object2Json(value), expire, TimeUnit.SECONDS);
        }
    }

    */
/**
     * 从redis删除一条数据
     *
     * @description
     * @param key
     *            redis中的key
     * @version 3.4.0
     * @author wangws
     * @date 2014年11月4日
     *//*

    public void delete(final String key) {
        if (key == null) {
            return;
        }

        redisTemplate.delete(key);
    }

    */
/**
     * 存入数据至redis（SortedSet（有序集合））
     *
     * @description
     * @param key
     * @param value
     * @param score
     * @version 3.5.0
     * @author wangws
     * @date 2014年11月18日
     *//*

    public void zsetAdd(final String key, final Object value, final double score) {
        if (key == null || value == null) {
            return;
        }

        ZSetOperations<String, String> zsetOper = redisTemplate.opsForZSet();
        zsetOper.add(key, JsonUtils.object2Json(value), score);
    }



    */
/**
     * 存入多条数据至redis（SortedSet（有序集合））
     *
     * @description
     * @param key
     * @param value
     * @version 3.5.0
     * @author wangws
     * @date 2014年11月18日
     *//*

    public void zsetAdd(final String key, final Set<TypedTuple<String>> tuples) {
        if (key == null || tuples == null) {
            return;
        }

        ZSetOperations<String, String> zsetOper = redisTemplate.opsForZSet();
        zsetOper.add(key, tuples);
    }

    */
/**
     * 计算SortedSet（有序集合）某key对应的set的size
     *
     * @description
     * @param key
     * @return
     * @version 3.5.0
     * @author wangws
     * @date 2014年11月18日
     *//*

    public Long zsetCart(final String key) {
        ZSetOperations<String, String> zsetOper = redisTemplate.opsForZSet();
        return zsetOper.zCard(key);
    }

    */
/**
     * 取得score在区间内的数据,按score从小到大排列（SortedSet（有序集合））
     *
     * @description
     * @param key
     * @param start
     * @param end
     * @version 3.5.0
     * @author wangws
     * @date 2014年11月18日
     *//*

    @SuppressWarnings("unchecked")
    public Set<Object> zsetRange(final String key, long start, long end, Type type) {
        if (key == null || (!(type instanceof Class) && !(type instanceof JavaType))) {
            return null;
        }

        Set<Object> oSet = null;

        ZSetOperations<String, String> zsetOper = redisTemplate.opsForZSet();
        Set<String> sSet = zsetOper.range(key, start, end);
        if (sSet != null) {
            oSet = new LinkedHashSet<>(sSet.size());
            if (type instanceof Class) {
                for (String json : sSet) {
                    oSet.add(JsonUtils.json2Object(json, (Class<Object>) type));
                }
            } else if (type instanceof JavaType) {
                for (String json : sSet) {
                    oSet.add(JsonUtils.json2Object(json, (JavaType) type));
                }
            }
        }

        return oSet;
    }



    */
/**
     * 取得score在区间内的数据,按score从大到小排列（SortedSet（有序集合））
     *
     * @description
     * @param key
     * @param start
     * @param end
     * @version 3.5.0
     * @author wangws
     * @date 2014年11月18日
     *//*

    @SuppressWarnings("unchecked")
    public Set<Object> zsetRevrange(final String key, long start, long end, Type type) {
        if (key == null || (!(type instanceof Class) && !(type instanceof JavaType))) {
            return null;
        }

        Set<Object> oSet = null;

        ZSetOperations<String, String> zsetOper = redisTemplate.opsForZSet();
        Set<String> sSet = zsetOper.reverseRange(key, start, end);

        if (sSet != null) {
            oSet = new LinkedHashSet<>(sSet.size());
            if (type instanceof Class) {
                for (String json : sSet) {
                    oSet.add(JsonUtils.json2Object(json, (Class<Object>) type));
                }
            } else if (type instanceof JavaType) {
                for (String json : sSet) {
                    oSet.add(JsonUtils.json2Object(json, (JavaType) type));
                }
            }
        }

        return oSet;
    }

    */
/**
     * 设置redis key的过期时间
     *
     * @description
     * @param key
     *            redis中的key
     * @param expire
     *            过期时间(单位：秒)
     * @version 3.4.0
     * @author wangws
     * @date 2014年12月2日
     *//*

    public void expire(String key, long expire) {
        if (key == null) {
            return;
        }

        redisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }

    */
/**
     * 取得redis key的过期时间（单位：毫秒。注意这里是毫秒）
     *
     * @description
     * @param key
     *            redis中的key
     * @version 4.1.0
     * @author wangws
     * @date 2014年12月26日
     *//*

    public long getExpire(String key) {

        return redisTemplate.getExpire(key, TimeUnit.MILLISECONDS);
    }

    */
/**
     * 判断redis是否存在该key
     *
     * @description
     * @param key
     *            redis中的key
     * @version 4.1.0
     * @author wangws
     * @date 2014年12月19日
     *//*

    public boolean hasKey(final String key) {

        return redisTemplate.hasKey(key);
    }

}
*/
