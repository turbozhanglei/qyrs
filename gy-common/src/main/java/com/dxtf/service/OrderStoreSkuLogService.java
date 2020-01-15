package com.dxtf.service;

public interface OrderStoreSkuLogService {

    /**
     *
     * @param memberId 用户id
     * @param sku_id 产品id
     * @param log_type 调整类型
     * @param num 数量
     * @param skuName  产品名称
     * @param in_batch_number 批次号
     * @param relationNum 关系编码，出库单，入库单等
     */
    public void insertLog(Long memberId, Long sku_id, int log_type, int num, String skuName, String in_batch_number, String relationNum);
}
