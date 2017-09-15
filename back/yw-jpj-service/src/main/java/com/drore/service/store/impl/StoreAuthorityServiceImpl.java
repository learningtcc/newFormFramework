package com.drore.service.store.impl;

import com.alibaba.fastjson.JSONObject;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.domain.util.RequestExample;
import com.drore.constant.CommonConstant;
import com.drore.domain.store.StoreAuthority;
import com.drore.domain.store.StoreRoleAuthority;
import com.drore.domain.vo.StoreAuthVo;
import com.drore.enums.CommonEnum;
import com.drore.service.impl.BaseServiceImpl;
import com.drore.service.store.StoreAuthorityService;
import com.drore.service.store.StoreRoleAuthorityService;
import com.drore.util.LoginStoreUserUtil;
import com.drore.util.PageUtil;
import com.drore.util.RestMessageModel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2017<br/>
 * 说明:     <br/>
 * 项目名称: yw-jpj-scenic <br/>
 * 创建日期:  2017/8/31 10:42  <br/>
 * 作者:    wdz
 */
@Service
public class StoreAuthorityServiceImpl extends BaseServiceImpl implements StoreAuthorityService {

    @Autowired
    StoreRoleAuthorityService storeRoleAuthorityService;
    /**
     * 如果唯一返回true，否则返回falseStoreAuthority
     *
     * @param code
     * @param id
     * @return
     */
    public boolean uniqueCode(String code, String id) {
        RequestExample example = new RequestExample(10000,1);
        RequestExample.Param param = example.createParam();
        RequestExample.Criteria cri = example.create();
        param.addTerm("code", code);
        cri.getMust().add(param);
        if (StringUtils.isNotEmpty(id)) {// 如果不为空，那么不查询自己的
            RequestExample.Param notParam = example.createParam();
            notParam.addTerm("id", id);
            cri.getMustNot().add(notParam);
        }
        List<StoreAuthority> list = this.queryList(StoreAuthority.table,
                example, StoreAuthority.class, "id");

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
    public List<StoreAuthority> queryList(String enable) {
        RequestExample example = new RequestExample(10000,1);
        RequestExample.Param param = example.createParam();
        if (!StringUtils.isEmpty(enable)) {
            param.addTerm("is_enable", enable);
            example.create().getMust().add(param);
        }
        List<StoreAuthority> list = this.queryList(StoreAuthority.table,
                example, StoreAuthority.class);
        return list;
    }


    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    public StoreAuthority findById(String id) {
        return this
                .queryOne(id, StoreAuthority.table, StoreAuthority.class);
    }

    /**
     * 保存
     *
     * @param sysAuthority
     * @return
     */
    public RestMessageModel save(StoreAuthority sysAuthority) {
        RestMessageModel model = new RestMessageModel();
        if (!uniqueCode(sysAuthority.getCode(), null)) {
            model.setErrorMessage("编号不能使用");
            return model;
        }
        if (StringUtils.isEmpty(sysAuthority.getParentId()))
            sysAuthority.setParentId(CommonConstant.ROOT);
        sysAuthority.setCreator(LoginStoreUserUtil.getUserId());
        return this.saveObject(sysAuthority,
                StoreAuthority.table);
    }

    /**
     * 更新
     *
     * @param sysAuthority
     * @return
     */
    public RestMessageModel update(StoreAuthority sysAuthority) {
        RestMessageModel model = new RestMessageModel();
        if (!uniqueCode(sysAuthority.getCode(), sysAuthority.getId())) {
            model.setErrorMessage("编号不能使用");
            return model;
        }
        StoreAuthority old = findById(sysAuthority.getId());
        old.setName(sysAuthority.getName());
        old.setCode(sysAuthority.getCode());
        old.setIsEnable(sysAuthority.getIsEnable());
        old.setType(sysAuthority.getType());
        old.setSort(sysAuthority.getSort());
        old.setUrl(sysAuthority.getUrl());
        old.setModifier(LoginStoreUserUtil.getUserId());
        return this.updateObject(sysAuthority.getId(),
                old, StoreAuthority.table);
    }

    /**
     * 查询所有子节点
     *
     * @param parentId
     * @param enable
     * @return
     */
    public List<StoreAuthority> queryByParentId(String parentId, String enable) {
        if (StringUtils.isEmpty(parentId)) {
            parentId = CommonConstant.ROOT;
        }
        RequestExample example = new RequestExample(10000,1);
        RequestExample.Criteria cri = example.create();
        RequestExample.Param param = example.createParam();
        param.addTerm("parent_id", parentId);
        if (StringUtils.isNotEmpty(enable)) {
            param.addTerm("is_enable", enable);
        }
        param.addTerm("is_deleted", CommonEnum.YesOrNo.NO.getCode());

        cri.getMust().add(param);
        // 获取结果
        List<StoreAuthority> queryList = queryList(StoreAuthority.table,
                example, StoreAuthority.class);
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
    @SuppressWarnings("unchecked")
    public List<StoreAuthority> queryByRoleId(String roleId, String enable) {
        //此处级联不支持

        List<StoreRoleAuthority> list = 	storeRoleAuthorityService.queryByRoleId(roleId);
        if(list.size()== 0)
            return new ArrayList<StoreAuthority>();
        StringBuffer auths = new StringBuffer();
        for(StoreRoleAuthority re:list){
            if(auths.length()>0)auths.append(",");
            auths.append("'"+re.getAuthorityId()+"'");
        }


        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT a.* FROM " + StoreAuthority.table
                + " a where  a.is_deleted='N'  ");
        if (StringUtils.isNotEmpty(enable)) {
            sb.append(" and a.is_enable ='").append(enable).append("'");
        }
        sb.append(" and  a.id in ( "+auths.toString()+")");
        PageUtil pager = new PageUtil();
        pager.setPageNo(1);
        pager.setPageSize(100000);
        pager= querySql(sb.toString(), pager, StoreAuthority.class);
        if (pager.isSuccess()) {
            return (List<StoreAuthority>) pager.getRoot();
        }
        return new ArrayList<StoreAuthority>();

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
        List<StoreAuthority> StoreAuthoritys = queryByParentId(parentId,
                CommonEnum.YesOrNo.YES.getCode());
        if (StoreAuthoritys == null || StoreAuthoritys.size() == 0)
            return new ArrayList<JSONObject>();
        String id;
        if (null == list) {
            list = new ArrayList<JSONObject>();
        }
        JSONObject r;
        for (StoreAuthority StoreAuthority : StoreAuthoritys) {
            id = StoreAuthority.getId();
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
                jsonObject.put("modifier", LoginStoreUserUtil.getUserId());
            }
            RestMessage updateBatch = updateBatchObject(StoreAuthority.table,
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
    public List<StoreAuthority> queryAll(String enable) {
        RequestExample example = new RequestExample(10000,1);
        RequestExample.Criteria cri = example.create();
        RequestExample.Param param = example.createParam();
        if (StringUtils.isNotEmpty(enable)) {
            param.addTerm("is_enable", enable);
        }
        param.addTerm("is_deleted", CommonEnum.YesOrNo.NO.getCode());
        cri.getMust().add(param);
        // 获取结果
        List<StoreAuthority> queryList = queryList(StoreAuthority.table,
                example, StoreAuthority.class);
        return queryList;
    }
    /**
     * 递归查询所有节点（包括禁用）
     *
     * @param parentId
     * @param datas
     * @return
     */
    public  List<StoreAuthVo> queryAllChildren(String parentId, List<StoreAuthVo> datas, String enable) {
        RequestExample example = new RequestExample(10000,1);
        RequestExample.Criteria cri = example.create();
        RequestExample.Param param = example.createParam();
        param.addTerm("parent_id", parentId);
        if (StringUtils.isNotEmpty(enable)) {
            param.addTerm("is_enable", enable);
        }

        cri.getMust().add(param);
        // 获取结果
        List<StoreAuthority> queryList = this.queryList(
                StoreAuthority.table, example, StoreAuthority.class,
                new String[] {});

        if (queryList == null || queryList.size() == 0)
            return null;
        StoreAuthVo temp;
        for (int i = 0; i < queryList.size(); i++) {
            temp = new StoreAuthVo(queryList.get(i));
            temp.setChildren(queryAllChildren(temp.getId(),
                    new ArrayList<StoreAuthVo>(), enable));
            datas.add(temp);
        }
        return datas;
    }

    @Override
    public boolean checkIdExist(String id) {
        return checkIdExist(id, StoreAuthority.table,
                StoreAuthority.class);
    }

    @Override
    public RestMessageModel deleteById(String id) {
        return deleteById(id,
                StoreAuthority.table);
    }

    @Override
    public StoreAuthority queryOne(String id) {
        return queryOne(
                id, StoreAuthority.table,
                StoreAuthority.class);
    }
}