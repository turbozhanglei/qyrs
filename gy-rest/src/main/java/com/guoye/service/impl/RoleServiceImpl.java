package com.guoye.service.impl;

import com.guoye.service.RoleService;
import com.guoye.util.BaseResult;
import com.guoye.service.BizService;

import com.guoye.util.JSONUtil;
import com.guoye.util.RedisService;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service("roleServiceImpl")
public class RoleServiceImpl implements RoleService {

    @Autowired
    public RedisService redisService;

    @Autowired
    public BizService bizService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public BaseResult deleteRoleByRoleId(Dto dto, Long memberId) throws Exception {
        BaseResult result = new BaseResult();
        try {
            if (dto.getAsString("ids") != null) {
                String id[] = dto.getAsString("ids").split(",");
                BaseDto rDto = new BaseDto();
                BaseDto rmDto = new BaseDto();
                rDto.put("tableName", "sysRole");
                rDto.put("updator", memberId);
                rDto.put("method", "deleteInfo");
                rmDto.put("tableName", "sysRole");
                rmDto.put("method", "deleteInfoByRoleId");
                for (int i = 0; i < id.length; i++) {
                    rDto.put("id", id[i]);
                    rmDto.put("roleid", id[i]);

                    Dto delete = new BaseDto();
                    delete.put("method", "deleteSysRoleDept");
                    delete.put("tableName", "sysRole");
                    delete.put("roleid", id[i]);

                    bizService.delete(delete);
                    bizService.update(rDto);
                    bizService.update(rmDto);
                }
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public BaseResult saveRoleMenus(Dto dto) throws Exception {
        BaseResult result = new BaseResult();
        try {
            List<Map<String, Object>> roleMenus1 = JSONUtil.parseJSON2List(dto.getAsString("roleMenus"));
            if (dto.getAsLong("roleId") != null) {
                Dto rDto = new BaseDto();
                Dto mDto = new BaseDto();
                rDto.put("tableName", "sysRole");
                rDto.put("roleid", dto.getAsLong("roleId"));
                rDto.put("method", "deleteByRoleId");
                mDto.put("tableName", "sysRole");
                mDto.put("method", "saveRoleMenu");
                bizService.delete(rDto);//先删除
                for (Map<String, Object> roleMenus : roleMenus1) {
                    for (String role : roleMenus.keySet()) {
                        mDto.put("roleid", roleMenus.get("roleId"));
                        mDto.put("menuid", roleMenus.get("menuId"));

                    }
                    bizService.update(mDto);

                }
                redisService.delete("INIT_ROLE_WMS_SALE_" + dto.getAsLong("roleId"));
            }
            result.setMsg("操作成功");
            result.setCode("0000");

        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        }
        return result;
    }

}
