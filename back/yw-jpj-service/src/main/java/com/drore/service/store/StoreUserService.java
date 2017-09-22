package com.drore.service.store;

import com.drore.domain.store.StoreAuthority;
import com.drore.domain.store.StoreUser;
import com.drore.util.PageUtil;
import com.drore.util.RestMessageModel;

import java.util.Date;
import java.util.List;

/**
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2017<br/>
 * 说明:     <br/>
 * 项目名称: yw-jpj-scenic <br/>
 * 创建日期:  2017/8/31 13:53  <br/>
 * 作者:    wdz
 */
public interface StoreUserService {

    /**
     * 修改用户最后登录信息
     *
     * @param id
     * @param ip
     * @param date
     * @return
     */
    public RestMessageModel updateLoginInfo(String id, String ip, Date date);


    /**
     * 密码重置
     *
     * @param id
     * @return
     */
    public RestMessageModel resetPassWord(String id, String loginUserId);


    /**
     * 修改密码
     *
     * @param newPassWord
     * @param oldPassWord
     * @return
     */
    public RestMessageModel updatePassWord(String newPassWord,
                                           String oldPassWord);

    /**
     * 重置密码公用方法
     *
     * @param resortsUser
     * @return
     */
    public RestMessageModel commonRestPassWord(StoreUser resortsUser,
                                               String passWord);

    /**
     * 唯一性校验 唯一 返回true，不唯一，返回false
     *
     * @param userName
     * @param id
     * @return
     */
    public boolean uniqueUserName(String userName, String id);

    /**
     * 根据用户名查找
     *
     * @param userName
     * @param
     * @return
     */
    public StoreUser queryUserName(String userName);


    public StoreUser findById(String id);

    /**
     * 保存 此处要处理密码加盐，初始密码，发送邮件，是否首次登录标记
     *
     * @param resortsUser
     * @return
     */
    public RestMessageModel save(StoreUser resortsUser,String isAdmin,String creatorId);


    /**
     * 功能描述：商家管理员对商家进行修改                    <br/>
     * 作    者：wdz                         <br/>
     * 创建时间：2017/9/5  14:52           <br/>
     * 实现逻辑：详述实现逻辑                 <br/>
     * 修 改 人：                            <br/>
     * 修改时间：                            <br/>
     * 修改说明：                            <br/>
     * ${tags}
     */

    public RestMessageModel update(StoreUser resortsUser, String loginUserId);
    /**
     * 功能描述：景区人员对商家用户进行修改                    <br/>
     * 作    者：wdz                         <br/>
     * 创建时间：2017/9/5  14:52           <br/>
     * 实现逻辑：详述实现逻辑                 <br/>
     * 修 改 人：                            <br/>
     * 修改时间：                            <br/>
     * 修改说明：                            <br/>
     * ${tags}
     */

    public RestMessageModel updateScenic(StoreUser resortsUser);


    /**
     * 更新 最后登录信息
     *
     * @param id
     * @param ip
     * @param time
     * @return
     */
    public RestMessageModel updateLastLogin(String id, String ip, Date time);

    public RestMessageModel deleteUsr(String id, String loginUserId);


    /**
     * 根据用户主键，是否有效，查找所拥有的权限
     *
     * @param userId
     * @param enable
     * @return
     */
    public List<StoreAuthority> queryAuthority(String userId, String enable);


    /**
     *
     * @param pageUtil
     * @param user
     * @return
     */
    public PageUtil findShowListByPage(PageUtil pageUtil, StoreUser user);

    boolean checkIdExist(String id);

    /**
     * 启用禁用
     * @param id
     * @return
     */
    public RestMessageModel disableUser(String id);

}
