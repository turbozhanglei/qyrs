package com.guoye;

import com.guoye.service.BizService;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.resource.util.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ZgtServiceApplicationTests {

    @Autowired
    BizService bizService;
    @Test
    public void contextLoads() {

        try {
            List<Dto> depts = bizService.queryForList("sysDept.queryDeptList", new BaseDto("dept_name","北京丁丁"));
            Set<String> parentList = new HashSet<>();
            depts.forEach(element -> {
                String parent = element.getAsString("parentList");
                if (StringUtils.isNotEmpty(parent)) {
                    parentList.addAll(Arrays.asList(parent.split(",")));
                }
            });
            System.out.println(parentList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
