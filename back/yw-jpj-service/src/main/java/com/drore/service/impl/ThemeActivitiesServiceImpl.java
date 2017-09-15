package com.drore.service.impl;

import com.drore.cloud.sdk.client.CloudQueryRunner;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.common.util.DateUtil;
import com.drore.cloud.sdk.domain.Pagination;
import com.drore.enums.CommonEnum;
import com.drore.exception.CustomException;
import com.drore.model.ThemeActivities;
import com.drore.service.ClicksInfoService;
import com.drore.service.ThemeActivitiesService;
import com.drore.util.PageUtil;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangh on 2017/9/1 0001.
 */

@Service
public class ThemeActivitiesServiceImpl implements ThemeActivitiesService {

	@Autowired
	private CloudQueryRunner run;
	@Autowired
	ClicksInfoService clicksInfoService;

	@Override
	public PageUtil getThemeActivities(ThemeActivities themeActivities, Integer pageSize, Integer pageNo) {
		//动态拼接sql语句
		StringBuffer sql = new StringBuffer("select * from theme_activities where is_deleted='N'");
		if(StringUtils.isNotEmpty(themeActivities.getIsRelease())){
			sql.append(" and is_release = '" + themeActivities.getIsRelease() + "'");
		}
		if(StringUtils.isNotEmpty(themeActivities.getTitle())){
			sql.append(" and title like '%" + themeActivities.getTitle() + "%'");
		}
		if(StringUtils.isNotEmpty(themeActivities.getType())){
			sql.append(" and type like '%" + themeActivities.getType() + "%'");
		}
		//筛选活动开始结束时间
		if(StringUtils.isNotEmpty(themeActivities.getStartTime())){
			sql.append(" and act_start_time >= '" + themeActivities.getStartTime() + "'");
		}
		if(StringUtils.isNotEmpty(themeActivities.getEndTime())){
			sql.append(" and act_end_time <= '" + themeActivities.getEndTime() + "'");
		}
		sql.append(" order by create_time desc");
		Pagination<ThemeActivities> themeActivitiesPagination = run.sql(ThemeActivities.class, sql.toString(), pageNo, pageSize);
		PageUtil pageUtil = new PageUtil(themeActivitiesPagination);
		return pageUtil;
	}

	@Override
	public RestMessage saveThemeActivitie(ThemeActivities themeActivities) {
		//设置初始状态
		themeActivities.setIsNewest("N");
		themeActivities.setIsTop("N");
		themeActivities.setIsUsing("N");
		themeActivities.setReleaseTime(DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
		return run.insert(ThemeActivities.table, themeActivities);
	}

	@Override
	public RestMessage deleteThemeActivity(String id) {
		return run.delete(ThemeActivities.table,id);
	}

	@Override
	public RestMessage updateThemeActivity(ThemeActivities themeActivities) {
		return run.update(ThemeActivities.table,themeActivities.getId(),themeActivities);
	}

	@Override
	public RestMessage queryThemeActivity(String id) {
		RestMessage restMessage = new RestMessage();
		ThemeActivities themeActivities = run.queryOne(ThemeActivities.class, ThemeActivities.table, id);
		restMessage.setData(themeActivities);
		return restMessage;
	}

	@Override
	public RestMessage setIsUsing(String id, String status) {
		RestMessage restMessage = run.update(ThemeActivities.table, id, ImmutableMap.of("is_using", status));
		return restMessage;
	}

	@Override
	public RestMessage setTop(String id) {
		RestMessage restMessage = new RestMessage();
		Pagination<Map> pagination = run.queryListByExample(ThemeActivities.table, 1,Integer.MAX_VALUE);
		List<Map> data = pagination.getData();
		for (int i = 0; i < data.size(); i++) {
			String id1 = String.valueOf(data.get(i).get("id"));
			run.update(ThemeActivities.table,id1, ImmutableMap.of("is_top", "N"));
		}
		restMessage = run.update(ThemeActivities.table, id, ImmutableMap.of("is_top", "Y"));
		if (restMessage.isSuccess()){
			restMessage.setMessage("置顶成功");
		}
		return restMessage;
	}

	@Override
	public ThemeActivities queryDetailApiById(String id, String name, String memberId, String memberName) {
		 		ThemeActivities themeActivities = run.queryOne(ThemeActivities.class, ThemeActivities.table, id);
		if (themeActivities == null) {
			throw new CustomException("未发现查找对象!");
		}


		themeActivities.setClicks(themeActivities.getClicks() + 1);
		RestMessage restMessage = run.update(ThemeActivities.table, id, themeActivities);
		if (!restMessage.isSuccess()) {
			throw new CustomException(restMessage.getMessage());
		}
		restMessage = clicksInfoService.save(id, ThemeActivities.table, name, CommonEnum.ClickSourceEnum.WEIXIN.getCode(), memberId, memberName);
		if (!restMessage.isSuccess()) {
			throw new CustomException("保存点击率信息报错" + restMessage.getMessage());
		}

		return themeActivities;
	}

}
