package com.drore.service.store.impl;

import com.alibaba.fastjson.JSONObject;
import com.drore.cloud.sdk.domain.email.EmailModel;
import com.drore.cloud.sdk.domain.util.RequestExample;
import com.drore.domain.DataDictionary;
import com.drore.domain.SystemConfig;
import com.drore.domain.store.StoreAuthority;
import com.drore.domain.store.StoreRole;
import com.drore.domain.store.StoreUser;
import com.drore.domain.store.StoreUserRole;
import com.drore.enums.CommonEnum;
import com.drore.service.DataDictionaryService;
import com.drore.service.SystemConfigService;
import com.drore.service.impl.BaseServiceImpl;
import com.drore.service.store.StoreAuthorityService;
import com.drore.service.store.StoreRoleService;
import com.drore.service.store.StoreUserService;
import com.drore.util.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.*;

/**
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2017<br/>
 * 说明:     <br/>
 * 项目名称: yw-jpj-scenic <br/>
 * 创建日期:  2017/8/31 10:43  <br/>
 * 作者:    wdz
 */
@Service
public class StoreUserServiceImpl extends BaseServiceImpl  implements StoreUserService{
    @Autowired
    StoreRoleService resortsRoleService;
    @Autowired
    StoreAuthorityService resortsAuthorityService;
    @Autowired
    DataDictionaryService dataDictionaryService;

    @Autowired
    SystemConfigService systemConfigService;

    /**
     * 修改用户最后登录信息
     *
     * @param id
     * @param ip
     * @param date
     * @return
     */
    public RestMessageModel updateLoginInfo(String id, String ip, Date date) {
        RestMessageModel model = new RestMessageModel();
        StoreUser resortsUser = findById(id);
        if (resortsUser == null) {
            model.setErrorMessage("未查找到用户信息");
            return model;
        }
        resortsUser.setLastLoginIp(ip);
        resortsUser.setLastLoginTime(date);
        resortsUser.setIsResetPassword(CommonEnum.YesOrNo.NO.getCode());// 默认设置未重置密码
        resortsUser.setModifier(LoginStoreUserUtil.getUserId());
        return this.updateObject(id, resortsUser, StoreUser.table);
    }

    /**
     * 密码重置
     *
     * @param id
     * @return
     */
    public RestMessageModel resetPassWord(String id, String loginUserId) {
        RestMessageModel model = new RestMessageModel();
        StoreUser resortsUser = findById(id);
        if (resortsUser == null) {
            model.setErrorMessage("未查找到用户信息");
            return model;
        }

        if (loginUserId.equalsIgnoreCase(id)) {
            model.setErrorMessage("不能重置当前登录用户");
            return model;
        }

        if (resortsUser.getIsAdmin().equalsIgnoreCase(CommonEnum.YesOrNo.YES.getCode())) {
            model.setErrorMessage("管理员密码不能重置");
            return model;
        }

        return commonRestPassWord(resortsUser, null);
    }

    /**
     * 修改密码
     *
     * @param newPassWord
     * @param oldPassWord
     * @return
     */
    public RestMessageModel updatePassWord(String newPassWord,
                                           String oldPassWord) {
        RestMessageModel model = new RestMessageModel();
        StoreUser resortsUser = LoginStoreUserUtil.getStoreUser();

        // 根据密码盐值， 解码
        byte[] salt = EncodeUtils.hexDecode(resortsUser.getSalt());
        byte[] hashPassword = Digests.sha1(oldPassWord.getBytes(), salt,
                StoreUser.HASH_INTERATIONS);
        // 密码 数据库中密码
        String validatePassWord = EncodeUtils.hexEncode(hashPassword);

        if (!resortsUser.getPassword().equals(validatePassWord)) {// 如果不相等
            model.setErrorMessage("原密码错误，请重新输入");
            return model;
        }

        String[] str = PassWordUtil.getSysUser(newPassWord, null);

        resortsUser.setSalt(str[1]);
        resortsUser.setPassword(str[2]);
        resortsUser.setModifier(LoginStoreUserUtil.getUserId());
        model = this.updateObject(resortsUser.getId(), resortsUser,
                StoreUser.table);

        if (model.isSuccess()) {
            // Subject subject = SecurityUtils.getSubject();
            // subject.logout();
        }

        return model;

    }

    // public static void main(String[] args)
    // {
    // String[] resortsUser = PassWordUtil.getStoreUser("111111", null);
    //
    // for (String s : resortsUser)
    // {
    // Storetem.out.println(s);
    // }
    //
    //
    // }

