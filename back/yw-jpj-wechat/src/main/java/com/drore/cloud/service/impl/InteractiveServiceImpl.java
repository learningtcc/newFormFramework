package com.drore.cloud.service.impl;

import com.drore.cloud.constant.LocalConstant;
import com.drore.cloud.model.InteractiveContent;
import com.drore.cloud.model.InteractiveEvaluation;
import com.drore.cloud.model.InteractiveTheme;
import com.drore.cloud.model.MemberInfo;
import com.drore.cloud.sdk.client.CloudQueryRunner;
import com.drore.cloud.sdk.common.ThreadLocalHolder;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.common.util.DateUtil;
import com.drore.cloud.sdk.domain.Pagination;
import com.drore.cloud.sdk.domain.util.RequestExample;
import com.drore.cloud.service.ImageInfoService;
import com.drore.cloud.service.InteractiveService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangh on 2017/9/12 0001.
 */

@Service
public class InteractiveServiceImpl implements InteractiveService {

    @Autowired
    private CloudQueryRunner run;

    @Autowired
    private ImageInfoService imageInfoService;

    @Override
    public RestMessage getInteractiveThemeList(Integer page_size, Integer current_page) {
        RestMessage restMessage = new RestMessage();
        RequestExample requestExample=new RequestExample(page_size,current_page);
        requestExample.addSort("number","asc");
        Pagination<InteractiveTheme> themePagination = run.queryListByExample(InteractiveTheme.class, InteractiveTheme.table, requestExample);
        restMessage.setData(themePagination);
        return restMessage;
    }

    @Override
    public RestMessage getInteractiveContentList(String interactivThemeFk, Integer page_size, Integer current_page) {
        RestMessage restMessage = new RestMessage();
        RequestExample requestExample=new RequestExample(page_size,current_page);
        RequestExample.Criteria rc = requestExample.create();
        RequestExample.Param pa = requestExample.createParam();
        pa.addTerm("interactiv_theme_fk",interactivThemeFk);
        pa.addTerm("audit_status","Audited");
        pa.addTerm("is_deleted","N");
        rc.getMust().add(pa);
        Pagination<InteractiveContent> contentPagination = run.queryListByExample(InteractiveContent.class, InteractiveContent.table, requestExample);
        //获取图片
        for(InteractiveContent content : contentPagination.getData()){
            List<String> pics = imageInfoService.findPics(InteractiveContent.table, content.getId());
            content.setPicList(pics);

            MemberInfo memberInfo = run.queryOne(MemberInfo.class, MemberInfo.table, content.getPublisher());
            if(memberInfo != null){
                content.setHeadImageUrl(memberInfo.getHeadImageUrl());
            }
        }

        restMessage.setData(contentPagination);
        return restMessage;
    }

    @Override
    public RestMessage getInteractiveContentListByPublisher(Integer page_size, Integer current_page) {
        RestMessage restMessage = new RestMessage();
        MemberInfo sessionMemberInfo = (MemberInfo) ThreadLocalHolder.getSession().getAttribute(LocalConstant.SESSION_CURRENT_USER);
        RequestExample requestExample=new RequestExample(page_size,current_page);
        requestExample.addSort("create_time","desc");
        RequestExample.Criteria rc = requestExample.create();
        RequestExample.Param pa = requestExample.createParam();
        pa.addTerm("publisher",sessionMemberInfo.getId());
        pa.addTerm("is_deleted","N");
        rc.getMust().add(pa);
        Pagination<InteractiveContent> contentPagination = run.queryListByExample(InteractiveContent.class, InteractiveContent.table, requestExample);
        //获取图片
        MemberInfo memberInfo = run.queryOne(MemberInfo.class, MemberInfo.table, sessionMemberInfo.getId());
        for(InteractiveContent content : contentPagination.getData()){
            List<String> pics = imageInfoService.findPics(InteractiveContent.table, content.getId());
            content.setPicList(pics);
            if(memberInfo != null){
                content.setHeadImageUrl(memberInfo.getHeadImageUrl());
            }
        }
        restMessage.setData(contentPagination);
        return restMessage;
    }

