package com.drore.service.store;

import com.drore.domain.store.StoreUserRole;
import com.drore.util.RestMessageModel;

import java.util.List;

/**
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2017<br/>
 * 说明:     <br/>
 * 项目名称: yw-jpj-scenic <br/>
 * 创建日期:  2017/8/31 13:53  <br/>
 * 作者:    wdz
 */
public interface StoreUserRoleService {

    /**
     * 执行用户角色权限保存操作,保存之前先判断是否存在，存在删除
     * @param list
     * @param userId
     * @return
     */
    public RestMessageModel saveBatch(List<StoreUserRole> list,
                                      String userId);

    /**
     * 根据用户主键获取
     * @param userId
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<StoreUserRole> queryByUserId(String userId);
}
