package nya.nekoneko.redis;

import redis.clients.jedis.Jedis;

public class RedisSession implements AutoCloseable {
    public static final String REDIS_SUCCESS = "OK";
    /**
     * jedis对象
     */
    private final Jedis jedis;
    /**
     * 构造方法
     */
    RedisSession(Jedis jedis) {
        this.jedis = jedis;
    }

    /**
     * 保存key，默认的过期时间
     */
    public boolean set(String key, String value) {
        String result = jedis.set(key,value);
        return REDIS_SUCCESS.equals(result);
    }

    /**
     * 保存key，指定的过期时间
     */
    public boolean set(String key, String value, long expireSeconds) {
        String result = jedis.setex(key, expireSeconds, value);
        return REDIS_SUCCESS.equals(result);
    }

    /**
     * 如果key不存在，进行操作，返回1
     * 如果key存在，不进行操作，返回0
     */
    public boolean setnx(String key, String value) {
        return 1 == jedis.setnx(key, value);
    }

    /**
     * 获取值
     */
    public String get(String key) {
        return jedis.get(key);
    }

    /**
     * 获取key的过期时间
     */
    public long getExpireSeconds(String key) {
        return jedis.ttl(key);
    }

    /**
     * 设定key的过期时间
     */
    public boolean setExpireSeconds(String key, long expireSeconds) {
        return 1 == jedis.expire(key, expireSeconds);
    }

    @Override
    public void close() {
        jedis.close();
    }
}
