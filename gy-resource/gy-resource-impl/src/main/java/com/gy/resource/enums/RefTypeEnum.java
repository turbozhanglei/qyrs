package com.gy.resource.enums;

/**
 * @author xuyongliang
 * @version V1.0
 * @className RefTypeEnum
 * @description TODO
 * @date 2020/2/15
 */
public enum RefTypeEnum {

    // 关联类型，0、关注用户，1、资源浏览数，2、资源分享数，3、资源拨打电话数，4、资讯文章分享数，5、资讯文章点赞数，6、资讯文章浏览数
    FOLLOW_USER(0, "关注用户"),
    SOURCE_BROWSE(1, "资源浏览数"),
    SOURCE_SHARE(2, "资源分享数"),
    SOURCE_CALL(3, "资源拨打电话数"),
    INFO_SHARE(4, "资讯文章分享数"),
    INFO_FOLLOW(5, "资讯文章点赞数"),
    INFO_BROWSE(6, "资讯文章浏览数");

    private Integer code;

    private String message;

    RefTypeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public RefTypeEnum getByCode(Integer code){
        RefTypeEnum[] values = RefTypeEnum.values();
        for(RefTypeEnum value : values){
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
