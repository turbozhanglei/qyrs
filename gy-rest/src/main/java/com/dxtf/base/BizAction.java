package com.dxtf.base;


import com.dxtf.service.BizService;
import com.dxtf.util.BaseResult;
import com.dxtf.util.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.g4studio.core.model.SpringBeanLoader;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * BizAction 商业Action组件基类
 *
 * @author Mcl
 * @see BizAction
 * @since 2018-01-03
 */
public class BizAction {


    @Autowired
    public RedisService redisService;

    @Autowired
    public BizService bizService;

    /**
     * 从服务容器中获取服务组件
     *
     * @param pBeanId
     * @return
     */
    protected Object getService(String pBeanId) {
        Object springBean = SpringBeanLoader.getSpringBean(pBeanId);
        return springBean;
    }

    /**
     * 交易成功提示信息
     *
     * @param pMsg 提示信息
     * @param pMsg
     * @return
     * @throws IOException
     */
    protected BaseResult reduceErr(String pMsg) {
        BaseResult result = new BaseResult();
        if (StringUtils.isEmpty(pMsg) || pMsg.length() > 20) {
            pMsg = "对不起，系统发生异常，我们会尽快处理。";
        }
        result.setCode("9999");
        result.setMsg(pMsg);
        return result;
    }

    public BaseResult returnMsg(String code, String pMsg) {
        BaseResult result = new BaseResult();
        if ("".equals(pMsg)) {
            pMsg = code;
        }
        result.setCode(code);
        result.setMsg(pMsg);
        return result;
    }
}
