package com.drore.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.drore.cloud.sdk.builder.QueryBuilder;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.domain.Pagination;
import com.drore.cloud.sdk.domain.email.EmailModel;
import com.drore.cloud.sdk.domain.util.RequestExample;
import com.drore.cloud.sdk.init.RestBuilder;
import com.drore.cloud.sdk.init.RestBuilderFactory;
import com.drore.cloud.sdk.init.RestThirdBuilder;
import com.drore.exception.CustomException;
import com.drore.util.PageUtil;
import com.drore.util.RestMessageModel;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 浙江卓锐科技股份有限公司 版权所有 Copyright 2016<br/>
 * 说明: 基础服务层 除了部分系统底层用到，其它的不建议使用<<br/>
 * 项目名称: drore-base <br/>
 * 创建日期: 2016年11月2日 下午3:22:22 <br/>
 * 作者: xwb
 */
@Deprecated
public class BaseServiceImpl  {

	/**
	 * 直接转换成对象
	 * 
	 * @param id
	 * @param tableName
	 * @param classOfT
	 * @return
	 */
	public <T> T queryOne(String id, String tableName, Class<T> classOfT) {
		RestBuilder restBuilder = RestBuilderFactory.newBuilder();
		Map<String, Object> data = restBuilder.newQueryBuilder()
				.findOneByRName(tableName, id);
		if (data.isEmpty() || data.size() == 0) {
			return null;
		}
		JSONObject jsonObject = new JSONObject(data);
		JSON.parseObject(jsonObject.toJSONString());
		return JSONObject.toJavaObject(jsonObject, classOfT);
	}

	/**
	 * 条件查询第一条记录
	 * 
	 * @param tableName
	 * @param term
	 * @param classOfT
	 * @return
	 */
	public <T> T queryOne(String tableName, Map<String, Object> term,
			Class<T> classOfT) {
		RestBuilder restBuilder = RestBuilderFactory.newBuilder();
		Map<String, Object> data = restBuilder.newQueryBuilder()
				.findFirstByRName(tableName, term);
		if (data.isEmpty() || data.size() == 0) {
			return null;
		}
		JSONObject jsonObject = new JSONObject(data);
		return JSONObject.toJavaObject(jsonObject, classOfT);
	}

	/**
	 * 分页查询
	 * 
	 * @param resourceName
	 * @param term
	 * @param current_page
	 * @param page_size
	 * @param classOfT
	 * @return
	 */
	public <T> PageUtil queryPage(String resourceName,
								  Map<String, Object> term, int current_page, int page_size,
								  Class<T> classOfT) {

		RestBuilder rb = RestBuilderFactory.newBuilder();
		QueryBuilder builder = rb.newQueryBuilder();
		Pagination<Map<String, Object>> page = builder.findListByExample(
				resourceName, term, current_page, page_size, new String[] {});

		PageUtil pagerUtil = new PageUtil();
		pagerUtil.setPageNo(current_page);
		pagerUtil.setPageSize(page_size);

		if (page == null)
			return pagerUtil;

		if (page.getData() != null) {
			List<Map<String, Object>> list = (List<Map<String, Object>>) page
					.getData();
			if (list != null) {
				List<T> result = new ArrayList<T>();
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> map = list.get(i);
					JSONObject jsonObject = new JSONObject(map);
					result.add(JSONObject.toJavaObject(jsonObject, classOfT));
				}
				pagerUtil.setRoot(result);
			}

		}

		pagerUtil.setTotal(page.getTotal_page());

