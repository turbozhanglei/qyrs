package com.guoye.service.impl;

import com.guoye.base.BaseMapper;
import com.guoye.service.BizService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * 业务模型实现基类<br>
 *
 * @author mcl
 * @since 2017年4月21日23:34:25
 */
@Service("bizServiceImpl")
public class BizServiceImpl implements BizService {


    private static Log log = LogFactory.getLog(BizServiceImpl.class);
    /**
     * 基类中给子类暴露的一个DAO接口<br>
     * 连接平台数据库
     */
    @Autowired
    protected BaseMapper g4Dao;

    @Override
    public Dto deleteInfo(Dto pDto) {
        Dto dto = new BaseDto();
        String[] arrChecked = pDto.getAsString("ids").split(",");
        for (int i = 0; i < arrChecked.length; i++) {
            dto.put("id", arrChecked[i]);
            g4Dao.delete(pDto.getAsString("tableName") + ".deleteInfo", dto);
        }
        return null;
    }

    @Override
    public Dto delete(Dto pDto) {
        Dto dto = new BaseDto();

        g4Dao.delete(pDto.getAsString("tableName") + "." + pDto.getAsString("method"), pDto);
        return null;
    }

    @Override
    public Dto saveInfo(Dto pDto) {
        g4Dao.saveInfo(pDto.getAsString("tableName") + ".saveInfo", pDto);
        return null;
    }

    @Override
    public Dto saveInfoForApp(Dto pDto) {
        g4Dao.saveInfo(pDto.getAsString("tableName") + ".saveInfoForApp", pDto);
        return null;
    }

    @Override
    public Dto updateInfo(Dto pDto) {
        pDto.put("updated_time", new Date());
        g4Dao.updateInfo(pDto.getAsString("tableName") + ".updateInfo", pDto);
        return null;
    }

    @Override
    public Dto updateInfoForApp(Dto pDto) {
        pDto.put("updated_time", new Date());
        g4Dao.updateInfo(pDto.getAsString("tableName") + ".updateInfoForApp", pDto);
        return null;
    }

    @Override
    public Dto update(Dto pDto) {
        g4Dao.updateInfo(pDto.getAsString("tableName") + "." + pDto.getAsString("method"), pDto);
        return null;
    }

    @Override
    public Dto updateStatus(Dto pDto) {
        Dto dto = new BaseDto();
        String[] arrChecked = pDto.getAsString("strChecked").split(",");
        for (int i = 0; i < arrChecked.length; i++) {
            dto.put("id", arrChecked[i]);
            dto.put("status", pDto.get("status"));
            g4Dao.updateInfo(pDto.getAsString("tableName") + ".updateStatus", dto);
        }
        return null;
    }

    @Override
    public Dto save(Dto dto) {
        g4Dao.saveInfo(dto.getAsString("tableName") + "." + dto.getAsString("method"), dto);
        return dto;
    }

    @Override
    public void insertBatch(List<Dto> insertList) {
        g4Dao.insertBatch(insertList);
    }


    /**
     * 查询一条记录
     *
     * @param
     * @param parameterObject 查询条件对象(map javaBean)
     */
    public Object queryForObject(String statementName, Object parameterObject) {
        return g4Dao.queryForObject(statementName, parameterObject);
    }

    /**
     * 查询一条记录
     *
     * @param
     * @param parameterObject 查询条件对象(map javaBean)
     */
    public Object queryForDto(String statementName, Object parameterObject) {
        Dto dto = (Dto) g4Dao.queryForObject(statementName, parameterObject);

        return dto;
    }

    /**
     * 查询一条记录
     *
     * @param
     */
    public Object queryForObject(String statementName) {
        return g4Dao.queryForObject(statementName, new BaseDto());
    }

    /**
     * 查询记录集合
     *
     * @param
     * @param parameterObject 查询条件对象(map javaBean)
     */
    public List queryForList(String statementName, Object parameterObject) {
        List<Dto> list = g4Dao.queryForList(statementName, parameterObject);
        return list;
    }

    public List queryList(String statementName, Object parameterObject) {
        List<Object> list = g4Dao.queryForList(statementName, parameterObject);
        return list;
    }


    /**
     * 按分页查询
     *
     * @param
     * @throws SQLException
     */
    public List queryForPage(String statementName, Dto qDto) throws SQLException {

        List<Dto> list = g4Dao.queryForPageCenter(statementName, qDto);
        return list;
    }

    /**
     * 查询记录集合
     *
     * @param
     */
    public List queryForList(String statementName) {
        return g4Dao.queryForList(statementName, new BaseDto());
    }


    @Override
    public List queryForPageCenter(String statementName, Dto qDto) throws SQLException {

        List<Dto> list = g4Dao.queryForPageCenter(statementName, qDto);
        return list;
    }


    public String amountFormat(String s) {
        if (s.indexOf(".") > 0) {

            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

}
