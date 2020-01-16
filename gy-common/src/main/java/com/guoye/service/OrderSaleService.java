package com.guoye.service;

import com.guoye.util.BaseResult;
import org.g4studio.core.metatype.Dto;

public interface OrderSaleService {

    public BaseResult saveOrder(Dto dto, Long memberId) ;

    public BaseResult saveSaleOutOrder(Dto dto, Long memberId) ;

    public BaseResult saveSaleDistribution(Dto dto, Long memberId) ;

    public BaseResult deleteOutOrderInfo(Dto dto, Long memberId) ;
}
