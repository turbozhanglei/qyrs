package com.guoye.service;

import org.g4studio.core.metatype.Dto;

import java.sql.SQLException;
import java.util.List;

/**
 * 业务模型接口
 * 
 * @author Mcl
 * @since 2017-11-21
 */
public interface BizService {

	/**
	 * 删除信息
	 * 
	 * @param pDto
	 */
	Dto deleteInfo(Dto pDto);

	/**
	 * 删除信息
	 *
	 * @param pDto
	 */
	Dto delete(Dto pDto);

	/**
	 * 保存信息
	 * 
	 * @param pDto
	 */
	Dto saveInfo(Dto pDto);

	Dto saveInfoForApp(Dto pDto);

	/**
	 * 修改信息
	 * 
	 * @param pDto
	 */
	public Dto updateInfo(Dto pDto);

	public Dto updateInfoForApp(Dto pDto);

	Dto update(Dto pDto);

	/**
	 * 审核信息
	 * 
	 * @param pDto
	 */
	Dto updateStatus(Dto pDto);

	Dto save(Dto dto);

	void insertBatch(List<Dto> insertList);


	/**
	 * 查询一条记录
	 * @param parameterObject
	 *            查询条件对象(map javaBean)
	 */
	Object queryForObject(String statementName, Object parameterObject);

	Object queryForDto(String statementName, Object parameterObject);
	/**
	 * 查询一条记录
	 */
	Object queryForObject(String statementName);

	/**
	 * 查询记录集合
	 *
	 * @param parameterObject
	 *            查询条件对象(map javaBean)
	 */
	List queryForList(String statementName, Object parameterObject);
	List queryList(String statementName, Object parameterObject);


	/**
	 * 查询记录集合

	 */
	List queryForList(String statementName);

	/**
	 * 按分页查询
	 *
	 *            查询条件对象(map javaBean)
	 */
	List queryForPage(String statementName, Dto qDto)
			throws SQLException;



	List queryForPageCenter(String statementName, Dto qDto) throws SQLException;

}
