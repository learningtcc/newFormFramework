package com.drore.service.store;

import com.drore.domain.store.StoreRole;
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
public interface StoreRoleService {
    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    public StoreRole findById(String id);

    /**
     * 保存
     *
     * @param resortsRole
     * @return
     */
    public RestMessageModel save(StoreRole resortsRole);


    /**
     * 更新
     *
     * @return
     */
    public RestMessageModel update(StoreRole resortsRole);


    /**
     * 根据状态获取所有 为空查询所有
     *
     * @param enable
     * @return
     */
    public List<StoreRole> queryAll(String enable);


    /**
     * 根据用户主键，是否启用状态
     *
     * @param userId
     * @param enable
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<StoreRole> queryByUserId(String userId, String enable);


    /**
     * 如果唯一返回true，否则返回falseStoreAuthority
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
                                           StoreRole role);


    public boolean checkIdExit(String id);


    public  RestMessageModel deleteById(String id);
}