    /**
     * 重置密码公用方法
     *
     * @param resortsUser
     * @return
     */
    public RestMessageModel commonRestPassWord(StoreUser resortsUser,
                                               String passWord) {
        RestMessageModel model = new RestMessageModel();
        // 密码重置
        String[] str = PassWordUtil.getStoreUser(passWord, null);
        passWord = str[0];
        resortsUser.setSalt(str[1]);
        resortsUser.setPassword(str[2]);

        // 设置是否重置密码标记位
        resortsUser.setIsResetPassword(CommonEnum.YesOrNo.YES.getCode());
        // 获取密码失效时间设置项
        SystemConfig timeConfig = systemConfigService
                .findByCode(CommonConstant.SYSCONFIG_LOOK_PASSWORD_TIME);

        if (timeConfig == null) {
            model.setErrorMessage("未查找到密码失效时间设置项");
            return model;
        }

        String times = timeConfig.getValue();
        if (StringUtils.isEmpty(times)) {
            model.setErrorMessage("未获取到密码失效时间");
            return model;
        }

        int timeNum = 0;
        try {
            timeNum = Integer.valueOf(times);
        } catch (Exception e) {
            model.setErrorMessage("密码失效时间转换错误，请重新设置");
            return model;
        }

        Date validateTime = null;
        // 邮箱提示失效时间，单位小时
        int timeEmail;
        // 此时设置默认2天失效
        if (timeNum <= 0) {
            validateTime = DateTimeUtil.nowTimeAfterMinute(2 * 24 * 60);

            timeEmail = 48;
        } else {
            validateTime = DateTimeUtil.nowTimeAfterMinute(timeNum);

            timeEmail = timeNum / 60;
        }
        // 设置密码失效时间
        resortsUser.setPasswordInvalidTime(validateTime);
        resortsUser.setModifier(LoginStoreUserUtil.getUserId());
        model = this.updateObject(resortsUser.getId(), resortsUser,
                StoreUser.table);
        if (!model.isSuccess()) {
            model.setErrorMessage("更新用户密码信息错误");
            return model;
        }
        // 获取密码服务器配置信息
        SystemConfig systemConfig = systemConfigService
                .findByCode(CommonConstant.SYSCONFIG_EMAILCONFIGKEY);

        if (systemConfig == null) {
            model.setErrorMessage("没有查找到系统配置项");
            return model;
        }

        EmailModel emailModel = null;
        if (!StringUtils.isEmpty(systemConfig.getValue())) {
            JSONObject temp = (JSONObject) JSONObject.parse(systemConfig
                    .getValue());
            emailModel = JSONObject.toJavaObject(temp, EmailModel.class);
        }
        emailModel.setToEmail(resortsUser.getMail());
//		emailModel.setSubject(EmailTemplateConfig.getStoreRePassSubject());
//		emailModel.setCentent(MessageFormat.format(
//				EmailTemplateConfig.getStoreRePassContent(), new Object[] {
//						resortsUser.getUserName(), passWord, timeEmail }));
        emailModel.setCentent(MessageFormat.format(
                "用户名：{0}，密码：{1}", new Object[] {
                        resortsUser.getUserName(), passWord, timeEmail }));
        // 发送邮件结束
        model = this.sendEmail(emailModel);
        if (!model.isSuccess()) {
            model.setErrorMessage("发送邮件错误");
            return model;
        }
        return model;

    }

    /**
     * 唯一性校验 唯一 返回true，不唯一，返回false
     *
     * @param userName
     * @param id
     * @return
     */
    public boolean uniqueUserName(String userName, String id) {
        RequestExample example = new RequestExample(1, 1);
        RequestExample.Param param = example.createParam();
        RequestExample.Criteria cri = example.create();
        param.addTerm("user_name", userName);
        cri.getMust().add(param);
        if (StringUtils.isNotEmpty(id)) {// 如果不为空，那么不查询自己的
            RequestExample.Param notParam = example.createParam();
            notParam.addTerm("id", id);
            cri.getMustNot().add(notParam);
        }
        List<StoreUser> list = this.queryList(StoreUser.table, example,
                StoreUser.class, "id");

        if (list != null && list.size() > 0)
            return false;
        else
            return true;

    }

