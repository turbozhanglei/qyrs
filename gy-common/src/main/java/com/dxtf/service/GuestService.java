package com.dxtf.service;

import com.dxtf.util.BaseResult;
import org.g4studio.core.metatype.Dto;

public interface GuestService {

    //计算出warningday_num
    double getWarningdayNum(String warningday);

    //审核
    BaseResult passAudit(Dto dto) throws Exception;

    void initCustomer(Dto dto);
}
