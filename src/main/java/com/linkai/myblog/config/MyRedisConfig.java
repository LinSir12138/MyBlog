package com.linkai.myblog.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkai.myblog.entity.Blog;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Arrays;

/**
* @Description: 自定义的 Redis 配置类
* @Param:
* @return:
* @Author: 林凯
* @Date: 2020/4/29
*/
@Configuration
public class MyRedisConfig {

    //  Redis 用作数据库时用到（用作缓存展示没有用到），存储对象时某人使用 JDK 的序列化，我们要改造一下
    @Bean(name = "myRedisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        // 我们为了自己开发方便，一般直接使用 <String, Object>
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(factory);

        // Json 序列化配置(利用 JSON 解析对象)
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);     // 以及过时了，可以删除
        jackson2JsonRedisSerializer.setObjectMapper(om);

        // String 的序列化
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        // key采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);

        // hash的key也采用String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);

        // value 序 列 化 方 式 采 用 jackson
        template.setValueSerializer(jackson2JsonRedisSerializer);

        // hash 的 value 序 列 化 方 式 采 用 jackson
        template.setHashValueSerializer(jackson2JsonRedisSerializer); template.afterPropertiesSet();

        return template;
    }


    /**
    * @Description:  自定义缓存管理器（Redis缓存用到），实现 key 和 value 的序列化。
     *               因为引入 Redis 之后，SpringBoot使用的缓存管理器是 RedisCacheManager，但是默认使用的是 JDK 的序列化，
     *               我们要稍微改造一下
    * @Param: [redisConnectionFactory]
    * @return: org.springframework.cache.CacheManager
    * @Author: 林凯
    * @Date: 2020/4/29
    */
    @Bean(name = "myCacacheManager")
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                // 设置过期时间
                .entryTtl(Duration.ofDays(2))
                // 设置 key 的序列化
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                // 设置 value 的序列化
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

        return RedisCacheManager.builder(redisConnectionFactory).cacheDefaults(redisCacheConfiguration).build();

    }


    /**
    * @Description: 可以自定义可以 key 生成器，看情况选择使用
     *            注意：是 org.springframework.cache.interceptor.KeyGenerator
    * @Param: []
    * @return: org.springframework.cache.interceptor.KeyGenerator
    * @Author: 林凯
    * @Date: 2020/4/29
    */
    @Bean("mykeyGenerator")       // 加入到容器中，默认组件 id 是方法名，可以修改
    public KeyGenerator keyGenerator() {


        return new KeyGenerator(){

            // 重写 KeyGenerator 接口的方法
            @Override
            public Object generate(Object target, Method method, Object... params) {

                Blog blog = (Blog) params[0];
                System.out.println("blog = " + blog);
                long id = blog.getBid();
                return String.valueOf(id);
            }
        };
    }


}
