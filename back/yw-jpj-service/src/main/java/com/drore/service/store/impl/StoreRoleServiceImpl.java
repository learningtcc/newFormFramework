package com.drore.service.store.impl;

import com.drore.cloud.sdk.domain.util.RequestExample;
import com.drore.domain.store.StoreRole;
import com.drore.domain.store.StoreUserRole;
import com.drore.enums.CommonEnum;
import com.drore.service.impl.BaseServiceImpl;
import com.drore.service.store.StoreRoleService;
import com.drore.service.store.StoreUserRoleService;
import com.drore.util.JSONUtil;
import com.drore.util.LoginStoreUserUtil;
import com.drore.util.PageUtil;
import com.drore.util.RestMessageModel;
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
public class StoreRoleServiceImpl extends BaseServiceImpl implements StoreRoleService {

    @Autowired
    StoreUserRoleService storeUserRoleService;

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    public StoreRole findById(String id) {
        return this.queryOne(id, StoreRole.table, StoreRole.class);
    }

    /**
     * 保存
     *
     * @param resortsRole
     * @return
     */
    public RestMessageModel save(StoreRole resortsRole) {
        resortsRole.setCreator(LoginStoreUserUtil.getUserId());
        return this.saveObject(resortsRole,
                StoreRole.table);
    }

    /**
     * 更新
     *
     * @param storeRole
     * @return
     */
    public RestMessageModel update(StoreRole storeRole) {
        StoreRole old = findById(storeRole.getId());
        old.setName(storeRole.getName());
        old.setCode(storeRole.getCode());
        old.setIsEnable(storeRole.getIsEnable());
        old.setRemark(storeRole.getRemark());
        old.setSort(storeRole.getSort());
        old.setModifier(LoginStoreUserUtil.getUserId());
        return this.updateObject(storeRole.getId(), old,
                StoreRole.table);
    }

    /**
     * 根据状态获取所有 为空查询所有
     *
     * @param enable
     * @return
     */
    public List<StoreRole> queryAll(String enable) {
        RequestExample example = new RequestExample(10000, 1);
        RequestExample.Criteria cri = example.create();
        RequestExample.Param param = example.createParam();
        if (StringUtils.isNotEmpty(enable)) {
            param.addTerm("is_enable", enable);
        }
        param.addTerm("is_deleted", CommonEnum.YesOrNo.NO.getCode());
        cri.getMust().add(param);
        // 获取结果
        List<StoreRole> queryList = queryList(StoreRole.table, example,
                StoreRole.class);
        return queryList;
    }

    /**
     * 根据用户主键，是否启用状态
     *
     * @param userId
     * @param enable
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<StoreRole> queryByUserId(String userId, String enable) {

        List<StoreUserRole> list = storeUserRoleService.queryByUserId(userId);
        if (list.size() == 0)
            return new ArrayList<StoreRole>();
        StringBuffer roleIds = new StringBuffer();
        for (StoreUserRole re : list) {
            if (roleIds.length() > 0) roleIds.append(",");
            roleIds.append("'" + re.getRoleId() + "'");
        }


        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT r.* FROM " + StoreRole.table + " r where  r.is_deleted='N'  ");
        if (StringUtils.isNotEmpty(enable)) {
            sb.append(" and r.is_enable ='").append(enable).append("'");
        }
        sb.append(" and  r.id in ( " + roleIds + ")");
        PageUtil pager = new PageUtil();
        pager.setPageNo(1);
        pager.setPageSize(100000);
        pager= this.querySql(sb.toString(),
                pager, StoreRole.class);

        if (pager.isSuccess()) {
            return (List<StoreRole>) pager.getRoot();
        }
        return new ArrayList<StoreRole>();
    }

    /**
     * 如果唯一返回true，否则返回falseSysAuthority
     *
     * @param id
     * @return
     */
    public boolean uniqueCode(String code, String id) {
        RequestExample example = new RequestExample(1, 1);
        RequestExample.Param param = example.createParam();
        RequestExample.Criteria cri = example.create();
        param.addTerm("code", code);
        cri.getMust().add(param);
        if (StringUtils.isNotEmpty(id)) {// 如果不为空，那么不查询自己的
            RequestExample.Param notParam = example.createParam();
            notParam.addTerm("id", id);
            cri.getMustNot().add(notParam);
        }
        List<StoreRole> list = this.queryList(StoreRole.table,
                example, StoreRole.class, "id");

        if (list != null && list.size() > 0)
            return false;
        else
            return true;

    }

    /**
     * 分页查询
     *
     * @param role
     * @return
     */
    public <T> PageUtil findShowListByPage(PageUtil pageUtil,
                                           StoreRole role) {

        StringBuffer sql = new StringBuffer()
                .append("select *  from ")
                .append(StoreRole.table)
                .append("  where is_deleted='N' ");
        // 条件查询
        if (null != role) {
            if (StringUtils.isNotEmpty(role.getName())) {
                sql.append("and name like '%" + role.getName() + "%'");
            }
            if (StringUtils.isNotEmpty(role.getIsEnable())) {
                sql.append("and is_enable='" + role.getIsEnable() + "'");
            }
        }

        sql.append(" order by sort ");
        pageUtil= querySql(sql.toString(), pageUtil, StoreRole.class);
        pageUtil.setRoot((List<?>) JSONUtil.beanToMap(pageUtil.getRoot()));
        return pageUtil;

    }

    @Override
    public boolean checkIdExit(String id) {
        return  checkIdExist(id, StoreRole.table, StoreRole.class);
    }

    @Override
    public RestMessageModel deleteById(String id) {
        return  deleteById(id,
                StoreRole.table);
    }

}