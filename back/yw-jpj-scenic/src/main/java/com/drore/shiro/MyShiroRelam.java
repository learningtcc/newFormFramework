package com.drore.shiro;

import com.drore.cloud.sdk.domain.uc2.UcMenu;
import com.drore.cloud.sdk.domain.uc2.UcUserInfo;
import com.drore.domain.sys.SysUser;
import com.drore.enums.CommonEnum;
import com.drore.exception.InvalidPassWordException;
import com.drore.service.sys.SysUserService;
import com.drore.util.Digests;
import com.drore.util.EncodeUtils;
import com.drore.util.captcah.CaptchaServlet;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2017<br/>
 * 说明:     <br/>
 * 项目名称: cloud-dataCenter <br/>
 * 创建日期:  2017/2/28 18:08  <br/>
 * 作者:    wdz
 */
@Component("myShiroRelam")
public class MyShiroRelam extends AuthorizingRealm {
    @Autowired
    private SysUserService sysUserService;

    /**
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用,负责在应用程序中决定用户的访问控制的方法
     * 此处只有数据库配置了菜单访问的时候才验证，否则不验证
     */

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(
            PrincipalCollection principalCollection) {

        // 获取验证的对象
        if (principalCollection == null) {
            throw new AuthorizationException("Principal对象不能为空");
        }

        UcUserInfo ucUserInfo = (UcUserInfo) principalCollection
                .fromRealm(getName()).iterator().next();
        // 到数据库查是否有此对象
        if (ucUserInfo != null) {
            // 权限信息对象info,用来存放查出的用户的所有的角色（role）及权限（permission）
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();


            // 获取用户对应的权限
            Set<String> permissions = new HashSet<String>();
           // List<UcMenu> menus =  new UcBuilderImpl().getUcTwoMenuByToken(ucUserInfo.getToken(), DefaultProperties.getValueByKey(DefaultProperties.systemCode));
            List<UcMenu> menus = null;
            if (menus != null)
                for (UcMenu ucMenu : menus) {
                    permissions.add(ucMenu.getUrl());// 此系统暂时没有用角色名称去区分登录信息,故此处name没有处理
                }
            System.out.println("拥有的权限==" + permissions);
            info.setStringPermissions(permissions);

            return info;
        }
        return null;

    }

    /**
     * 登录的时候调用 , 进行鉴权但缓存中无用户的授权信息时调用. //登录信息和用户验证信息验证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken authenticationToken)
            throws AuthenticationException {
        // UsernamePasswordToken对象用来存放提交的登录信息
        UsernamePasswordCaptchaToken token = (UsernamePasswordCaptchaToken) authenticationToken;





        // 增加判断验证码逻辑
        String captcha = token.getCaptcha();
        String exitCode = (String) SecurityUtils.getSubject().getSession()
                .getAttribute(CaptchaServlet.KEY_CAPTCHA);
		/*if (null == captcha || !captcha.equalsIgnoreCase(exitCode)) {
			throw new CaptchaException("验证码错误");
		}*/
        // 查出是否有此用户
        SysUser sysUser =  sysUserService.queryUserName(token.getUsername());

        if (sysUser == null) {
            throw new UnknownAccountException("账号或者密码不正确");// 没找到帐号
        }



        // 获取密码盐值 系统存的
        String pswdSalt = sysUser.getSalt();
        // 用户填写的登录密码
        String passWord = String.valueOf(token.getPassword());
        // 根据密码盐值， 解码
        byte[] salt = EncodeUtils.hexDecode(pswdSalt);
        byte[] hashPassword = Digests.sha1(passWord.getBytes(), salt,
                SysUser.HASH_INTERATIONS);
        // 密码 数据库中密码
        String validatePassWord = EncodeUtils.hexEncode(hashPassword);
        // 获取存在系统中的密码
        String sysPassWord = sysUser.getPassword();
        if (validatePassWord.equals(sysPassWord)) {// 如果密码正确

            //增加密码失效判断时间
            if(CommonEnum.YesOrNo.YES.getCode().equalsIgnoreCase(sysUser.getIsResetPassword())){//如果是重置密码，
                Date invalidTime = sysUser.getPasswordInvalidTime();//获取密码失效时间
                if(invalidTime.getTime()<(new Date()).getTime()){
                    throw new InvalidPassWordException("密码已经失效");
                }
            }


            //判断是否锁定
            String isLock = sysUser.getIsLock();
            if (CommonEnum.YesOrNo.YES.getCode().equalsIgnoreCase(isLock)) {// 如果为0表示锁定
                throw new LockedAccountException("账号被锁定");// 没找到帐号
            } else {
                // 重新把验证通过的密码放置到token中
                char[] st = validatePassWord.toCharArray();
                token.setPassword(st);
                return new SimpleAuthenticationInfo(sysUser, sysPassWord,
                        ByteSource.Util.bytes(sysUser.getSalt()), getName());
            }
        } else
            throw new UnknownAccountException("账号或者密码不正确");// 没找到帐号

    }

}