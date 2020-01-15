package com.dxtf.service;

import com.dxtf.util.BaseResult;
import org.g4studio.core.metatype.Dto;

public interface ReturnGoodsService {

    public BaseResult saveReturnOrder(Dto dto, Dto member) throws  Exception;

    public BaseResult deleteReturnInfo(Dto dto, Dto member) throws  Exception;

    public void saveLocationTransfer(Dto dto, Dto member) throws  Exception;

    public void saveNotorderout(Dto dto, Dto member) throws  Exception;

}
