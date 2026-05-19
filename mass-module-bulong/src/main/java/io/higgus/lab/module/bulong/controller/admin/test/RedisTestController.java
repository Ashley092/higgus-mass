package io.higgus.lab.module.bulong.controller.admin.test;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Redis 测试控制器
 *
 * 用途：验证 Redis 配置是否正确，学习 Redis 操作
 *
 * 测试步骤：
 * 1. 启动应用
 * 2. 依次访问下面的接口，观察控制台日志
 * 3. 使用 Redis 客户端（如 RedisInsight）查看数据
 */
@Slf4j
@RestController
@RequestMapping("/admin/test/redis")
public class RedisTestController {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private static final String TEST_KEY = "test:redis:demo";
    private static final String COUNTER_KEY = "test:redis:counter";

    // ==================== 基础操作 ====================

    /**
     * 1. 测试连接 + 设置值
     * 访问: GET http://localhost:8080/admin/test/redis/set?value=hello
     */
    @GetMapping("/set")
    public Map<String, Object> setValue(@RequestParam(defaultValue = "hello") String value) {
        log.info("=== Redis SET 操作 ===");
        log.info("设置 key={}, value={}", TEST_KEY, value);

        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        ops.set(TEST_KEY, value, 60, TimeUnit.SECONDS);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("key", TEST_KEY);
        result.put("value", value);
        result.put("ttl", "60秒");
        result.put("message", "已设置成功，请访问 /get 查看");
        return result;
    }

    /**
     * 2. 获取值
     * 访问: GET http://localhost:8080/admin/test/redis/get
     */
    @GetMapping("/get")
    public Map<String, Object> getValue() {
        log.info("=== Redis GET 操作 ===");
        log.info("读取 key={}", TEST_KEY);

        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        Object value = ops.get(TEST_KEY);

        Map<String, Object> result = new HashMap<>();
        result.put("key", TEST_KEY);
        result.put("value", value);
        result.put("exists", value != null);

        if (value == null) {
            result.put("message", "值为空，请先访问 /set 设置");
        } else {
            result.put("message", "读取成功！");
        }
        return result;
    }

    /**
     * 3. 删除值
     * 访问: GET http://localhost:8080/admin/test/redis/del
     */
    @GetMapping("/del")
    public Map<String, Object> deleteValue() {
        log.info("=== Redis DEL 操作 ===");
        log.info("删除 key={}", TEST_KEY);

        Boolean deleted = redisTemplate.delete(TEST_KEY);

        Map<String, Object> result = new HashMap<>();
        result.put("key", TEST_KEY);
        result.put("deleted", deleted);
        result.put("message", deleted ? "删除成功" : "key不存在");
        return result;
    }

    // ==================== 缓存计数器（模拟实际业务） ====================

    /**
     * 4. 访问计数器（模拟接口调用统计）
     *
     * 业务场景：统计某个接口被调用了多少次
     *
     * 访问: GET http://localhost:8080/admin/test/redis/counter
     *
     * 多次访问，观察 count 是否递增
     */
    @GetMapping("/counter")
    public Map<String, Object> incrementCounter() {
        log.info("=== Redis 计数器操作 ===");
        log.info("递增 key={}", COUNTER_KEY);

        ValueOperations<String, Object> ops = redisTemplate.opsForValue();

        // 获取当前值
        Object current = ops.get(COUNTER_KEY);
        Long count = current != null ? Long.parseLong(current.toString()) : 0L;

        // 递增
        Long newCount = ops.increment(COUNTER_KEY);

        Map<String, Object> result = new HashMap<>();
        result.put("previousCount", count);
        result.put("currentCount", newCount);
        result.put("message", "计数器已递增，刷新页面查看变化");
        return result;
    }

    /**
     * 5. 获取计数器
     * 访问: GET http://localhost:8080/admin/test/redis/counter/get
     */
    @GetMapping("/counter/get")
    public Map<String, Object> getCounter() {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        Object current = ops.get(COUNTER_KEY);

        Map<String, Object> result = new HashMap<>();
        result.put("key", COUNTER_KEY);
        result.put("count", current != null ? current : 0);
        return result;
    }

    // ==================== Hash 操作 ====================

