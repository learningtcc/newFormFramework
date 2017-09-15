package com.drore.service.sys.impl;

import com.drore.cloud.sdk.domain.util.RequestExample;
import com.drore.cloud.sdk.domain.util.RequestExample.Criteria;
import com.drore.cloud.sdk.domain.util.RequestExample.Param;
import com.drore.domain.sys.SysRoleAuthority;
import com.drore.enums.CommonEnum;
import com.drore.service.impl.BaseServiceImpl;
import com.drore.service.sys.SysRoleAuthorityService;
import com.drore.util.LoginSysUserUtil;
import com.drore.util.PageUtil;
import com.drore.util.RestMessageModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 浙江卓锐科技股份有限公司 版权所有 © Copyright 2016<br/>
 * 说明: 角色权限<br/>
 * 项目名称: guided-assistant-service <br/>
 * 创建日期: 2016年8月19日 下午7:40:24 <br/>
 * 作者: wdz
 */
@Service
public class SysRoleAuthorityServiceImpl extends BaseServiceImpl  implements SysRoleAuthorityService {
	/**
	 * 执行用户角色权限保存操作,保存之前先判断是否存在，存在删除
	 * 
	 * @param list
	 * @return
	 */
	public RestMessageModel saveBatch(List<SysRoleAuthority> list,
									  String roleId) {
		RequestExample example = new RequestExample(100000,1);
		Criteria cri = example.create();
		Param param = example.createParam();
		param.addTerm("is_deleted", CommonEnum.YesOrNo.NO.getCode());
		param.addTerm("role_id", roleId);
		cri.getMust().add(param);
		List<SysRoleAuthority> exits = this.queryList(
				SysRoleAuthority.table, example,
				SysRoleAuthority.class, new String[] { "id" });
		if (exits != null && exits.size() > 0) {
			String[] pkIds = new String[exits.size()];
			for (int i=0;i<exits.size();i++){
				pkIds[i]=exits.get(i).getId();
			}
			
			 RestMessageModel model = this.deleteByIds(pkIds,
					SysRoleAuthority.table);
			if (!model.isSuccess()) {
				model.setErrorMessage("删除角色权限关联信息错误");
				return model;
			}
		}
		for(SysRoleAuthority sysRoleAuthority :list){
			sysRoleAuthority.setModifier(LoginSysUserUtil.getUserId());
		}
		return this.saveObjectBatch(SysRoleAuthority.table, list);

	}
	/**
	 * 根据角色主键查找
	 * @param roleId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SysRoleAuthority> queryByRoleId(String roleId) {

		StringBuffer sb = new StringBuffer();
		sb.append("  select ra.authority_id  from  "  + SysRoleAuthority.table + " ra ");
		sb.append(" where  ra.is_deleted = 'N' AND ra.role_id = '" + roleId + "' ");
		PageUtil pager = new PageUtil();
		pager.setPageNo(1);
		pager.setPageSize(100000);
		pager= this.querySql(sb.toString(),
				pager, SysRoleAuthority.class);

		if (pager.isSuccess()) {
			return (List<SysRoleAuthority>) pager.getRoot();
		}
		return new ArrayList<SysRoleAuthority>();
	}
	
}
