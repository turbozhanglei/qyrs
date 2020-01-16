package com.guoye.base;

import com.alibaba.fastjson.JSON;
import com.guoye.util.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.bouncycastle.asn1.x500.style.RFC4519Style;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.orm.xibatis.sqlmap.client.SqlMapExecutor;
import org.g4studio.core.orm.xibatis.support.SqlMapClientCallback;
import org.g4studio.core.util.G4Constants;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

import static org.bouncycastle.asn1.x500.style.RFC4519Style.o;

@Component
@Slf4j
public class BaseMapper {
    @Autowired
    SqlSessionTemplate sqlSessionTemplate;

    public List queryList(String statementName, Object parameterObject) {
        try {
            return JSONUtil.formatDateList(sqlSessionTemplate.selectList(statementName, parameterObject), G4Constants.FORMAT_DateTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List queryList(String statementName) {
        try {
            return JSONUtil.formatDateList(sqlSessionTemplate.selectList(statementName), G4Constants.FORMAT_DateTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object queryForObject(String statementName, Object parameterObject) {
        Object o = sqlSessionTemplate.selectOne(statementName, parameterObject);
        return JSONUtil.formatDateObject(o, G4Constants.FORMAT_DateTime);
    }

    public Object queryForDto(String statementName, Object parameterObject) {
        Object o = sqlSessionTemplate.selectOne(statementName, parameterObject);
        return JSONUtil.formatDateObject(RFC4519Style.o, G4Constants.FORMAT_DateTime);
    }

    public List queryForList(String statementName, Object parameterObject) {
        return JSONUtil.formatDateList(sqlSessionTemplate.selectList(statementName, parameterObject), G4Constants.FORMAT_DateTime);
    }

    public List queryForList(String statementName) {
        return JSONUtil.formatDateList(sqlSessionTemplate.selectList(statementName), G4Constants.FORMAT_DateTime);
    }

    public List queryForPage(String statementName, Object parameterObject) {
        return JSONUtil.formatDateList(sqlSessionTemplate.selectList(statementName, parameterObject), G4Constants.FORMAT_DateTime);
    }

    public int update(Dto pDto) {
        return sqlSessionTemplate.update(pDto.getAsString("tableName") + ".updateInfo", pDto);
    }

    public int updateInfo(String statementName, Object parameterObject) {
        return sqlSessionTemplate.update(statementName, parameterObject);
    }

    public int save(Dto pDto) {
        return sqlSessionTemplate.insert(pDto.getAsString("tableName") + ".saveInfo", pDto);
    }

    public int saveInfo(String statementName, Object parameterObject) {
        return sqlSessionTemplate.insert(statementName, parameterObject);
    }

    public int delete(String statementName, Object parameterObject) {
        return sqlSessionTemplate.delete(statementName, parameterObject);
    }

    public int delete(Dto pDto) {
        return sqlSessionTemplate.delete(pDto.getAsString("tableName") + "." + pDto.getAsString("method"), pDto);
    }

    public List queryForPageCenter(String statementName, Dto qDto) throws SQLException {
        Integer pageIndex = qDto.getAsInteger("start");
        Integer size = qDto.getAsInteger("limit");
        if (null != pageIndex) {
            Integer start = (pageIndex - 1) * size;
            qDto.put("start", start);
        } else {
            qDto.put("start", 0);
        }
        if (null != size) {
            qDto.put("end", size);
        } else {
            qDto.put("end", 999999);
        }
        List<Dto> list = sqlSessionTemplate.selectList(statementName, qDto);
        return list;
    }


    public void insertBatch(List<Dto> insertList) {

        // 如果自动提交设置为true,将无法控制提交的条数，改为最后统一提交，可能导致内存溢出
        SqlSession session = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        try {
            if (null != insertList || insertList.size() > 0) {
                int batch = 0;
                for (Dto insert : insertList) {
                    String tableName = insert.getAsString("tableName");
                    String method = insert.getAsString("method");
                    session.insert(tableName + "." + method, insert);

                    batch++;
                    //每500条批量提交一次。
                    if (batch == 500) {
                        session.commit();
                        // 清理缓存，防止溢出
                        session.clearCache();
                        batch = 0;
                    }

                }
                session.commit();
            }
        } catch (Exception e) {
            // 没有提交的数据可以回滚
            session.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}
