package com.dxtf.util;

public class StatusConstant {

	// 返回code 接口正常
	public static String CODE_0000 = "0000";

	// 返回code token失效
	public static String CODE_4000 = "4000";

	// 返回code  接口异常
	public static String CODE_9999 = "9999";

	// 系统参数key
	public static String CONFIG_SYSTEM = "CONFIG_SYSTEM";
	
	// client type  wechat 小程序
	public static String WEC_TYPE = "wec";
	
	// client type  android
	public static String ADR_TYPE = "adr";
	
	// client type  android
	public static String IOS_TYPE = "ios";
	
	// client type  android
	public static String H5_TYPE = "h5";
	
	// client type  android
	public static String PC_TYPE = "pc";
	
	// redis存放60秒：如图片验证码
	public static Long VERIFICATION_TIME = 60L;
	
	// redis存放10分钟：如短信验证码
	public static Long PHONE_CODE_TIME = 60*10L;
	//合同路径
	public static String FILE_PATH = "/usr/local/static/file/";
	// redis存放24小时：如发送短信验证码的次数
	public static Long SMS_FREQUENCY_TIME = 60*60*24L;
	// redis存放3小时：如登录次数
	public static Long LOGIN_FREQUENCY_TIME = 60*60*3L;

	//支付渠道-先锋支付
	public static final String PAY_CHANNEL_UCF = "ucf";
	//支付渠道-市民卡支付
	public static final String PAY_CHANNEL_CC = "cc";
	//银行卡类型-储蓄账户(借记卡)
	public static final String CARD_TYPE_0 = "0";
	//银行卡类型-支票账户
	public static final String CARD_TYPE_1 = "1";
	//银行卡类型-信用卡
	public static final String CARD_TYPE_2 = "2";
	//快捷支付开启状态-开启
	public static final String QUICK_STATUS_Y = "Y";
	//快捷支付开启状态-关闭
	public static final String QUICK_STATUS_N = "N";
	//绑卡状态-已绑定
	public static final Long CARD_STATUS_Y = 1l;
	//绑卡状态-绑定中
	public static final Long CARD_STATUS_S = 0l;
	//绑卡状态-绑定失败
	public static final Long CARD_STATUS_N = 0l;
	//绑卡是否主卡-是主卡
	public static final String CARD_IS_MAIN_Y = "Y";
	//绑卡是否主卡-不是主卡
	public static final String CARD_IS_MAIN_N = "N";
	//银行卡解绑类型-解绑
	public static final String CARD_UNBIND_TYPE_1 = "1";
	//银行卡解绑类型-设置主卡
	public static final String CARD_UNBIND_TYPE_2 = "2";
	//用户投资人认证状态-认证通过
	public static final Long USER_STATUS_Y = 362l;
	//货币类型-人民币
	public static final String CURRENCY_TYPE_CNY = "CNY";
	//货币类型-美元
	public static final String CURRENCY_TYPE_USD = "USD";
	//业务code-正常
	public static final String SERVICE_CODE_OK = "0000";
	//业务code-异常
	public static final String SERVICE_CODE_ERR = "9999";
	//短信验证码类型-快捷绑卡
	public static final Long SMS_TYPE_BIND_CARD = 284l;
	//绑卡短信
	public static final String SMS_BIND_CARD = "验证码%s，您正在海投进行银行卡绑定操作，如非您本人操作，请致电400-867-7848";
	//短信验证码状态-发送成功
	public static final Long SMS_STATUS_OK = 290l;
	//短信验证码状态-发送失败
	public static final Long SMS_STATUS_ERR = 291l;

}