    /**
     * 根据用户名查找
     *
     * @param userName
     * @param
     * @return
     */
    public StoreUser queryUserName(String userName) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("user_name", userName);
        data.put("is_deleted",CommonEnum.YesOrNo.NO.getCode());
        return this.queryOne(StoreUser.table, data, StoreUser.class);

    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    public StoreUser findById(String id) {
        return this.queryOne(id, StoreUser.table, StoreUser.class);
    }

    /**
     * 保存 此处要处理密码加盐，初始密码，发送邮件，是否首次登录标记
     *
     * @param resortsUser
     * @return
     */
    public RestMessageModel save(StoreUser resortsUser,String isAdmin,String creatorId) {

        RestMessageModel model = new RestMessageModel();
        StoreUser tem = queryUserName(resortsUser.getUserName());
        if (tem != null) {
            model.setErrorMessage("已经存在此用户名");
            return model;
        }

        // 获取初始密码
        String[] res = PassWordUtil.getStoreUser(null, null);
        resortsUser.setSalt(res[1]);
        resortsUser.setPassword(res[2]);

        // 设置未激活
        resortsUser.setIsActivated(CommonEnum.YesOrNo.NO.getCode());
        // 设置否管理员
        resortsUser.setIsAdmin(isAdmin);
        // 设置默认首次登录
        resortsUser.setIsFirstLogin(CommonEnum.YesOrNo.YES.getCode());
        // 设置是否是密码重置
        resortsUser.setIsResetPassword(CommonEnum.YesOrNo.NO.getCode());

        // // 设置是否锁定,初始默认都不锁定
        // resortsUser.setIsLock(YesOrNo.NO.getCode());
       resortsUser.setCreator(creatorId);

        model = this.saveObject(resortsUser, StoreUser.table);
        if (!model.isSuccess()) {
            model.setErrorMessage("新增用户失败");
            return model;
        }
        // 发送邮件
        SystemConfig systemConfig = systemConfigService
                .findByCode(CommonConstant.SYSCONFIG_EMAILCONFIGKEY);

        if (systemConfig == null) {
            model.setErrorMessage("没有查找到系统配置项");
            return model;
        }

        EmailModel emailModel = null;
        if (!StringUtils.isEmpty(systemConfig.getValue())) {
            JSONObject temp = (JSONObject) JSONObject.parse(systemConfig
                    .getValue());
            emailModel = JSONObject.toJavaObject(temp, EmailModel.class);
        }
        emailModel.setToEmail(resortsUser.getMail());

        emailModel.setCentent(MessageFormat.format(
                "用户名：{0}，密码：{1}", new Object[] {
                        resortsUser.getUserName(), res[0] }));

        // 发送邮件结束
        model = this.sendEmail(emailModel);
        if (!model.isSuccess()) {
            model.setErrorMessage("发送密码邮件错误");
            return model;
        }
        return model;

    }

    /**
     * 更新
     *
     * @param resortsUser
     * @return
     */
    public RestMessageModel update(StoreUser resortsUser, String loginUserId) {
        StoreUser old = findById(resortsUser.getId());
        RestMessageModel model = new RestMessageModel();
        if (old == null) {
            model.setErrorMessage("没有查询到对象");
            return model;
        }

        if (loginUserId.equalsIgnoreCase(resortsUser.getId())) {
            model.setErrorMessage("不能修改当前登录用户");
            return model;
        }

        if (CommonEnum.YesOrNo.YES.getCode().equalsIgnoreCase(old.getIsAdmin())) {
            model.setErrorMessage("不能对管理员修改操作");
            return model;
        }

        // 根据实际情况补充
        old.setNickName(resortsUser.getNickName());
        old.setName(resortsUser.getName());
        old.setMail(resortsUser.getMail());
        old.setSex(resortsUser.getSex());
        old.setIsLock(resortsUser.getIsLock());
        old.setModifier(LoginStoreUserUtil.getUserId());
        return this.updateObject(resortsUser.getId(), old, StoreUser.table);
    }

    @Override
    public RestMessageModel updateScenic(StoreUser resortsUser) {
        StoreUser old = findById(resortsUser.getId());
        RestMessageModel model = new RestMessageModel();
        if (old == null) {
            model.setErrorMessage("没有查询到对象");
            return model;
        }
        // 根据实际情况补充
        old.setNickName(resortsUser.getNickName());
        old.setName(resortsUser.getName());
        old.setMail(resortsUser.getMail());
        old.setSex(resortsUser.getSex());
        old.setIsLock(resortsUser.getIsLock());
        old.setModifier(LoginSysUserUtil.getUserId());
        return this.updateObject(resortsUser.getId(), old, StoreUser.table);
    }

