package com.guoye.service.impl;

import com.guoye.enums.SkuLogTypeEnum;
import com.guoye.service.BizService;
import com.guoye.service.OrderStoreSkuLogService;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("orderStoreSkuLogServiceImpl")
public class OrderStoreSkuLogServiceImpl implements OrderStoreSkuLogService {

    @Autowired
    BizService bizService;

    @Override
    public void insertLog(Long memberId, Long sku_id, int log_type, int num, String skuName, String in_batch_number, String relationNum) {

        Dto insert = new BaseDto();

        String descByCode = SkuLogTypeEnum.getDescByCode(log_type) + "单号";


        StringBuffer remark = new StringBuffer("");
        remark.append(num).append(" 件").append(skuName).append(",");
        remark.append(descByCode).append(" ").append(relationNum);


        insert.put("remark", remark.toString());
        insert.put("in_batch_number", in_batch_number);
        insert.put("sku_id", sku_id);
        insert.put("log_type", log_type);
        insert.put("creator", memberId);
        insert.put("tableName", "orderStoreSkuLog");

        bizService.saveInfo(insert);

    }
}
