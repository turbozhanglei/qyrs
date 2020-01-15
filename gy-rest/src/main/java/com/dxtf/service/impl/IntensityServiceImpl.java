package com.dxtf.service.impl;

import com.dxtf.service.IntensityService;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @Auther: WuMengChang
 * @Date: 2018/6/2 12:20
 * @Description:
 */
@SuppressWarnings("all")
@Service("intensityServiceImpl")
public class IntensityServiceImpl implements IntensityService {

    //判断密码强度
    @Override
    public Integer judgePW(String PassWord) {
        Dto map = new BaseDto();
        for (int i = 0; i < PassWord.length(); i++) {
            int A = PassWord.charAt(i);
            if (A >= 48 && A <= 57) {// 数字
                map.put("数字", "数字");
            } else if (A >= 65 && A <= 90) {// 大写
                map.put("大写", "大写");
            } else if (A >= 97 && A <= 122) {// 小写
                map.put("小写", "小写");
            } else {
                map.put("特殊", "特殊");
            }
        }
        Set<String> sets = map.keySet();
        int pwdSize = sets.size();// 密码字符种类数
        int pwdLength = PassWord.length();// 密码长度
        if (pwdSize >= 3 && pwdLength >= 8) {
            return 1;// 强密码
        } else if (pwdSize >= 2 && pwdLength >= 6) {
            return 2;// 弱密码
        } else {
            return 3;// 弱密码
        }
    }
}
