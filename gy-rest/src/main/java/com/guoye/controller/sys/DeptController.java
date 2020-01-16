package com.guoye.controller.sys;

import com.guoye.base.BizAction;
import com.guoye.util.BaseResult;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.web.util.WebUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 后台用户业务处理
 *
 * @author mcl
 * @see DeptController
 * @since 2017年6月20日15:49:06
 */
@RestController
@RequestMapping("/dept")
public class DeptController extends BizAction {


    /**
     * 获取所有部门树状数据结构
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryAllDepts")
    public BaseResult queryAllDepts(HttpServletRequest request) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            // 查询根目录下所有部门
            dto.put("pid", 0);
            List<Dto> rdepts = (List<Dto>) bizService.queryForList("sysDept.queryList", dto);
            for (Dto rdept : rdepts) {
                dto.put("pid", rdept.get("id"));
                List<Dto> nodes = (List<Dto>) bizService.queryForList("sysDept.queryList", dto);
                for (Dto node : nodes) {
                    dto.put("pid", node.get("id"));
                    List<Dto> dnodes = (List<Dto>) bizService.queryForList("sysDept.queryList", dto);
                    node.put("nodes", dnodes);
                }
                rdept.put("nodes", nodes);
            }
            result.setData(rdepts);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 删除角色信息
     *
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deleteDept")
    public BaseResult delectRole(HttpServletRequest request, HttpServletResponse response) {
        Dto dto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        try {
            Dto dept = (BaseDto) bizService.queryForObject("sysDept.judgeDept", dto);
            if (dept == null) {
                dto.put("tableName", "sysDept");
                dto.put("ids", dto.getAsString("id"));
                bizService.deleteInfo(dto);
                result.setMsg("数据操作成功");
            } else {
                throw new Exception("对不起，此部门有子部门或者有关联用户，不能删除。");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 部门新增编辑
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/editDeptInfo")
    public BaseResult editDeptInfo(HttpServletRequest request, HttpServletResponse response) {
        Dto inDto = WebUtils.getParamAsDto(request);
        BaseResult result = new BaseResult();
        inDto.put("tableName", inDto.getAsString("t"));
        try {
            Dto member = redisService.getObject(inDto.getAsString("token"), BaseDto.class);
//			inDto.put("creator", member==null?"":member.get("id"));
            if (inDto.getAsLong("id") != null) {//修改
                Dto dept = (BaseDto) bizService.queryForDto("sysDept.getInfo", new BaseDto("id", inDto.getAsString("id")));
                if (dept.getAsString("id").equals(inDto.getAsString("pid"))) {
                    throw new Exception("修改失败,上级部门不能选当前修改部门！");
                }
                List<Dto> depts = bizService.queryForList("sysDept.queryList", new BaseDto("pid", inDto.getAsString("id")));
                for (Dto dto : depts) {
                    if (dto.getAsString("id").equals(inDto.getAsString("pid"))) {
                        throw new Exception("修改失败,上级部门不能选自己的下属部门！");
                    }
                }
                bizService.updateInfo(inDto);
            } else {//新增
                Dto dept = (BaseDto) bizService.queryForDto("sysDept.getInfo", new BaseDto("dept_name", inDto.getAsString("dept_name")));
                if (dept != null) {
                    throw new Exception("部门名称不可重复！");
                }
//				inDto.put("updator", member==null?"":member.get("id"));
                bizService.saveInfo(inDto);
            }
            result.setData(inDto);
        } catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }
}

