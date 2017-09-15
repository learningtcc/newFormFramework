package com.drore.service.store.impl;

import com.drore.cloud.sdk.domain.util.RequestExample;
import com.drore.domain.store.StoreRoleAuthority;
import com.drore.enums.CommonEnum;
import com.drore.service.impl.BaseServiceImpl;
import com.drore.service.store.StoreRoleAuthorityService;
import com.drore.util.LoginStoreUserUtil;
import com.drore.util.PageUtil;
import com.drore.util.RestMessageModel;
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
public class StoreRoleAuthorityServiceImpl extends BaseServiceImpl implements StoreRoleAuthorityService {
    /**
     * 执行用户角色权限保存操作,保存之前先判断是否存在，存在删除
     *
     * @param list
     * @return
     */
    public RestMessageModel saveBatch(List<StoreRoleAuthority> list,
                                      String roleId) {
        RequestExample example = new RequestExample(100000,1);
        RequestExample.Criteria cri = example.create();
        RequestExample.Param param = example.createParam();
        param.addTerm("is_deleted", CommonEnum.YesOrNo.NO.getCode());
        param.addTerm("role_id", roleId);
        cri.getMust().add(param);
        List<StoreRoleAuthority> exits = this.queryList(
                StoreRoleAuthority.table, example,
                StoreRoleAuthority.class, new String[] { "id" });
        if (exits != null && exits.size() > 0) {
            String[] pkIds = new String[exits.size()];
            for (int i=0;i<exits.size();i++){
                pkIds[i]=exits.get(i).getId();
            }

            RestMessageModel model = this.deleteByIds(pkIds,
                    StoreRoleAuthority.table);
            if (!model.isSuccess()) {
                model.setErrorMessage("删除角色权限关联信息错误");
                return model;
            }
        }
        for(StoreRoleAuthority sysRoleAuthority :list){
            sysRoleAuthority.setModifier(LoginStoreUserUtil.getUserId());
        }
        return this.saveObjectBatch(StoreRoleAuthority.table, list);

    }
    /**
     * 根据角色主键查找
     * @param roleId
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<StoreRoleAuthority> queryByRoleId(String roleId) {

        StringBuffer sb = new StringBuffer();
        sb.append("  select ra.authority_id  from  "  + StoreRoleAuthority.table + " ra ");
        sb.append(" where  ra.is_deleted = 'N' AND ra.role_id = '" + roleId + "' ");
        PageUtil pager = new PageUtil();
        pager.setPageNo(1);
        pager.setPageSize(100000);
        pager= this.querySql(sb.toString(),
                pager, StoreRoleAuthority.class);

        if (pager.isSuccess()) {
            return (List<StoreRoleAuthority>) pager.getRoot();
        }
        return new ArrayList<StoreRoleAuthority>();
    }

}
