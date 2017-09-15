package com.drore.service.store;

import com.drore.domain.store.StoreRoleAuthority;
import com.drore.util.RestMessageModel;

import java.util.List;

/**
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2017<br/>
 * 说明:     <br/>
 * 项目名称: yw-jpj-scenic <br/>
 * 创建日期:  2017/8/31 13:52  <br/>
 * 作者:    wdz
 */
public interface StoreRoleAuthorityService {
    /**
     * 执行用户角色权限保存操作,保存之前先判断是否存在，存在删除
     *
     * @param list
     * @return
     */
    public RestMessageModel saveBatch(List<StoreRoleAuthority> list,
                                      String roleId);

    /**
     * 根据角色主键查找
     * @param roleId
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<StoreRoleAuthority> queryByRoleId(String roleId);
}