    /**
     * 更新 最后登录信息
     *
     * @param id
     * @param ip
     * @param time
     * @return
     */
    public RestMessageModel updateLastLogin(String id, String ip, Date time) {
        StoreUser old = findById(id);
        // 根据实际情况补充
        old.setLastLoginIp(ip);
        old.setLastLoginTime(time);
        old.setModifier(LoginStoreUserUtil.getUserId());
        return this.updateObject(id, old, StoreUser.table);
    }

    public RestMessageModel deleteUsr(String id, String loginUserId) {
        RestMessageModel model = new RestMessageModel();
        if (id == null) {
            model.setSuccess(false);
            model.setErrorMessage("id不能为空");
        } else {

            StoreUser old = findById(id);
            if (old == null) {
                model.setErrorMessage("没有查询到对象");
                return model;
            }

            if (loginUserId.equalsIgnoreCase(id)) {
                model.setErrorMessage("不能删除当前登录用户");
                return model;
            }

            if (CommonEnum.YesOrNo.YES.getCode().equalsIgnoreCase(old.getIsAdmin())) {
                model.setErrorMessage("不能对管理员进行删除操作");
                return model;
            }

            model = deleteById(id, StoreUser.table);
            RequestExample example = new RequestExample(500, 1);
            RequestExample.Param param = example.createParam().addTerm("is_deleted", "N")
                    .addTerm("user_id", id);
            example.create().getMust().add(param);
            List<StoreUserRole> uRoles = queryList(StoreUserRole.table, example,
                    StoreUserRole.class);
            if (uRoles != null && uRoles.size() > 0) {
                String[] ids = new String[uRoles.size()];
                for (int i = 0; i < uRoles.size(); i++) {
                    if (uRoles.get(i).getId() != null) {
                        ids[i] = uRoles.get(i).getId();
                    }
                }
                model = deleteByIds(ids, StoreUserRole.table);
            }
        }
        return model;
    }

    /**
     * 根据用户主键，是否有效，查找所拥有的权限
     *
     * @param userId
     * @param enable
     * @return
     */
    public List<StoreAuthority> queryAuthority(String userId, String enable) {
        List<StoreAuthority> list = new ArrayList<StoreAuthority>();
        List<StoreRole> listRole = resortsRoleService.queryByUserId(userId,
                enable);
        if (listRole == null || listRole.size() == 0){
            return list;
        }


        List<StoreAuthority> temp = null;
        for (StoreRole role : listRole) {
            temp = resortsAuthorityService.queryByRoleId(role.getId(), enable);
            if (temp != null && temp.size() > 0) {
                for (StoreAuthority auth : temp) {
                    if (list.contains(auth)){
                        continue;// 如果存在，那么去除
                    }

                    list.add(auth);
                }
            }
        }
        return list;
    }

    /**
     *
     * @param pageUtil
     * @param user
     * @return
     */
    public PageUtil findShowListByPage(PageUtil pageUtil, StoreUser user) {
        StringBuffer sql = new StringBuffer().append("select *  from ")
                .append(StoreUser.table).append("  where is_deleted='N' ");
        // 条件查询
        if (null != user) {
            if (StringUtils.isNotEmpty(user.getName())) {
                sql.append("and name like '%" + user.getName() + "%'");
            }
            if (StringUtils.isNotEmpty(user.getIsLock())) {
                sql.append("and is_lock='" + user.getIsLock() + "'");
            }

            if (StringUtils.isNotEmpty(user.getStoreId())) {
                sql.append("and store_id='" + user.getStoreId() + "'");
            }
        }

        sql.append(" order by sort ");
        pageUtil = querySql(sql.toString(), pageUtil, StoreUser.class);
        //pagerUtil.setRoot((List<?>) JSONUtil.beanToMap(pagerUtil.getRoot()));

        if (pageUtil.getCount() > 0) {
            List<StoreUser> list = (List<StoreUser>) pageUtil.getRoot();
            Map<String, String> spotMap = new HashMap<String, String>();
            DataDictionary data = null;

            pageUtil.setRoot((List<?>) JSONUtil.beanToMap(list));
        }
        return pageUtil;

    }

    @Override
    public boolean checkIdExist(String id) {
        return checkIdExist(id, StoreUser.table, StoreUser.class);
    }

}

