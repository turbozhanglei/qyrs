package com.dxtf.controller.sys;

import com.dxtf.base.BizAction;
import com.dxtf.service.BizService;
import com.dxtf.util.BaseResult;
import com.dxtf.util.RedisService;
import com.dxtf.util.StatusConstant;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.web.util.WebUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜单控制器
 *
 * @author Louis
 * @date Oct 29, 2018
 */
@RestController
@RequestMapping("menus")
public class SysMenusController extends BizAction {


    private void findChildren(List<Dto> SysMenus, List<Dto> menus) {
        for (Dto SysMenu : SysMenus) {
            List<Dto> children = new ArrayList<>();
            for (Dto menu : menus) {
//                if (menuType == 1 && menu.getAsInteger("type") == 2) {
//                    // 如果是获取类型不需要按钮，且菜单类型是按钮的，直接过滤掉
//                    continue;
//                }
                if (SysMenu.getAsInteger("id") != null && SysMenu.getAsInteger("id").equals(menu.getAsInteger("pid"))) {
                    menu.put("parentName", SysMenu.getAsString("name"));
                    menu.put("level", SysMenu.getAsInteger("level") + 1);
                    if (!exists(children, menu)) {
                        children.add(menu);
                    }
                }
            }
            SysMenu.put("children", children);
            children.sort((o1, o2) -> o1.getAsInteger("order_num").compareTo(o2.getAsInteger("order_num")));
            findChildren(children, menus);
        }
    }

    @RequestMapping(value = "/findMenuTree")
    public BaseResult findMenuTree(HttpServletRequest request) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);

            if (null == member) {
                result.setCode(StatusConstant.CODE_4000);
                result.setMsg("请登录");
                return result;
            }

//            根据用户id查询菜单id
            String userid = member.getAsString("id");
            dto.put("id", userid);


            BaseDto baseDto = new BaseDto("userid", userid);
            baseDto.put("roleid", 1);
            List<Dto> roleList = bizService.queryForList("sysRoleUser.queryList", baseDto);

            List<Dto> menus = new ArrayList<>();
            if (roleList.isEmpty()) {
                menus = bizService.queryForList("sysMenu.queryMenuListByUserid", dto);
            } else {
                menus = bizService.queryForList("sysMenu.queryList", new BaseDto());
            }

            List<Dto> resultList = new ArrayList<>();
            for (Dto menu : menus) {
                if (menu.getAsInteger("pid") == null || menu.getAsInteger("pid") == 0) {
                    menu.put("level", 0);
                    if (!exists(resultList, menu) && menu.getAsString("is_delete").equals("N")) {
                        resultList.add(menu);
                    }
                }
            }
            findChildren(resultList, menus);
            resultList.sort((o1, o2) -> o1.getAsInteger("order_num").compareTo(o2.getAsInteger("order_num")));

            result.setData(resultList);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }


    /**
     * 判断当前权限是否添加过
     *
     * @param sysMenus
     * @param sysMenu
     * @return
     */
    private boolean exists(List<Dto> sysMenus, Dto sysMenu) {
        boolean exist = false;
        for (Dto menu : sysMenus) {
            if (menu.getAsString("id").equals(sysMenu.getAsString("id"))) {
                exist = true;
            }
        }
        return exist;
    }
}
