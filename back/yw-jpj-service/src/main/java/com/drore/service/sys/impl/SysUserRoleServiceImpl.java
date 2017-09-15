package com.drore.service.sys.impl;

import com.drore.cloud.sdk.domain.util.RequestExample;
import com.drore.cloud.sdk.domain.util.RequestExample.Criteria;
import com.drore.cloud.sdk.domain.util.RequestExample.Param;
import com.drore.domain.sys.SysUserRole;
import com.drore.enums.CommonEnum;
import com.drore.service.impl.BaseServiceImpl;
import com.drore.service.sys.SysUserRoleService;
import com.drore.util.LoginSysUserUtil;
import com.drore.util.PageUtil;
import com.drore.util.RestMessageModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 浙江卓锐科技股份有限公司 版权所有 © Copyright 2016<br/>
 * 说明: 用户角色服务层接口<br/>
 * 项目名称: guided-assistant-service <br/>
 * 创建日期: 2016年8月19日 下午7:58:53 <br/>
 * 作者: wdz
 */
@Service
public class SysUserRoleServiceImpl extends BaseServiceImpl implements SysUserRoleService {
	/**
	 * 执行用户角色权限保存操作,保存之前先判断是否存在，存在删除
	 * @param list
	 * @param userId
	 * @return
	 */
	public RestMessageModel saveBatch(List<SysUserRole> list,
									  String userId) {
		RequestExample example = new RequestExample(100000,1);
		Criteria cri = example.create();
		Param param = example.createParam();
		param.addTerm("is_deleted", CommonEnum.YesOrNo.NO.getCode());
		param.addTerm("user_id", userId);
		cri.getMust().add(param);
		List<SysUserRole> exits = this.queryList(
				SysUserRole.table, example,
				SysUserRole.class, new String[] { "id" });
		if (exits != null && exits.size() > 0) {
			List<String> ids = new ArrayList<String>();
			for (SysUserRole temp : exits)
				ids.add(temp.getId());

			String[] strArr = new String[ids.size()];
			strArr = ids.toArray(strArr);
			RestMessageModel model = this.deleteByIds(strArr,
					SysUserRole.table);
			if (!model.isSuccess()) {
				model.setErrorMessage("删除用户角色关联信息错误");
				return model;
			}
		}
		for(SysUserRole sysUserRole:list ){
			sysUserRole.setModifier(LoginSysUserUtil.getUserId());
		}
		return this.saveObjectBatch(SysUserRole.table, list);

	}
	
	/**
	 * 根据用户主键获取
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SysUserRole> queryByUserId(String userId) {

		StringBuffer sb = new StringBuffer();
		sb.append("  select ra.role_id  from  "  + SysUserRole.table + " ra ");
		sb.append(" where  ra.is_deleted = 'N' AND ra.user_id = '" + userId + "' ");
		PageUtil pager = new PageUtil();
		pager.setPageNo(1);
		pager.setPageSize(100000);
		pager= this.querySql(sb.toString(),
				pager, SysUserRole.class);

		if (pager.isSuccess()) {
			return (List<SysUserRole>) pager.getRoot();
		}
		return new ArrayList<SysUserRole>();
	}
	
	
}
