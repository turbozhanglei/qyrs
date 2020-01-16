package com.guoye.schedule;

import com.guoye.service.BizService;
import com.guoye.service.InStockService;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.resource.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 定时任务
 */
@Configuration
public class SaticScheduleTask {

    @Autowired
    InStockService inStockService;

    @Autowired
    BizService bizService;

    //3.添加定时任务
    @Scheduled(cron = "0 0/10 * * * ?")
    private void configureTasks() {
        //状态 0：待审核 1：审核通过 2:审核拒绝
        Dto dto = new BaseDto();
        dto.put("tableName", "order_buy");
        dto.put("method", "updateStatus");
        dto.put("updator", 1);
        dto.put("status", "1");

        //查询所有待审核的数据
        List<Dto> list = bizService.queryList("order_buy.queryWaitCheckOrderIds", new BaseDto());
        for (Dto checkList : list) {
            String id = checkList.getAsString("id");
            if (StringUtils.isNotEmpty(id)) {
                try {
                    inStockService.doOrderCheck(dto, 1l, id);
                } catch (Exception e) {
                    e.printStackTrace();
                    Dto update = new BaseDto();
                    update.put("tableName", "order_buy");
                    update.put("method", "updateIsChecked");
                    update.put("updator", 1);
                    update.put("is_checked", "1");
                    update.put("id", dto.getAsString("id"));
                    bizService.update(update);
                }
            }
        }

    }
}
