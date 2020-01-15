package com.dxtf;

import com.dxtf.service.BizService;
import com.dxtf.util.OrderNumberUtils;
import com.dxtf.util.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ZgtOrderApplicationTests {

    @Autowired
    OrderNumberUtils redisService;

    @Test
    public void contextLoads() throws  Exception{

        String cuntomerNo = redisService.getCuntomerNo();
        System.out.println(cuntomerNo);
        System.out.println("*****************************");
    }

}
