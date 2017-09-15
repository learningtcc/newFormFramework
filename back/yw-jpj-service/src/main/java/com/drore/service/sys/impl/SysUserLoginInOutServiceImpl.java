package com.drore.service.sys.impl;


import com.drore.domain.sys.SysUser;
import com.drore.domain.sys.SysUserLoginInOut;
import com.drore.enums.CommonEnum;
import com.drore.service.impl.BaseServiceImpl;
import com.drore.service.sys.SysUserLoginInOutService;
import com.drore.service.sys.SysUserService;
import com.drore.util.JSONUtil;
import com.drore.util.LoginSysUserUtil;
import com.drore.util.PageUtil;
import com.drore.util.RestMessageModel;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 浙江卓锐科技股份有限公司 版权所有 © Copyright 2016<br/>
 * 说明:登录登出 <br/>
 * 项目名称: guided-assistant-service <br/>
 * 创建日期: 2016年8月19日 下午7:33:52 <br/>
 * 作者: wdz
 */
@Service
public class SysUserLoginInOutServiceImpl extends BaseServiceImpl implements SysUserLoginInOutService {
	@Autowired
	private SysUserService userService;

	/**
	 * 保存
	 *
	 * @param resortsUserLoginInOut
	 * @return
	 */
	public RestMessageModel save(SysUserLoginInOut resortsUserLoginInOut) {
		resortsUserLoginInOut.setCreator(LoginSysUserUtil.getUserId());
		return this.saveObject(resortsUserLoginInOut, SysUserLoginInOut.table);
	}

	public SysUserLoginInOut findById(String id) {
		return queryOne(id, SysUserLoginInOut.table, SysUserLoginInOut.class);
	}

	/**
	 * 分页查询用户登录登出信息
	 * @param pageUtil
	 * @param userLoginInOut
	 * @return
	 * @throws ParseException
     */
	public PageUtil findByPage(PageUtil pageUtil,
			SysUserLoginInOut userLoginInOut) throws ParseException {
		StringBuilder sql = new StringBuilder().append("select * from ");
				sql.append(SysUserLoginInOut.table +" s,"+ SysUser.table+" u");
				sql.append(" where s.user_id=u.id and u.is_deleted = 'N' ");

		// 用户名称
		if (StringUtils.isNotEmpty(userLoginInOut.getUserIdName())) {
			sql.append(" and u.user_name like '%" + userLoginInOut.getUserIdName()
					+ "%' ");
		}
		int inOutType = userLoginInOut.getInoutType();
		// 判断登录登出
		if (inOutType == CommonEnum.LoginInOut.IN.getValue()
				|| inOutType == CommonEnum.LoginInOut.OUT.getValue()) {
			sql.append(" and s.inout_type = " + inOutType);
		}
		// 用户ip
		if (StringUtils.isNotEmpty(userLoginInOut.getIp())) {
			sql.append(" and s.ip like '%" + userLoginInOut.getIp() + "%'");
		}
		// 开始时间
		if (StringUtils.isNotEmpty(userLoginInOut.getStartDate())) {
			sql.append(" and s.record_date>='" + userLoginInOut.getStartDate()
					+ "'");
		}
		// 结束时间
		if (StringUtils.isNotEmpty(userLoginInOut.getEndDate())) {
			sql.append(" and s.record_date<='" + userLoginInOut.getEndDate()
					+ "'");
		}
		sql.append(" order by s.record_date desc");
		PageUtil pager = querySql(sql.toString(), pageUtil,
				SysUserLoginInOut.class);
		List<SysUserLoginInOut> list = null;
		if (pager.getRoot() != null) {
			// 保存用户信息
			Map<String, String> userMap = new HashMap<String, String>();
			list = (List<SysUserLoginInOut>) pager.getRoot();
			for (SysUserLoginInOut temp : list) {
				String userId = temp.getUserId();
				if (StringUtils.isNotEmpty(userId)) {
					if (userMap.containsKey(userId)) {
						temp.setUserIdName(userMap.get(userId));
					} else {
						SysUser user = userService.findById(temp.getUserId());
						userMap.put(userId,
								user == null ? "" : user.getUserName());
						temp.setUserIdName(user == null ? "" : user
								.getUserName());
					}
				}
			}
		}
		pager.setRoot((List<?>) JSONUtil.beanToMap(list));
		return pager;
	}
}