    @Override
    public RestMessage queryInteractiveContent(String id ,Integer page_size, Integer current_page) {
        RestMessage restMessage = new RestMessage();
        InteractiveContent interactiveContent = run.queryOne(InteractiveContent.class, InteractiveContent.table, id);
        List<String> pics = imageInfoService.findPics(InteractiveContent.table, id);
        if(interactiveContent != null){
            interactiveContent.setPicList(pics);
        } else{
            return new RestMessage();
        }
        MemberInfo memberInfo = run.queryOne(MemberInfo.class, MemberInfo.table, interactiveContent.getPublisher());
        if(memberInfo != null){
            interactiveContent.setHeadImageUrl(memberInfo.getHeadImageUrl());
        }

        //获取评价
        RequestExample requestExample=new RequestExample(page_size,current_page);
        RequestExample.Criteria rc = requestExample.create();
        RequestExample.Param pa = requestExample.createParam();
        pa.addTerm("interactive_content_fk",id);
        pa.addTerm("audit_status","Audited");
        pa.addTerm("is_deleted","N");
        rc.getMust().add(pa);
        Pagination<InteractiveEvaluation> interactiveEvaluation = run.queryListByExample(InteractiveEvaluation.class, InteractiveEvaluation.table, requestExample);
        for(InteractiveEvaluation evaluation : interactiveEvaluation.getData()){
            memberInfo = run.queryOne(MemberInfo.class, MemberInfo.table, evaluation.getReviewer());
            if(memberInfo != null){
                evaluation.setHeadImageUrl(memberInfo.getHeadImageUrl());
            }
        }

        Map<String,Object> interactive = new HashMap<String,Object>();
        interactive.put("interactiveContent",interactiveContent);
        interactive.put("interactiveEvaluation",interactiveEvaluation);
        restMessage.setData(interactive);
        return restMessage;
    }

    @Override
    public RestMessage saveInteractiveEvaluation(InteractiveEvaluation interactiveEvaluation) {
        //状态处理
        interactiveEvaluation.setAuditStatus("PendingAudit");
        MemberInfo sessionMemberInfo = (MemberInfo) ThreadLocalHolder.getSession().getAttribute(LocalConstant.SESSION_CURRENT_USER);
        //测试id
        String reviewerId = "58b9a959ecef4a22a666b067eb8c6113";
        String reviewerName = "小熊";
        if(sessionMemberInfo != null){
            reviewerId = sessionMemberInfo.getId();
            reviewerName = sessionMemberInfo.getNickName();
        }
        interactiveEvaluation.setReviewer(reviewerId);
        interactiveEvaluation.setReviewerNickname(reviewerName);
        interactiveEvaluation.setEvaluationTime(DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"));

        return run.insert(InteractiveEvaluation.table, interactiveEvaluation);
    }

    @Override
    public RestMessage saveInteractiveContent(InteractiveContent interactiveContent) {
        //状态处理
        interactiveContent.setAuditStatus("PendingAudit");
        MemberInfo sessionMemberInfo = (MemberInfo) ThreadLocalHolder.getSession().getAttribute(LocalConstant.SESSION_CURRENT_USER);
        //测试id
        String publisherId= "58b9a959ecef4a22a666b067eb8c6113";
        String publisherName = "小熊";
        if(sessionMemberInfo != null){
            publisherId = sessionMemberInfo.getId();
            publisherName = sessionMemberInfo.getNickName();
        }
        interactiveContent.setPublisher(publisherId);
        interactiveContent.setPublisherNickname(publisherName);
        interactiveContent.setSubmitTime(DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
        List<String> picList = interactiveContent.getPicList();
        interactiveContent.setPicList(null);
        RestMessage restMessage = run.insert(InteractiveContent.table,interactiveContent);
        if(restMessage.isSuccess()){
            imageInfoService.saveBatch(InteractiveContent.table,restMessage.getId(),picList);
        }

        return restMessage;
    }

    @Override
    public RestMessage deleteInteractiveContent(String id) {
        return run.delete(InteractiveContent.table,id);
    }

}