package com.gy.resource.enums;

/**
 * @author xuyongliang
 * @version V1.0
 * @className FollowTypeEnum
 * @description TODO
 * @date 2020/2/15
 */
public enum FollowTypeEnum {

    // 关注还是取消关注标示   0取消关注、取关，1 关注、点赞
    UN_FOLLOW(0, "取消关注、取关"),
    FOLLOW(1, "关注、点赞");

    private Integer code;

    private String message;

    FollowTypeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public FollowTypeEnum getByCode(Integer code){
        FollowTypeEnum[] values = FollowTypeEnum.values();
        for(FollowTypeEnum value : values){
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
