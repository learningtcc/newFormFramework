package com.drore.service.store;

import com.alibaba.fastjson.JSONObject;
import com.drore.domain.store.StoreAuthority;
import com.drore.domain.vo.StoreAuthVo;
import com.drore.util.RestMessageModel;

import java.util.List;

/**
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2017<br/>
 * 说明:     <br/>
 * 项目名称: yw-jpj-scenic <br/>
 * 创建日期:  2017/8/31 13:52  <br/>
 * 作者:    wdz
 */
public interface StoreAuthorityService {
    /**
     * 如果唯一返回true，否则返回falseSysAuthority
     *
     * @param code
     * @param id
     * @return
     */
    public boolean uniqueCode(String code, String id);


    /**
     * 查询权限列表， 此处根据isEnable查询 为空查询所有，不为空，查询对应状态的
     *
     * @return
     */
    public List<StoreAuthority> queryList(String enable);

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    public StoreAuthority findById(String id);


    /**
     * 保存
     *
     * @param storeAuthority
     * @return
     */
    public RestMessageModel save(StoreAuthority storeAuthority);


    /**
     * 更新
     *
     * @param storeAuthority
     * @return
     */
    public RestMessageModel update(StoreAuthority storeAuthority);


    /**
     * 查询所有子节点
     *
     * @param parentId
     * @param enable
     * @return
     */
    public List<StoreAuthority> queryByParentId(String parentId, String enable);

    /**
     * 根据角色ID，查找角色拥有的权限
     *
     * @param roleId
     * @param enable
     *            如果为空，查询全部，不为空，根据查询是否有效的
     * @return
     */
    public List<StoreAuthority> queryByRoleId(String roleId, String enable);

    /**
     * 递归查询父节点下所有权限的id集合,并将状态设置为禁用
     *
     * @param parentId
     * @param list
     * @return
     */
    public List<JSONObject> queryAllChildren(String parentId,
                                             List<JSONObject> list);


    /**
     * 根据父节点禁用所有子节点
     *
     * @param parentId
     * @return
     */
    public boolean disableChildrenByParentId(String parentId);


    /**
     * 获取所有权限 根据状态
     *
     * @param enable
     * @return
     */
    public List<StoreAuthority> queryAll(String enable);


    /**
     * 递归查询所有节点（包括禁用）
     *
     * @param parentId
     * @param datas
     * @return
     */
    public  List<StoreAuthVo> queryAllChildren(String parentId, List<StoreAuthVo> datas, String enable);

    public boolean checkIdExist(String id);


    public  RestMessageModel deleteById(String id);

    public StoreAuthority queryOne(String id);
}
