package com.guoye.enums;

public enum SkuLogTypeEnum {

    INSTORE(0, "入库"),
    OUTSTORE(1, "出库"),
    NO_ORDER_OUTSTORE(2, "非订单出库"),;

    private Integer code;
    private String desc;

    /**
     * 根据code获取平台c码
     */
    public static String getDescByCode(Integer code) {

        String safeEnum = null;

        for (SkuLogTypeEnum uaenum : SkuLogTypeEnum.values()) {
            if (uaenum.code.intValue() == code.intValue()) {
                return uaenum.getDesc();
            }
        }
        return safeEnum;

    }

    private SkuLogTypeEnum(Integer code, String desc) {
        this.desc = desc;
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


}
