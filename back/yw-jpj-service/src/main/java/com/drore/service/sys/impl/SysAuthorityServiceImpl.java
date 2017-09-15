package com.drore.service.sys.impl;

import com.alibaba.fastjson.JSONObject;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.domain.util.RequestExample;
import com.drore.cloud.sdk.domain.util.RequestExample.Criteria;
import com.drore.cloud.sdk.domain.util.RequestExample.Param;
import com.drore.constant.CommonConstant;
import com.drore.domain.sys.SysAuthority;
import com.drore.domain.sys.SysRoleAuthority;
import com.drore.domain.vo.SysAuthVo;
import com.drore.enums.CommonEnum;
import com.drore.service.impl.BaseServiceImpl;
import com.drore.service.sys.SysAuthorityService;
import com.drore.service.sys.SysRoleAuthorityService;
import com.drore.util.LoginSysUserUtil;
import com.drore.util.PageUtil;
import com.drore.util.RestMessageModel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 浙江卓锐科技股份有限公司 版权所有 © Copyright 2016<br/>
 * 说明:景区权限服务层 <br/>
 * 项目名称: guided-assistant-service <br/>
 * 创建日期: 2016年8月13日 下午4:22:37 <br/>
 * 作者: wdz
 */
@Service
public class SysAuthorityServiceImpl extends BaseServiceImpl implements SysAuthorityService {

	@Autowired
	SysRoleAuthorityService sysRoleAuthorityService;
	/**
	 * 如果唯一返回true，否则返回falseSysAuthority
	 * 
	 * @param code
	 * @param id
	 * @return
	 */
	public boolean uniqueCode(String code, String id) {
		RequestExample example = new RequestExample(10000,1);
		Param param = example.createParam();
		Criteria cri = example.create();
		param.addTerm("code", code);
		cri.getMust().add(param);
		if (StringUtils.isNotEmpty(id)) {// 如果不为空，那么不查询自己的
			Param notParam = example.createParam();
			notParam.addTerm("id", id);
			cri.getMustNot().add(notParam);
		}
		List<SysAuthority> list = this.queryList(SysAuthority.table,
				example, SysAuthority.class, "id");

		if (list != null && list.size() > 0)
			return false;
		else
			return true;

	}

	/**
	 * 查询权限列表， 此处根据isEnable查询 为空查询所有，不为空，查询对应状态的
	 * 
	 * @return
	 */
	public List<SysAuthority> queryList(String enable) {
		RequestExample example = new RequestExample(10000,1);
		Param param = example.createParam();
		if (!StringUtils.isEmpty(enable)) {
			param.addTerm("is_enable", enable);
			example.create().getMust().add(param);
		}
		List<SysAuthority> list = this.queryList(SysAuthority.table,
				example, SysAuthority.class);
		return list;
	}


	/**
	 * 根据id查询
	 * 
	 * @param id
	 * @return
	 */
	public SysAuthority findById(String id) {
		return this
				.queryOne(id, SysAuthority.table, SysAuthority.class);
	}

	/**
	 * 保存
	 * 
	 * @param sysAuthority
	 * @return
	 */
	public RestMessageModel save(SysAuthority sysAuthority) {
		RestMessageModel model = new RestMessageModel();
		if (!uniqueCode(sysAuthority.getCode(), null)) {
			model.setErrorMessage("编号不能使用");
			return model;
		}
		if (StringUtils.isEmpty(sysAuthority.getParentId()))
			sysAuthority.setParentId(CommonConstant.ROOT);
		sysAuthority.setCreator(LoginSysUserUtil.getUserId());
		return this.saveObject(sysAuthority,
				SysAuthority.table);
	}

	/**
	 * 更新
	 * 
	 * @param sysAuthority
	 * @return
	 */
	public RestMessageModel update(SysAuthority sysAuthority) {
		RestMessageModel model = new RestMessageModel();
		if (!uniqueCode(sysAuthority.getCode(), sysAuthority.getId())) {
			model.setErrorMessage("编号不能使用");
			return model;
		}
		SysAuthority old = findById(sysAuthority.getId());
		old.setName(sysAuthority.getName());
		old.setCode(sysAuthority.getCode());
		old.setIsEnable(sysAuthority.getIsEnable());
		old.setType(sysAuthority.getType());
		old.setSort(sysAuthority.getSort());
		old.setUrl(sysAuthority.getUrl());
		old.setModifier(LoginSysUserUtil.getUserId());
		return this.updateObject(sysAuthority.getId(),
				old, SysAuthority.table);
	}

	/**
	 * 查询所有子节点
	 * 
	 * @param parentId
	 * @param enable
	 * @return
	 */
	public List<SysAuthority> queryByParentId(String parentId, String enable) {
		if (StringUtils.isEmpty(parentId)) {
			parentId = CommonConstant.ROOT;
		}
		RequestExample example = new RequestExample(10000,1);
		Criteria cri = example.create();
		Param param = example.createParam();
		param.addTerm("parent_id", parentId);
		if (StringUtils.isNotEmpty(enable)) {
			param.addTerm("is_enable", enable);
		}
		param.addTerm("is_deleted",CommonEnum.YesOrNo.NO.getCode());

		cri.getMust().add(param);
		// 获取结果
		List<SysAuthority> queryList = queryList(SysAuthority.table,
				example, SysAuthority.class);
		return queryList;
	}