		return pagerUtil;
	}

	/**
	 * 按条件查询list
	 * @param resourceName
	 * @param term
	 * @param classOfT
	 * @param <T>
     * @return
     */
	public <T> List<T> queryList(String resourceName, Map term,
								 Class<T> classOfT) {

		RestBuilder rb = RestBuilderFactory.newBuilder();
		QueryBuilder builder = rb.newQueryBuilder();

		Pagination<Map<String, Object>> page = builder.findListByRName(resourceName,term,1,10000);
		List<Map<String, Object>> list = page.getData();
		if (list != null && list.size() > 0) {
			JSONObject jsonObject = null;
			T t = null;
			List<T> resList = new ArrayList<T>();
			for (Map<String, Object> map : list) {
				jsonObject = new JSONObject(map);
				t = JSONObject.toJavaObject(jsonObject, classOfT);
				resList.add(t);
			}
			return resList;
		} else
			return null;

	}
	/**
	 * 条件查询 对象 使用
	 * 
	 * @param <T>
	 * @param resourceName
	 * @param example
	 * @param classOfT
	 * @param displayFields
	 * @return
	 * @return
	 * @throws IOException
	 */
	public <T> List<T> queryList(String resourceName, RequestExample example,
			Class<T> classOfT, String... displayFields) {
		if (null == example) {
			example = new RequestExample(100000, 1);
		}
		if (MapUtils.isEmpty(example.getSort())) {
			HashMap<String, Object> sort = new HashMap<String, Object>();
			sort.put("sort", "ASC");
			example.setSort(sort);
		}
		RestBuilder rb = RestBuilderFactory.newBuilder();
		QueryBuilder builder = rb.newQueryBuilder();
		Pagination<Map<String, Object>> page = builder.findListByRName(
				resourceName, example, displayFields);
		List<Map<String, Object>> list = page.getData();
		if (list != null && list.size() > 0) {
			JSONObject jsonObject = null;
			T t = null;
			List<T> resList = new ArrayList<T>();
			for (Map<String, Object> map : list) {
				jsonObject = new JSONObject(map);
				t = JSONObject.toJavaObject(jsonObject, classOfT);
				resList.add(t);
			}
			return resList;
		} else
			return null;

	}

	/**
	 * 查询数量
	 * 
	 * @param resourceName
	 * @param example
	 * @param displayFields
	 * @param <T>
	 * @return
	 */
	public <T> int queryCount(String resourceName, RequestExample example,
			String... displayFields) {
		if (null == example) {
			example = new RequestExample(100000, 1);
		}
		if (MapUtils.isEmpty(example.getSort())) {
			HashMap<String, Object> sort = new HashMap<String, Object>();
			sort.put("sort", "ASC");
			example.setSort(sort);
		}
		RestBuilder rb = RestBuilderFactory.newBuilder();
		QueryBuilder builder = rb.newQueryBuilder();
		Pagination<Map<String, Object>> page = builder.findListByRName(
				resourceName, example, displayFields);
		return page.getCount();

	}

	/**
	 * SQL分页查询
	 * 
	 * @param sql
	 * @return 返回pager对象
	 */
	public <T> PageUtil querySql(String sql, PageUtil pageUtil, Class<T> classOfT) {
		RestBuilder rb = RestBuilderFactory.newBuilder();
		QueryBuilder builder = rb.newQueryBuilder();
		Pagination<Map<String, Object>> page = builder.execute(sql,
				pageUtil.getPageSize(), pageUtil.getPageNo());

		if (page == null) {
			return pageUtil;
		}
		if (null != page.getData()) {
			List<Map<String, Object>> list = page.getData();
			if (list != null) {
				List<T> result = new ArrayList<T>();
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> map = list.get(i);
					JSONObject jsonObject = new JSONObject(map);
					result.add(JSONObject.toJavaObject(jsonObject, classOfT));
				}
				pageUtil.setRoot(result);
				// pager.setRoot((List<T>) JSONUtil.beanToMap(result));
			}
		}
		pageUtil.setSuccess(page.getSuccess());
		pageUtil.setTotal(page.getTotal_page());
		pageUtil.setCount(page.getCount());

		return pageUtil;

	}

	/**
	 * 保存操作 使用
	 * 
	 * @param data
	 * @param tableName
	 * @return
	 */
	public RestMessageModel saveObject(Object data, String tableName) {
		RestBuilder restBuilder = RestBuilderFactory.newBuilder();
		RestMessage restMessage = restBuilder.newRecordBuilder().createByRName(
				tableName, JSONObject.toJSON(data));

		if (restMessage == null) {
			restMessage = new RestMessage();
			restMessage.setSuccess(false);
		}

		if (!restMessage.isSuccess()) {
			throw new CustomException("保存数据失败");
		}
		return new RestMessageModel(restMessage);
	}

	/**
	 * 批量新增方法 使用
	 * 
	 * @param resourceName
	 * @param list
	 * @return
	 */
	public RestMessageModel saveObjectBatch(String resourceName, List<?> list) {

		RestBuilder restBuilder = RestBuilderFactory.newBuilder();

		RestMessage restMessage = restBuilder.newRecordBuilder()
				.createBatchByRName(resourceName, JSONObject.toJSON(list));

		if (restMessage == null) {
			restMessage = new RestMessage();
			restMessage.setSuccess(false);
		}
		if (!restMessage.isSuccess())
			throw new CustomException("保存数据失败");
		return new RestMessageModel(restMessage);
	}

	/**
	 * 批量删除 使用
	 * 
	 * @param pkIds
	 * @param tableName
	 * @return
	 * @throws IOException
	 */
	public RestMessageModel deleteByIds(String[] pkIds, String tableName) {
		RestBuilder restBuilder = RestBuilderFactory.newBuilder();
		RestMessage restMessage = restBuilder.newRecordBuilder().deleteByRName(
				tableName, pkIds);

		if (restMessage == null) {
			restMessage = new RestMessage();
			restMessage.setSuccess(false);
		}

		if (!restMessage.isSuccess())
			throw new CustomException("删除数据失败");
		return new RestMessageModel(restMessage);
	}

	/**
	 * 单个删除 使用
	 * 
	 * @param id
	 * @param tableName
	 * @return
	 */
	public RestMessageModel deleteById(String id, String tableName) {
		RestBuilder restBuilder = RestBuilderFactory.newBuilder();
		RestMessage restMessage = restBuilder.newRecordBuilder().deleteByRName(
				tableName, id);

		if (restMessage == null) {
			restMessage = new RestMessage();
			restMessage.setSuccess(false);
		}

		if (!restMessage.isSuccess())
			throw new CustomException("删除数据失败");
		return new RestMessageModel(restMessage);
	}

	/**
	 * 根据条件删除
	 * 
	 * @param resourceName
	 * @param params
	 * @return
	 */
	public RestMessageModel deleteByCriterion(String resourceName,
			Map<String, Object> params) {
		RestBuilder restBuilder = RestBuilderFactory.newBuilder();
		RestMessage restMessage = restBuilder.newRecordBuilder()
				.deleteByCriterion(resourceName, params);
		if (restMessage == null) {
			restMessage = new RestMessage();
			restMessage.setSuccess(false);
		}

		if (!restMessage.isSuccess())
			throw new CustomException("删除数据失败.");
		return new RestMessageModel(restMessage);
	}

	/**
	 * 修改数据 使用
	 * 
	 * @param id
	 * @param data
	 * @param tableName
	 * @return
	 */
	public RestMessageModel updateObject(String id, Object data,
			String tableName) {
		RestBuilder restBuilder = RestBuilderFactory.newBuilder();
		RestMessage restMessage = restBuilder.newRecordBuilder().updateByRName(
				tableName, id, JSONObject.toJSON(data));
		if (restMessage == null) {
			restMessage = new RestMessage();
			restMessage.setSuccess(false);
		}

		if (!restMessage.isSuccess()) {
			throw new CustomException("更新数据失败.");
		}
		return new RestMessageModel(restMessage);

	}

	/**
	 * 批量更新 使用
	 */
	public RestMessageModel updateBatchObject(String tableName, List<?> list) {

		RestBuilder restBuilder = RestBuilderFactory.newBuilder();
		RestMessage restMessage = restBuilder.newRecordBuilder()
				.updateBatchByRName(tableName, JSONObject.toJSON(list));

		if (restMessage == null) {
			restMessage = new RestMessage();
			restMessage.setSuccess(false);
		}

		if (!restMessage.isSuccess()) {
			throw new CustomException("更新数据失败.");
		}
		return new RestMessageModel(restMessage);
	}

	/**
	 * 发送邮件
	 * 
	 * @param emailModel
	 * @return
	 */
	public RestMessageModel sendEmail(EmailModel emailModel) {
		RestThirdBuilder restBuilder = RestBuilderFactory.newThirdBuilder();
		RestMessage restMessage = restBuilder.newMailBuilder().sendTextEmail(
				emailModel);
		if (restMessage == null) {
			restMessage = new RestMessage();
			restMessage.setSuccess(false);
		}
		if (!restMessage.isSuccess()) {
			throw new CustomException("发送邮件失败.");
		}
		return new RestMessageModel(restMessage);

	}

	/**
	 * 检查是否有某条数据
	 * 
	 * @param id
	 * @param tableName
	 * @param classOfT
	 * @return true 有；false 没有
	 */
	public boolean checkIdExist(String id, String tableName, Class<?> classOfT) {
		Object queryOne = this.queryOne(id, tableName, classOfT);

		if (null != queryOne) {
			return true;
		}
		return false;
	}

	public boolean checkIdsExist(String ids, String tableName,
			Class<?> classOfT, String isEnable) {
		if (StringUtils.isEmpty(ids)) {
			return false;
		}
		String ids_ = ids.replace(",", "','");
		String sql = "select * from " + tableName + " where id in ('" + ids_
				+ "') ";
		if (StringUtils.isNotEmpty(isEnable)) {
			sql += "is_enable = '" + isEnable + "'";
		}
		PageUtil pager = new PageUtil();
		pager.setPageSize(1);
		PageUtil result = this.querySql(sql, pager, classOfT);
		String[] split = ids.split(",");

		if (null != result.getTotal() && split.length == result.getTotal()) {
			return true;
		}
		return false;
	}
}
