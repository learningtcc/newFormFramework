package com.drore.service.sys;

import com.drore.domain.sys.SysAuthority;
import com.drore.domain.sys.SysUser;
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
public interface SysUserService {

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
    public RestMessageModel commonRestPassWord(SysUser resortsUser,
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
    public SysUser queryUserName(String userName);


    public SysUser findById(String id);

    /**
     * 保存 此处要处理密码加盐，初始密码，发送邮件，是否首次登录标记
     *
     * @param resortsUser
     * @return
     */
    public RestMessageModel save(SysUser resortsUser);


    /**
     * 更新
     *
     * @param resortsUser
     * @return
     */
    public RestMessageModel update(SysUser resortsUser, String loginUserId);


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
    public List<SysAuthority> queryAuthority(String userId, String enable);


    /**
     *
     * @param pageUtil
     * @param user
     * @return
     */
    public PageUtil findShowListByPage(PageUtil pageUtil, SysUser user);


    public boolean checkIdExist(String id);
}