	/**
	 * 根据角色ID，查找角色拥有的权限
	 * 
	 * @param roleId
	 * @param enable
	 *            如果为空，查询全部，不为空，根据查询是否有效的
	 * @return
	 */
	public List<SysAuthority> queryByRoleId(String roleId, String enable) {
		//此处级联不支持
		
		 List<SysRoleAuthority> list = 	sysRoleAuthorityService.queryByRoleId(roleId);
		if(list.size()== 0)
			return new ArrayList<SysAuthority>();
		StringBuffer auths = new StringBuffer();
		for(SysRoleAuthority re:list){
			if(auths.length()>0)auths.append(",");
			auths.append("'"+re.getAuthorityId()+"'");
		}
		
		
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT a.* FROM " + SysAuthority.table
				+ " a where  a.is_deleted='N'  ");
		if (StringUtils.isNotEmpty(enable)) {
			sb.append(" and a.is_enable ='").append(enable).append("'");
		}
		sb.append(" and  a.id in ( "+auths.toString()+")");
		PageUtil pager = new PageUtil();
		pager.setPageNo(1);
		pager.setPageSize(100000);
		pager= querySql(sb.toString(), pager, SysAuthority.class);
		if (pager.isSuccess()) {
			return (List<SysAuthority>) pager.getRoot();
		}
		return new ArrayList<SysAuthority>();

	}

	/**
	 * 递归查询父节点下所有权限的id集合,并将状态设置为禁用
	 * 
	 * @param parentId
	 * @param list
	 * @return
	 */
	public List<JSONObject> queryAllChildren(String parentId,
                                             List<JSONObject> list) {

		// 获取结果
		List<SysAuthority> SysAuthoritys = queryByParentId(parentId,
				CommonEnum.YesOrNo.YES.getCode());
		if (SysAuthoritys == null || SysAuthoritys.size() == 0)
			return new ArrayList<JSONObject>();
		String id;
		if (null == list) {
			list = new ArrayList<JSONObject>();
		}
		JSONObject r;
		for (SysAuthority SysAuthority : SysAuthoritys) {
			id = SysAuthority.getId();
			r = new JSONObject();
			r.put("pkid", id);
			r.put("is_enable", CommonEnum.YesOrNo.NO.getCode());
			list.add(r);
			list.addAll(queryAllChildren(id, new ArrayList<JSONObject>()));
		}
		return list;
	}

	/**
	 * 根据父节点禁用所有子节点
	 * 
	 * @param parentId
	 * @return
	 */
	public boolean disableChildrenByParentId(String parentId) {
		List<JSONObject> queryAllChildren = queryAllChildren(parentId,
				new ArrayList<JSONObject>());
		if (CollectionUtils.isNotEmpty(queryAllChildren)) {
			for(JSONObject jsonObject:queryAllChildren){
				jsonObject.put("modifier", LoginSysUserUtil.getUserId());
			}
			RestMessage updateBatch = updateBatchObject(SysAuthority.table,
					queryAllChildren);
			return updateBatch.isSuccess();
		}
		return false;
	}

	/**
	 * 获取所有权限 根据状态
	 * 
	 * @param enable
	 * @return
	 */
	public List<SysAuthority> queryAll(String enable) {
		RequestExample example = new RequestExample(10000,1);
		Criteria cri = example.create();
		Param param = example.createParam();
		if (StringUtils.isNotEmpty(enable)) {
			param.addTerm("is_enable", enable);
		}
		param.addTerm("is_deleted", CommonEnum.YesOrNo.NO.getCode());
		cri.getMust().add(param);
		// 获取结果
		List<SysAuthority> queryList = queryList(SysAuthority.table,
				example, SysAuthority.class);
		return queryList;
	}
	/**
	 * 递归查询所有节点（包括禁用）
	 * 
	 * @param parentId
	 * @param datas
	 * @return
	 */
	public  List<SysAuthVo> queryAllChildren(String parentId,List<SysAuthVo> datas, String enable) {
		RequestExample example = new RequestExample(10000,1);
		Criteria cri = example.create();
		Param param = example.createParam();
		param.addTerm("parent_id", parentId);
		if (StringUtils.isNotEmpty(enable)) {
			param.addTerm("is_enable", enable);
		}

		cri.getMust().add(param);
		// 获取结果
		List<SysAuthority> queryList = this.queryList(
				SysAuthority.table, example, SysAuthority.class,
				new String[] {});

		if (queryList == null || queryList.size() == 0)
			return null;
		SysAuthVo temp;
		for (int i = 0; i < queryList.size(); i++) {
			temp = new SysAuthVo(queryList.get(i));
			temp.setChildren(queryAllChildren(temp.getId(),
					new ArrayList<SysAuthVo>(), enable));
			datas.add(temp);
		}
		return datas;
	}


	@Override
	public boolean checkIdExist(String id){
		return  checkIdExist(id, SysAuthority.table,
				SysAuthority.class);
	}

	@Override
	public  RestMessageModel deleteById(String id){
		return  deleteById(id,
				SysAuthority.table);
	}

	@Override
	public SysAuthority queryOne(String id) {
		return queryOne(
				id, SysAuthority.table,
				SysAuthority.class);
	}
}
