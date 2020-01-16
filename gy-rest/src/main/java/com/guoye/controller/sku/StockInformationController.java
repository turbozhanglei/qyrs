package com.guoye.controller.sku;

import com.alibaba.fastjson.JSONObject;
import com.guoye.base.BizAction;
import com.guoye.service.InStockService;
import com.guoye.util.BaseResult;
import com.guoye.util.CommonUtil;
import com.guoye.util.DateUtil;
import com.guoye.util.MailHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.EmailException;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/stockinfo")
public class StockInformationController extends BizAction {

    @Autowired
    InStockService inStockService;


    /**
     * 发送预警邮件
     *
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/sendMessage")
    public BaseResult sendMessage(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
            if (member == null) {
                result.setCode("4000");
                result.setMsg("会话失效,请重新登陆");
                return result;
            }

            String memberId = member.getAsString("id");

            String inBatchNumberList = dto.getAsString("in_batch_number_list");
            if (StringUtils.isNotEmpty(inBatchNumberList)) {

                List<String> list = Arrays.asList(inBatchNumberList.split(","));

                Iterator<String> iterator = list.iterator();

                Integer sendEmailLimit = CommonUtil.getSysConfig().getAsInteger("SEND_EMAIL_LIMIT");

                int times = list.size() > 10 ? 10 : list.size();

                ExecutorService singlePool = Executors.newFixedThreadPool(times);

                while (iterator.hasNext()) {

                    String next = iterator.next();
                    if (StringUtils.isEmpty(next)) {
                        continue;
                    }
                    Dto query = new BaseDto();
                    query.put("in_batch_number", next);
                    List<Dto> skuList = bizService.queryList("norderStoreSku.queryList", query);

                    System.out.println("开始发送邮件：" + skuList);

                    if (skuList.isEmpty()) {
                        continue;
                    }

                    Dto skuInfo = skuList.get(0);

                    skuInfo.put("creator", memberId);


                    int sendEmail = skuInfo.getAsInteger("sendemail");
                    if (sendEmail >= sendEmailLimit) {
                        //已到达系统上线，不在发送
                    } else {
                        //发送邮件
                        String inBatchumber = skuInfo.getAsString("in_batch_number");
                        try {

                            String value = redisService.getValue("SEND_MESSAGE_" + inBatchumber);

                            if (StringUtils.isNotEmpty(value)) {
                                //已经在处理
                                continue;
                            }

                            //设置处理标识
                            redisService.setValue("SEND_MESSAGE_" + inBatchumber, inBatchumber, 60l);

                            if (skuInfo.getAsInteger("is_expired_days") < 0) {

                                final Dto emailSendInfo = skuInfo;


                                singlePool.execute(new Runnable() {
                                    public void run() {
                                        //发送过期邮件
                                        try {
                                            sendErrorEmail(emailSendInfo);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                            } else if (skuInfo.getAsInteger("warning_days") < 0) {
                                final Dto emailSendInfo = skuInfo;

                                singlePool.execute(new Runnable() {
                                    public void run() {
                                        //发送预警邮件
                                        try {
                                            sendWarnEmail(emailSendInfo);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                            } else {
                                //正常处理
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            redisService.delete("SEND_MESSAGE_" + inBatchumber);
                        }
                    }

                }

                singlePool.shutdown();

            }


        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    /**
     * 删除库存
     *
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deleteSkuStore")
    public BaseResult deleteSkuStore(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
            if (member == null) {
                result.setCode("4000");
                result.setMsg("会话失效,请重新登陆");
                return result;
            }


            String memberId = member.getAsString("id");

            String inBatchNumberList = dto.getAsString("in_batch_number_list");

            if (StringUtils.isNotEmpty(inBatchNumberList)) {
                for (String inbatchNum : inBatchNumberList.split(",")) {

                    if (StringUtils.isNotEmpty(inbatchNum)) {
                        inStockService.deleteSkuStore(inbatchNum, memberId);
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    /**
     * 发送预警邮件
     *
     * @param dto
     */
    private void sendWarnEmail(Dto dto) throws Exception {

        String emailConfig = CommonUtil.getSysConfig().getAsString("EMAIL_CONFIG");
        if (StringUtils.isEmpty(emailConfig)) {
            return;
        }

        JSONObject email = JSONObject.parseObject(emailConfig);

        String host = email.getString("host");
        String username = email.getString("username");
        String password = email.getString("password");


        String warnTemplate = CommonUtil.getSysConfig().getAsString("WARN_TEMPLATE");
        if (StringUtils.isEmpty(warnTemplate)) {
            return;
        }

        //内容
        String content = String.format(warnTemplate, "red", dto.getAsString("sku_name"), dto.getAsString("warningday"), DateUtil.getStringFromDate(new Date(), "yyyy年MM月dd日"));
        String toEmailAddress = dto.getAsString("manager_email");

        String guoyeSendEmailGroup = CommonUtil.getSysConfig().getAsString("guoye_SEND_EMAIL_GROUP");
        if (StringUtils.isNotEmpty(guoyeSendEmailGroup)) {
            toEmailAddress = toEmailAddress + "," + guoyeSendEmailGroup;
        }


        Dto saveEmail = new BaseDto();
        saveEmail.put("tableName", "wmail");
        saveEmail.put("method", "saveInfo");

        saveEmail.put("in_batch_number", dto.getAsString("in_batch_number"));
        saveEmail.put("to_mail", toEmailAddress);
        saveEmail.put("in_mail", username);
        saveEmail.put("subject", "北京东晓腾飞供应链管理有限公司-产品预警");
        saveEmail.put("content", content);
        saveEmail.put("creator", dto.getAsString("creator"));


        try {
            MailHelper.sendHtmlEmail(host, username, password, "北京东晓腾飞供应链管理有限公司-产品预警", content, toEmailAddress, username);
            saveEmail.put("status", "0");
            saveEmail.put("result", null);
        } catch (EmailException e) {
            e.printStackTrace();
            saveEmail.put("status", "1");
            saveEmail.put("result", e.getMessage());
        }

        bizService.save(saveEmail);

    }

