package com.gy.resource.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: gaolanyu
 * @date: 2020-02-18
 * @remark:
 */
@Component
@Slf4j
public class ResourceContentFilterTask {
    @Scheduled(cron = "*/10 * * * * ?")
    public void task(){
      log.info("开始处理敏感词");
    }
}
