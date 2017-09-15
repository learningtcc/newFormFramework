package com.drore.service.sys;

import com.drore.domain.sys.SysRole;
import com.drore.util.PageUtil;
import com.drore.util.RestMessageModel;

import java.util.List;

/**
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2017<br/>
 * 说明:     <br/>
 * 项目名称: yw-jpj-scenic <br/>
 * 创建日期:  2017/8/31 13:52  <br/>
 * 作者:    wdz
 */
public interface SysRoleService {
    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    public SysRole findById(String id);

    /**
     * 保存
     *
     * @param resortsRole
     * @return
     */
    public RestMessageModel save(SysRole resortsRole);


    /**
     * 更新
     *
     * @return
     */
    public RestMessageModel update(SysRole resortsRole);


    /**
     * 根据状态获取所有 为空查询所有
     *
     * @param enable
     * @return
     */
    public List<SysRole> queryAll(String enable);


    /**
     * 根据用户主键，是否启用状态
     *
     * @param userId
     * @param enable
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<SysRole> queryByUserId(String userId, String enable);


    /**
     * 如果唯一返回true，否则返回falseSysAuthority
     *
     * @param code
     * @param id
     * @return
     */
    public boolean uniqueCode(String code, String id);

    /**
     * 分页查询
     *
     * @param pageUtil
     * @param role
     * @return
     */
    public <T> PageUtil findShowListByPage(PageUtil pageUtil,
                                           SysRole role);

    public boolean checkIdExit(String id);


    public  RestMessageModel deleteById(String id);
}