    /**
     * 发送预警邮件
     *
     * @param dto
     */
    private void sendErrorEmail(Dto dto) throws Exception {
        String emailConfig = CommonUtil.getSysConfig().getAsString("EMAIL_CONFIG");
        if (StringUtils.isEmpty(emailConfig)) {
            return;
        }

        JSONObject email = JSONObject.parseObject(emailConfig);

        String host = email.getString("host");
        String username = email.getString("username");
        String password = email.getString("password");


        String warnTemplate = CommonUtil.getSysConfig().getAsString("ERROE_TEMPLATE");
        if (StringUtils.isEmpty(warnTemplate)) {
            return;
        }

        //内容
        String content = String.format(warnTemplate, "blueviolet", dto.getAsString("sku_name"), DateUtil.getStringFromDate(new Date(), "yyyy年MM月dd日"));

        String toEmailAddress = dto.getAsString("manager_email");
        String guoyeSendEmailGroup = CommonUtil.getSysConfig().getAsString("guoye_SEND_EMAIL_GROUP");

        if (StringUtils.isNotEmpty(guoyeSendEmailGroup)) {
            toEmailAddress = toEmailAddress + "," + guoyeSendEmailGroup;
        }

        Dto saveEmail = new BaseDto();
        saveEmail.put("tableName", "wmail");
        saveEmail.put("method", "saveInfo");

        saveEmail.put("in_batch_number", dto.getAsString("in_batch_number"));
        saveEmail.put("to_mail", toEmailAddress);
        saveEmail.put("in_mail", username);
        saveEmail.put("subject", "北京东晓腾飞供应链管理有限公司-产品过期");
        saveEmail.put("content", content);
        saveEmail.put("creator", dto.getAsString("creator"));

        try {

            MailHelper.sendHtmlEmail(host, username, password, "北京东晓腾飞供应链管理有限公司-产品过期", content, toEmailAddress, username);
            saveEmail.put("status", "0");
            saveEmail.put("result", null);

        } catch (EmailException e) {
            e.printStackTrace();
            saveEmail.put("status", "1");
            saveEmail.put("result", e.getMessage());
        }
        bizService.save(saveEmail);

    }
}