    /**
     * 6. Hash 操作（存储对象）
     *
     * 业务场景：缓存用户信息
     *
     * 访问: GET http://localhost:8080/admin/test/redis/hash
     */
    @GetMapping("/hash")
    public Map<String, Object> hashOperation() {
        log.info("=== Redis Hash 操作 ===");

        String hashKey = "test:redis:user:1001";

        // 写入 Hash
        redisTemplate.opsForHash().put(hashKey, "name", "张三");
        redisTemplate.opsForHash().put(hashKey, "age", "25");
        redisTemplate.opsForHash().put(hashKey, "city", "北京");

        // 设置过期时间
        redisTemplate.expire(hashKey, 5, TimeUnit.MINUTES);

        // 读取 Hash
        Map<Object, Object> user = redisTemplate.opsForHash().entries(hashKey);

        Map<String, Object> result = new HashMap<>();
        result.put("key", hashKey);
        result.put("data", user);
        result.put("message", "用户信息已缓存5分钟");
        return result;
    }

    // ==================== 健康检查 ====================

    /**
     * 7. 健康检查 - 验证 Redis 是否连接成功
     * 访问: GET http://localhost:8080/admin/test/redis/health
     */
    @GetMapping("/health")
    public Map<String, Object> healthCheck() {
        Map<String, Object> result = new HashMap<>();

        try {
            // 测试写入
            String pingKey = "test:redis:ping";
            redisTemplate.opsForValue().set(pingKey, "pong", 10, TimeUnit.SECONDS);

            // 测试读取
            Object value = redisTemplate.opsForValue().get(pingKey);

            // 测试删除
            redisTemplate.delete(pingKey);

            result.put("success", true);
            result.put("status", "UP");
            result.put("message", "Redis 连接正常！");
            result.put("operations", "SET/GET/DEL 全部成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("status", "DOWN");
            result.put("message", "Redis 连接失败！");
            result.put("error", e.getMessage());

            log.error("Redis 连接测试失败", e);
        }

        return result;
    }

    // ==================== 测试缓存穿透 ====================

    /**
     * 8. 模拟缓存穿透场景
     *
     * 业务场景：根据产品编号查询产品
     *
     * 访问: GET http://localhost:8080/admin/test/redis/cache?productCode=P001
     * 访问: GET http://localhost:8080/admin/test/redis/cache?productCode=NOT_EXIST
     */
    @GetMapping("/cache")
    public Map<String, Object> cacheDemo(@RequestParam String productCode) {
        log.info("=== 缓存演示 ===");
        log.info("查询产品: productCode={}", productCode);

        String cacheKey = "product:code:" + productCode;
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();

        // 1. 先查缓存
        Object cached = ops.get(cacheKey);
        if (cached != null) {
            log.info("【缓存命中】直接从Redis获取, value={}", cached);
            Map<String, Object> result = new HashMap<>();
            result.put("source", "CACHE");
            result.put("key", cacheKey);
            result.put("value", cached);
            result.put("tip", "刷新页面，value 不变（来自缓存）");
            return result;
        }

        // 2. 缓存未命中，模拟查询数据库
        log.info("【缓存未命中】模拟查询数据库...");
        String dbValue = "ProductData_" + productCode;

        // 3. 写入缓存
        ops.set(cacheKey, dbValue, 5, TimeUnit.MINUTES);
        log.info("【写入缓存】已保存到Redis, key={}, value={}", cacheKey, dbValue);

        Map<String, Object> result = new HashMap<>();
        result.put("source", "DATABASE");
        result.put("key", cacheKey);
        result.put("value", dbValue);
        result.put("tip", "首次访问，刷新页面会命中缓存");
        return result;
    }

    // ==================== 清除所有测试数据 ====================

    /**
     * 9. 清除所有测试数据
     * 访问: GET http://localhost:8080/admin/test/redis/clear
     */
    @GetMapping("/clear")
    public Map<String, Object> clearAll() {
        log.info("=== 清除所有测试数据 ===");

        // 清除所有以 test: 开头的 key
        var keys = redisTemplate.keys("test:*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("message", "测试数据已清除");
        result.put("clearedKeys", keys != null ? keys.size() : 0);
        return result;
    }
}
