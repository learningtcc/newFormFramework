package com.drore.service.store.impl;

import com.drore.cloud.sdk.domain.util.RequestExample;
import com.drore.domain.store.StoreUserRole;
import com.drore.enums.CommonEnum;
import com.drore.service.impl.BaseServiceImpl;
import com.drore.service.store.StoreUserRoleService;
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
 * 创建日期:  2017/8/31 10:43  <br/>
 * 作者:    wdz
 */
@Service
public class StoreUserRoleServiceImpl extends BaseServiceImpl implements StoreUserRoleService {
    /**
     * 执行用户角色权限保存操作,保存之前先判断是否存在，存在删除
     * @param list
     * @param userId
     * @return
     */
    public RestMessageModel saveBatch(List<StoreUserRole> list,
                                      String userId) {
        RequestExample example = new RequestExample(100000,1);
        RequestExample.Criteria cri = example.create();
        RequestExample.Param param = example.createParam();
        param.addTerm("is_deleted", CommonEnum.YesOrNo.NO.getCode());
        param.addTerm("user_id", userId);
        cri.getMust().add(param);
        List<StoreUserRole> exits = this.queryList(
                StoreUserRole.table, example,
                StoreUserRole.class, new String[] { "id" });
        if (exits != null && exits.size() > 0) {
            List<String> ids = new ArrayList<String>();
            for (StoreUserRole temp : exits)
                ids.add(temp.getId());

            String[] strArr = new String[ids.size()];
            strArr = ids.toArray(strArr);
            RestMessageModel model = this.deleteByIds(strArr,
                    StoreUserRole.table);
            if (!model.isSuccess()) {
                model.setErrorMessage("删除用户角色关联信息错误");
                return model;
            }
        }
        for(StoreUserRole sysUserRole:list ){
            sysUserRole.setModifier(LoginStoreUserUtil.getUserId());
        }
        return this.saveObjectBatch(StoreUserRole.table, list);

    }

    /**
     * 根据用户主键获取
     * @param userId
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<StoreUserRole> queryByUserId(String userId) {

        StringBuffer sb = new StringBuffer();
        sb.append("  select ra.role_id  from  "  + StoreUserRole.table + " ra ");
        sb.append(" where  ra.is_deleted = 'N' AND ra.user_id = '" + userId + "' ");
        PageUtil pager = new PageUtil();
        pager.setPageNo(1);
        pager.setPageSize(100000);
        pager= this.querySql(sb.toString(),
                pager, StoreUserRole.class);

        if (pager.isSuccess()) {
            return (List<StoreUserRole>) pager.getRoot();
        }
        return new ArrayList<StoreUserRole>();
    }


}

