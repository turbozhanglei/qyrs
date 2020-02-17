package com.gy.resource.enums;

/**
 * @author xuyongliang
 * @version V1.0
 * @className FollowTypeEnum
 * @description TODO
 * @date 2020/2/15
 */
public enum DeleteFlagEnum {

    // 0:未删除,1:删除
    UN_DELETE(0, "未删除"),
    DELETE(1, "删除");

    private Integer code;

    private String message;

    DeleteFlagEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public DeleteFlagEnum getByCode(Integer code){
        DeleteFlagEnum[] values = DeleteFlagEnum.values();
        for(DeleteFlagEnum value : values){
            if(value.getCode().equals(code)){
                return value;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
