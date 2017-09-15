package com.drore.service.impl;

import com.drore.cloud.sdk.client.CloudQueryRunner;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.domain.Pagination;
import com.drore.cloud.sdk.domain.util.RequestExample;
import com.drore.model.InteractiveContent;
import com.drore.model.InteractiveEvaluation;
import com.drore.model.InteractiveTheme;
import com.drore.model.ThemeActivities;
import com.drore.service.ImageInfoService;
import com.drore.service.InteractiveService;
import com.drore.util.PageUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangh on 2017/9/4 0001.
 */

@Service
public class InteractiveServiceImpl implements InteractiveService {

    @Autowired
    private CloudQueryRunner run;

    @Autowired
    private ImageInfoService imageInfoService;

    @Override
    public PageUtil getInteractiveThemeList(Integer page_size, Integer current_page) {
        RequestExample requestExample=new RequestExample(page_size,current_page);
        //创建Criteria
        RequestExample.Criteria rc = requestExample.create();
        RequestExample.Param pa = requestExample.createParam();
        pa.addTerm("is_deleted","N");
        rc.getMust().add(pa);
        requestExample.addSort("number","asc");
        Pagination<InteractiveTheme> themePagination = run.queryListByExample(InteractiveTheme.class, InteractiveTheme.table, requestExample);
        PageUtil pageUtil = new PageUtil(themePagination);
        return pageUtil;
    }

    @Override
    public RestMessage saveInteractiveTheme(InteractiveTheme interactiveTheme) {
        return run.insert(InteractiveTheme.table, interactiveTheme);
    }

    @Override
    public RestMessage deleteInteractiveTheme(String id) {
        return run.delete(InteractiveTheme.table,id);
    }

    @Override
    public RestMessage updateInteractiveTheme(InteractiveTheme interactiveTheme) {
        return run.update(InteractiveTheme.table,interactiveTheme.getId(),interactiveTheme);
    }

    @Override
    public RestMessage queryInteractiveTheme(String id) {
        RestMessage restMessage = new RestMessage();
        InteractiveTheme interactiveTheme = run.queryOne(InteractiveTheme.class, InteractiveTheme.table, id);
        restMessage.setData(interactiveTheme);
        return restMessage;
    }

    @Override
    public PageUtil getInteractiveContentList(InteractiveContent content, String startTime, String endTime, Integer page_size, Integer current_page) {
        //动态拼接sql语句
        StringBuffer sql = new StringBuffer("select * from interactive_content where 1=1");
        sql.append(" and is_deleted = 'N'");
        sql.append(" and audit_status = 'Audited'");
        if(StringUtils.isNotEmpty(content.getInteractivThemeFk())){
            sql.append(" and interactiv_theme_fk = '" + content.getInteractivThemeFk() + "'");
        }
       if(StringUtils.isNotEmpty(content.getInteractionTitle())){
           sql.append(" and Interaction_title like '%" + content.getInteractionTitle() + "%'");
       }
       if(StringUtils.isNotEmpty(startTime)){
           sql.append(" and publisher_time >= '"+ startTime +" 00:00:00'");
       }
        if(StringUtils.isNotEmpty(endTime)){
            sql.append(" and publisher_time <= '"+ endTime +" 23:59:59'");
        }
        sql.append(" order by create_time desc");
        Pagination<InteractiveContent> contentPagination = run.sql(InteractiveContent.class, sql.toString(), current_page, page_size);
        PageUtil pageUtil = new PageUtil(contentPagination);
        return pageUtil;
    }

    @Override
    public RestMessage queryInteractiveContent(String id) {
        RestMessage restMessage = new RestMessage();
        InteractiveContent interactiveContent = run.queryOne(InteractiveContent.class, InteractiveContent.table, id);
        List<String> pics = imageInfoService.findPics(InteractiveContent.table, id);
        interactiveContent.setPicList(pics);
        restMessage.setData(interactiveContent);
        return restMessage;
    }

    @Override
    public PageUtil getInteractiveEvaluationList(String contentId,Integer page_size, Integer current_page) {
        RequestExample requestExample=new RequestExample(page_size,current_page);
        //模糊查询
        //创建Criteria
        RequestExample.Criteria rc = requestExample.create();
        RequestExample.Param pa = requestExample.createParam();
        pa.addTerm("interactive_content_fk",contentId);
        pa.addTerm("is_deleted","N");
        rc.getMust().add(pa);
        Pagination<InteractiveEvaluation> evaluationPagination = run.queryListByExample(InteractiveEvaluation.class, InteractiveEvaluation.table, requestExample);
        PageUtil pageUtil = new PageUtil(evaluationPagination);
        return pageUtil;
    }
}