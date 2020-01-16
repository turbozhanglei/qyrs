package com.guoye.config;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

//@Component
//@Slf4j
public class AccessFilter extends ZuulFilter {

    @Value("${white_list}")
    private String white_list;

    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    public int filterOrder() {
        return 0;
    }

    public boolean shouldFilter() {
        return true;
    }

    public Object run() throws ZuulException {
        //获取请求的上下文类 注意是：com.netflix.zuul.context包下的
        RequestContext ctx = RequestContext.getCurrentContext();
        //获取request对象
        HttpServletRequest request = ctx.getRequest();
        //避免中文乱码
        ctx.addZuulResponseHeader("Content-type", "text/json;charset=UTF-8");
        ctx.getResponse().setCharacterEncoding("UTF-8");
        String enctype = request.getContentType();
        //log.info("--------enctype--------" + enctype);
        ctx.getResponse().setHeader("Access-Control-Allow-Origin", "*");
        ctx.getResponse().setHeader("Access-Control-Allow-Origin", "*");
        ctx.getResponse().setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        ctx.getResponse().setHeader("Access-Control-Max-Age", "3600");
        ctx.getResponse().setHeader("Access-Control-Allow-Headers", "x-requested-with");
        //打印日志
        //log.info("请求方式：{},地址：{}" + request.getMethod() + request.getRequestURI());
        String token = request.getParameter("token");
        /*if(white_list.indexOf(request.getRequestURI())==-1 ||request.getMethod().equals("get")){
            if(StringUtils.isBlank(token)) {
                BaseResult result=new BaseResult();
                result.setCode("9999");
                result.setMsg("非法访问");
                //使其不进行转发 自定义route类型时，在shouldFilter中也需要进行此参数判断。
                ctx.setSendZuulResponse(false);
                ctx.setResponseBody(JSON.toJSONString(result));
                ctx.setResponseStatusCode(HttpStatus.OK.value());//401
                //或者添加一个额外参数也可以 传递参数可以使用
                //ctx.set("checkAuth",false);
            }
        }*/
        return null;
    }
}