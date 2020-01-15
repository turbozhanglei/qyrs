package com.dxtf.service;

import com.dxtf.util.BaseResult;
import org.g4studio.core.metatype.Dto;

public interface InStockService {

    public BaseResult saveOrder(Dto dto, Long memberId) throws Exception;

    public BaseResult printQualityCard(Dto dto, Long memberId) throws Exception;

    public BaseResult doQualityProduct(Dto dto, Long memberId) throws Exception;


    public BaseResult saveStoreSku(Dto dto, Long memberId) throws Exception;

    public BaseResult intoStoreBuySku(Dto dto, Long memberId) throws Exception;

    /**
     * 处理入库订单表
     *
     * @param dto
     * @param memberId
     * @param i        批次信息
     */
    public String dealWithInStoreOrder(Dto dto, Long memberId, int i);


    /**
     * 批量更新数据入库存
     *
     * @param orderList
     * @param memberId
     */
    public void batchIntoOrderStoreSku(Dto orderList, Long memberId);

    public BaseResult deleteOrderBuySkuDriver(Dto dto) throws Exception;


    /*批量更新数据出库存
    *
    * @param Param
    * */
    public  Dto upSkuStatus(Dto dto) throws Exception;

    public void deleteSkuStore(String in_batch_number, String memberid);


    public void allotStock(Dto dto, Long memberId) throws Exception;

    public void doOrderCheck(Dto dto, Long memberId,String id);


    public void doCloseCheck(Dto dto, Long memberId);
}
