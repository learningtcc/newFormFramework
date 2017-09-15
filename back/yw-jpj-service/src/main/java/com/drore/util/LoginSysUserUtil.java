package com.drore.util;

import com.drore.domain.sys.SysUser;

/**
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2017<br/>
 * 说明:   景区登录信息  <br/>
 * 项目名称: yw-jpj-scenic <br/>
 * 创建日期:  2017/8/31 10:58  <br/>
 * 作者:    wdz
 */
public class LoginSysUserUtil {
    public static String getUserId() {
        SysUser sysUser = getSysUser();
        return sysUser.getId();
    }



    public static SysUser getSysUser() {
        SysUser sysUser = new SysUser();
        sysUser.setId("2f34b4995aeb4456ab037f3415579ac2");
        sysUser.setUserName("admin");
        sysUser.setIsAdmin("Y");
        return  sysUser;
        /* Subject subject = SecurityUtils.getSubject();
        Object object = subject.getPrincipal();
        SysUser sysUser = null;
        if (object != null) {
            sysUser = (SysUser) object;
        } else{
            throw new CustomException("无法获取登录用户信息");
        }
        return sysUser;*/
    }
}
