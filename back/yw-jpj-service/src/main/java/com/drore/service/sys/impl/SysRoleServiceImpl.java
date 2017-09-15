package com.drore.service.sys.impl;

import com.drore.cloud.sdk.domain.util.RequestExample;
import com.drore.cloud.sdk.domain.util.RequestExample.Criteria;
import com.drore.cloud.sdk.domain.util.RequestExample.Param;
import com.drore.domain.sys.SysRole;
import com.drore.domain.sys.SysUserRole;
import com.drore.enums.CommonEnum;
import com.drore.service.impl.BaseServiceImpl;
import com.drore.service.sys.SysRoleService;
import com.drore.service.sys.SysUserRoleService;
import com.drore.util.JSONUtil;
import com.drore.util.LoginSysUserUtil;
import com.drore.util.PageUtil;
import com.drore.util.RestMessageModel;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 浙江卓锐科技股份有限公司 版权所有 © Copyright 2016<br/>
 * 说明:景区角色信息服务层 <br/>
 * 项目名称: guided-assistant-service <br/>
 * 创建日期: 2016年8月13日 下午4:22:49 <br/>
 * 作者: wdz
 */
@Service
public class SysRoleServiceImpl extends BaseServiceImpl implements SysRoleService {

    @Autowired
    SysUserRoleService sysUserRoleService;

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    public SysRole findById(String id) {
        return this.queryOne(id, SysRole.table, SysRole.class);
    }

    /**
     * 保存
     *
     * @param resortsRole
     * @return
     */
    public RestMessageModel save(SysRole resortsRole) {
    	resortsRole.setCreator(LoginSysUserUtil.getUserId());
        return this.saveObject(resortsRole,
                SysRole.table);
    }

    /**
     * 更新
     *
     * @return
     */
    public RestMessageModel update(SysRole resortsRole) {
        SysRole old = findById(resortsRole.getId());
        old.setName(resortsRole.getName());
        old.setCode(resortsRole.getCode());
        old.setIsEnable(resortsRole.getIsEnable());
        old.setRemark(resortsRole.getRemark());
        old.setSort(resortsRole.getSort());
        old.setModifier(LoginSysUserUtil.getUserId());
        return this.updateObject(resortsRole.getId(), old,
                SysRole.table);
    }

    /**
     * 根据状态获取所有 为空查询所有
     *
     * @param enable
     * @return
     */
    public List<SysRole> queryAll(String enable) {
        RequestExample example = new RequestExample(10000, 1);
        Criteria cri = example.create();
        Param param = example.createParam();
        if (StringUtils.isNotEmpty(enable)) {
            param.addTerm("is_enable", enable);
        }
        param.addTerm("is_deleted", CommonEnum.YesOrNo.NO.getCode());
        cri.getMust().add(param);
        // 获取结果
        List<SysRole> queryList = queryList(SysRole.table, example,
                SysRole.class);
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
    public List<SysRole> queryByUserId(String userId, String enable) {

        List<SysUserRole> list = sysUserRoleService.queryByUserId(userId);
        if (list.size() == 0)
            return new ArrayList<SysRole>();
        StringBuffer roleIds = new StringBuffer();
        for (SysUserRole re : list) {
            if (roleIds.length() > 0) roleIds.append(",");
            roleIds.append("'" + re.getRoleId() + "'");
        }


        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT r.* FROM " + SysRole.table + " r where  r.is_deleted='N'  ");
        if (StringUtils.isNotEmpty(enable)) {
            sb.append(" and r.is_enable ='").append(enable).append("'");
        }
        sb.append(" and  r.id in ( " + roleIds + ")");
        PageUtil pager = new PageUtil();
        pager.setPageNo(1);
        pager.setPageSize(100000);
        pager= this.querySql(sb.toString(),
                pager, SysRole.class);

        if (pager.isSuccess()) {
            return (List<SysRole>) pager.getRoot();
        }
        return new ArrayList<SysRole>();
    }

    /**
     * 如果唯一返回true，否则返回falseSysAuthority
     *
     * @param code
     * @param id
     * @return
     */
    public boolean uniqueCode(String code, String id) {
        RequestExample example = new RequestExample(1, 1);
        Param param = example.createParam();
        Criteria cri = example.create();
        param.addTerm("code", code);
        cri.getMust().add(param);
        if (StringUtils.isNotEmpty(id)) {// 如果不为空，那么不查询自己的
            Param notParam = example.createParam();
            notParam.addTerm("id", id);
            cri.getMustNot().add(notParam);
        }
        List<SysRole> list = this.queryList(SysRole.table,
                example, SysRole.class, "id");

        if (list != null && list.size() > 0)
            return false;
        else
            return true;

    }

    /**
     * 分页查询
     *
     * @param pageUtil
     * @param role
     * @return
     */
    public <T> PageUtil findShowListByPage(PageUtil pageUtil,
                                            SysRole role) {

        StringBuffer sql = new StringBuffer()
                .append("select *  from ")
                .append(SysRole.table)
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
        pageUtil= querySql(sql.toString(), pageUtil, SysRole.class);
        pageUtil.setRoot((List<?>) JSONUtil.beanToMap(pageUtil.getRoot()));
        return pageUtil;

    }

    @Override
    public boolean checkIdExit(String id) {
        return  checkIdExist(id, SysRole.table, SysRole.class);
    }

    @Override
    public RestMessageModel deleteById(String id) {
        return  deleteById(id,
                SysRole.table);
    }
}
