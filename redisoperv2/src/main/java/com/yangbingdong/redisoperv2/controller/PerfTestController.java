package com.yangbingdong.redisoperv2.controller;

import com.yangbingdong.redisoperv2.core.command.RedisoperCommand;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ybd
 * @date 2019/11/27
 * @contact yangbingdong1994@gmail.com
 */
@RestController
@RequestMapping("/redisoper")
public class PerfTestController{
    private RedisCodec<String, byte[]> codec = RedisCodec.of(StringCodec.UTF8, ByteArrayCodec.INSTANCE);

    private String keyBytes = "startCoding:key";
    private byte[] valueBytes = "Hello, Redis!".getBytes();

    @Autowired
    private RedisoperCommand command;

    @GetMapping
    public String test() {
        command.setExAsync(keyBytes, 60L, valueBytes);
        command.delAsync(keyBytes);
        return "OK";
    }
}















