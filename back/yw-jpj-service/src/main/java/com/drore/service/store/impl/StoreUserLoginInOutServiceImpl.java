package com.drore.service.store.impl;

import com.drore.domain.store.StoreUser;
import com.drore.domain.store.StoreUserLoginInOut;
import com.drore.enums.CommonEnum;
import com.drore.service.impl.BaseServiceImpl;
import com.drore.service.store.StoreUserLoginInOutService;
import com.drore.service.store.StoreUserService;
import com.drore.util.JSONUtil;
import com.drore.util.LoginStoreUserUtil;
import com.drore.util.PageUtil;
import com.drore.util.RestMessageModel;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2017<br/>
 * 说明:     <br/>
 * 项目名称: yw-jpj-scenic <br/>
 * 创建日期:  2017/8/31 10:42  <br/>
 * 作者:    wdz
 */
@Service
public class StoreUserLoginInOutServiceImpl extends BaseServiceImpl implements StoreUserLoginInOutService {
    @Autowired
    private StoreUserService userService;

    /**
     * 保存
     *
     * @param resortsUserLoginInOut
     * @return
     */
    public RestMessageModel save(StoreUserLoginInOut resortsUserLoginInOut) {
        resortsUserLoginInOut.setCreator(LoginStoreUserUtil.getUserId());
        return this.saveObject(resortsUserLoginInOut, StoreUserLoginInOut.table);
    }

    public StoreUserLoginInOut findById(String id) {
        return queryOne(id, StoreUserLoginInOut.table, StoreUserLoginInOut.class);
    }

    /**
     * 分页查询用户登录登出信息
     * @param pageUtil
     * @param userLoginInOut
     * @return
     * @throws ParseException
     */
    public PageUtil findByPage(PageUtil pageUtil,
                               StoreUserLoginInOut userLoginInOut) throws ParseException {
        StringBuilder sql = new StringBuilder().append("select * from ");
        sql.append(StoreUserLoginInOut.table +" s,"+ StoreUser.table+" u");
        sql.append(" where s.user_id=u.id and u.is_deleted = 'N' ");
        sql.append(" and u.store_id ='"+LoginStoreUserUtil.getStoreId()+"'    ");
        // 用户名称
        if (StringUtils.isNotEmpty(userLoginInOut.getUserIdName())) {
            sql.append(" and u.user_name like '%" + userLoginInOut.getUserIdName()
                    + "%' ");
        }
        int inOutType = userLoginInOut.getInoutType();
        // 判断登录登出
        if (inOutType == CommonEnum.LoginInOut.IN.getValue()
                || inOutType == CommonEnum.LoginInOut.OUT.getValue()) {
            sql.append(" and s.inout_type = " + inOutType);
        }
        // 用户ip
        if (StringUtils.isNotEmpty(userLoginInOut.getIp())) {
            sql.append(" and s.ip like '%" + userLoginInOut.getIp() + "%'");
        }
        // 开始时间
        if (StringUtils.isNotEmpty(userLoginInOut.getStartDate())) {
            sql.append(" and s.record_date>='" + userLoginInOut.getStartDate()
                    + "'");
        }
        // 结束时间
        if (StringUtils.isNotEmpty(userLoginInOut.getEndDate())) {
            sql.append(" and s.record_date<='" + userLoginInOut.getEndDate()
                    + "'");
        }
        sql.append(" order by s.record_date desc");
        PageUtil pager = querySql(sql.toString(), pageUtil,
                StoreUserLoginInOut.class);
        List<StoreUserLoginInOut> list = null;
        if (pager.getRoot() != null) {
            // 保存用户信息
            Map<String, String> userMap = new HashMap<String, String>();
            list = (List<StoreUserLoginInOut>) pager.getRoot();
            for (StoreUserLoginInOut temp : list) {
                String userId = temp.getUserId();
                if (StringUtils.isNotEmpty(userId)) {
                    if (userMap.containsKey(userId)) {
                        temp.setUserIdName(userMap.get(userId));
                    } else {
                        StoreUser user = userService.findById(temp.getUserId());
                        userMap.put(userId,
                                user == null ? "" : user.getUserName());
                        temp.setUserIdName(user == null ? "" : user
                                .getUserName());
                    }
                }
            }
        }
        pager.setRoot((List<?>) JSONUtil.beanToMap(list));
        return pager;
    }
}