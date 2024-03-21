package org.skillup.infrastructure.redis;

import com.alibaba.fastjson2.JSON;
import org.junit.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RedisRepoTest {
    @Autowired
    RedisRepo redisRepo;
    public static String KEY = "name";
    public static String VALUE = "skillup";
    @AfterEach
    public void cleanDataCreated() {
        // clean data
        redisRepo.set(KEY, "Clean it!");
        System.out.println("---clean data---");
    }
    @Test
    public void setAndGetValueTest() {
        redisRepo.set(KEY, VALUE);
        assertEquals(VALUE, JSON.parseObject(redisRepo.get(KEY), String.class));
    }
}