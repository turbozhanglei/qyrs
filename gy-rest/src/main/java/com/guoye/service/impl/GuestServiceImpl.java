package com.guoye.service.impl;

import com.guoye.service.BizService;
import com.guoye.service.GuestService;
import com.guoye.util.BaseResult;
import com.guoye.util.OrderNumberUtils;
import com.guoye.util.RedisService;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.resource.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service("guestServiceImpl")
public class GuestServiceImpl implements GuestService {


    @Autowired
    public BizService bizService;

    @Autowired
    public RedisService redisService;

    @Autowired
    private OrderNumberUtils orderNumberUtils;

    //传入"1/2"、"1/3"类型字符串得出结果
    @Override
    public double getWarningdayNum(String warningday) {
        String[] split = warningday.split("/");
        double one = Integer.parseInt(split[0]);
        double two = Integer.parseInt(split[1]);
        double f = one / two;
        BigDecimal b = new BigDecimal(f);
        return b.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    //审核
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public BaseResult passAudit(Dto dto) {

        Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);
        BaseResult result = new BaseResult();
        //获取自定审核值
        String status = dto.getAsString("status");
        //获取ids
        String ids = dto.getAsString("ids");
        try {
            //判断前台所传审核值
            //审核通过
            if ("2".equals(status)) {
                //获取pid
                String deptid = member.getAsString("deptid");
                Integer sort = null;
                String pid = "";
                if (StringUtils.isNotEmpty(deptid)) {
                    Dto parent = (Dto) bizService.queryForDto("sysRole.getFirstParent", new BaseDto("dept_id", deptid));
                    pid = parent.getAsString("deptid");
                    Dto id3 = (Dto) bizService.queryForDto("sysDept.sortCount", new BaseDto("pid", pid));
                    String sortnumber = id3.getAsString("sortnumber");
                    sort = Integer.parseInt(sortnumber);
                }
                for (String id : ids.split(",")) {
                    //当前顾客信息
                    Dto wcustomer = (Dto) bizService.queryForDto("wcustomer.getInfo", new BaseDto("id", id));
                    //顾客当前审核状态
                    String wcustomer_status = wcustomer.getAsString("status");
                    //顾客为待审核状态
                    if ("0".equals(wcustomer_status)) {
                        //顾客表修改审核状态
                        wcustomer.put("tableName", "wcustomer");
                        wcustomer.put("status", status);
                        bizService.updateInfo(wcustomer);
                        Dto id2 = (Dto) bizService.queryForDto("customerRelation.getInfo", new BaseDto("customer_id", id));
                        if (id2 == null) {
                            //部门表添加
                            Dto tb_dept = new BaseDto();
                            if (sort != null) {
                                sort++;
                                tb_dept.put("tableName", "sysDept");
                                tb_dept.put("dept_name", wcustomer.getAsString("client_name"));
                                tb_dept.put("pid", pid);
                                tb_dept.put("status", 0);
                                tb_dept.put("type", 21);
                                tb_dept.put("sort", sort);
                                tb_dept.put("creator", member.getAsString("id"));
                                bizService.saveInfo(tb_dept);
                                //关联表添加
                                Dto tb_wcustomer_relation = new BaseDto();
                                tb_wcustomer_relation.put("tableName", "customerRelation");
                                tb_wcustomer_relation.put("customer_id", wcustomer.getAsString("id"));
                                tb_wcustomer_relation.put("deptid", tb_dept.getAsString("id"));
                                bizService.saveInfo(tb_wcustomer_relation);

                                //更新用户表数据
                                Dto updateUser = new BaseDto();
                                updateUser.put("account",wcustomer.getAsString("manager_email"));
                                updateUser.put("deptid",tb_dept.getAsString("id"));
                                updateUser.put("tableName","sysUser");
                                updateUser.put("method","updateDeptid");

                                bizService.update(updateUser);

                            }
                        }
                    }
                }
            }
            //拒绝审核
            if ("3".equals(status)) {
                for (String id : ids.split(",")) {
                    //当前顾客信息
                    Dto wcustomer = (Dto) bizService.queryForDto("wcustomer.getInfo", new BaseDto("id", id));
                    //顾客当前审核状态
                    String wcustomer_status = wcustomer.getAsString("status");
                    //顾客为待审核状态
                    if ("0".equals(wcustomer_status)) {
                        //顾客表修改审核状态
                        wcustomer.put("tableName", "wcustomer");
                        wcustomer.put("status", status);
                        bizService.updateInfo(wcustomer);
                    }
                }
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        }
        return result;
    }

    //客户注册
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void initCustomer(Dto inDto) {
        try {
            //第一次客户注册
            //插入数据到用户表
            String username = inDto.getAsString("manager_name");
            String status = "1";
            String account = inDto.getAsString("manager_email");
            String moile = inDto.getAsString("manager_mobile");
            String password = inDto.getAsString("password");

            Dto insertUser = new BaseDto();
            insertUser.put("username", username);
            insertUser.put("status", status);
            insertUser.put("account", account);
            insertUser.put("moile", moile);
            insertUser.put("password", password);
            insertUser.put("tableName", "sysUser");

            String customerno = orderNumberUtils.getCuntomerNo();

            inDto.put("customerno", customerno);

            bizService.saveInfo(inDto);
            bizService.saveInfo(insertUser);


            Dto insert = new BaseDto();
            insert.put("roleid", 2);
            insert.put("userid", insertUser.getAsString("id"));
            insert.put("tableName", "sysRoleUser");
            bizService.saveInfo(insert);


        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


}
