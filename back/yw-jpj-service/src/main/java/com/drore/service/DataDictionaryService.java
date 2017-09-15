package com.drore.service;

import com.drore.domain.DataDictionary;
import com.drore.util.PageUtil;
import com.drore.util.RestMessageModel;

import java.util.List;



public interface DataDictionaryService {
	
	public RestMessageModel deleteById(String id);
	/**
	 * 判断编码是否唯一 true 表示唯一，false表示已经存在
	 * 
	 * @param id
	 * @param code
	 * @return
	 */
	public boolean checkUniqueCode(String id, String code);
	/**
	 * 新增数据字典
	 * 
	 * @param dataDictionary
	 * @return
	 */
	public RestMessageModel save(DataDictionary dataDictionary);

	/**
	 * 更新数据字典 此处code是不允许修改的，否则要出错
	 * 
	 * @param dataDictionary
	 * @return
	 */
	public RestMessageModel update(DataDictionary dataDictionary);

	/**
	 * 根据id查询
	 * 
	 * @param id
	 * @return
	 */
	public DataDictionary findById(String id);
	/**
	 * 根据父类，是否启用查询
	 * 
	 * @param parentId
	 * @param enable
	 * @return
	 */
	public List<DataDictionary> findListByParentEnable(String parentId,
			String enable);
	
	/**
	 * 根据编码查询
	 * @param code
	 * @param enable
	 * @return
	 */
	public List<DataDictionary> findListByCode(String code, String enable);

	/**
	 * 根据编码模糊查询
	 * @param parentCode
	 * @param enable
	 * @param child
     * @return
     */
	public List<DataDictionary> findListByFuzzy(String parentCode, String enable, DataDictionary child);

	/**
	 * 分页查询
	 * @param pagerUtil
	 * @param dataDictionary
     * @return
     */
	public PageUtil findListByPage(PageUtil pagerUtil, DataDictionary dataDictionary);
	
	/**
	 * 查询所有子节点
	 * @param parentId
	 * @param enable
	 * @return
	 */
	public List<DataDictionary> queryByParentId(String parentId, String enable);

	/**
	 * 条件查询所有子节点
	 * @param parentId
	 * @param child
     * @return
     */
	public List<DataDictionary> queryByChildNameFuzzy(String parentId, DataDictionary child);
}
