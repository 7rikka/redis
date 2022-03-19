package nya.nekoneko.redis;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class RedisClient {
    private final JedisPool jedisPool;

    public RedisClient(String addr, int port, String password, int timeout, int dbIndex) {
        JedisPoolConfig config = new JedisPoolConfig();
        jedisPool = new JedisPool(config, addr, port, timeout, password, dbIndex);
    }

    /**
     * 创建连接, 使用RedisSession对象
     */
    public void start(Consumer<RedisSession> using) {
        try (RedisSession session = new RedisSession(jedisPool.getResource())) {
            using.accept(session);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, Object> getRedisStat() {
        return new HashMap<>() {
            {
                //返回线程从此池获取资源所花费的平均等待时间。
                put("MaxBorrowWaitTimeMillis", jedisPool.getMaxBorrowWaitTimeMillis());
                put("NumActive", jedisPool.getNumActive());
                put("NumWaiters", jedisPool.getNumWaiters());
                put("NumIdle", jedisPool.getNumIdle());
                //返回线程从此池获取资源所花费的平均等待时间。
                put("MeanBorrowWaitTimeMillis", jedisPool.getMeanBorrowWaitTimeMillis());
            }
        };
    }
}
