package com.drore.controller.store;

import com.alibaba.fastjson.JSONObject;
import com.drore.constant.SystemConfigConstant;
import com.drore.domain.SystemConfig;
import com.drore.domain.store.*;
import com.drore.enums.CommonEnum;
import com.drore.exception.CustomException;
import com.drore.exception.InvalidPassWordException;
import com.drore.redis.RedisName;
import com.drore.redis.RedisService;
import com.drore.service.SystemConfigService;
import com.drore.service.store.*;
import com.drore.shiro.UsernamePasswordCaptchaToken;
import com.drore.util.*;
import com.drore.util.captcah.CaptchaException;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 浙江卓锐科技股份有限公司 版权所有 © Copyright 2016<br/>
 * 说明: 系统用户 控制器 <br/>
 * 项目名称: guided-assistant-manage <br/>
 * 创建日期: 2016年8月19日 下午12:25:18 <br/>
 * 作者: wdz
 */

@Controller
@RequestMapping("/user/")
public class StoreUserController {
    private Logger log = LoggerFactory.getLogger(StoreUserController.class);
    @Autowired
    StoreUserService storeUserService;
    @Autowired
    StoreRoleService storeRoleService;
    @Autowired
    StoreUserRoleService storeUserRoleService;
    @Autowired
    RedisService redisService;
    @Autowired
    SystemConfigService systemConfigService;
    @Autowired
    StoreUserLoginInOutService storeUserLoginInOutService;
    @Autowired
    StoreAuthorityService storeAuthorityService;

    /**
     * 登录校验
     *
     * @param storeUser
     * @param rememberMe
     * @param request
     * @return
     */
    @PostMapping("loginCheck")
    @ResponseBody
    public JSONObject loginCheck(StoreUser storeUser, boolean rememberMe,
                                 @RequestParam String captcha, HttpServletRequest request) {
        log.info("loginCheck start");
        try {
            // 先判断是不是为空
            if (StringUtils.isEmpty(storeUser.getUserName())
                    || StringUtils.isEmpty(storeUser.getPassword())) {

                return JSONObjResult.toJSONObj("用户名或密码不能为空!");
            }

            Subject subject = SecurityUtils.getSubject();

            String ip = IpUtil.getIpAddr(request);

            UsernamePasswordCaptchaToken token = new UsernamePasswordCaptchaToken(
                    storeUser.getUserName(), storeUser.getPassword().toCharArray(),
                    rememberMe, ip, captcha);
            token.setRememberMe(rememberMe);
            subject.login(token);
            token.clear();// -============此处测试加上
            if (subject.isAuthenticated()) {

                //查询出会员配置的前缀
                SystemConfig systemConfig = systemConfigService.getSysConfigByKeyword(SystemConfigConstant.IDCARD_CONFIG);
                redisService.set(RedisName.idCard_config, JSONObject.toJSONString(systemConfig));

                // 保存登录日志
                storeUser = LoginStoreUserUtil.getStoreUser();
                System.out.println(storeUser);
                // 修改用户的最后登录时间和登录ip

                String userId = storeUser.getId();

                Date now = new Date();
                RestMessageModel model = storeUserService.updateLoginInfo(userId,
                        ip, now);// 修改最后登录信息

                if (model.isSuccess()) {
                    StoreUserLoginInOut inOut = new StoreUserLoginInOut();
                    inOut.setInoutType(CommonEnum.LoginInOut.IN.getValue());
                    inOut.setIp(ip);
                    inOut.setRecordDate(now);
                    inOut.setUserId(userId);
                    model = storeUserLoginInOutService.save(inOut);// 保存登录日志信息
                    if (model.isSuccess()) {
                        return JSONObjResult.toJSONObj(model);
                        // WebUtils.isTrue(request, String.valueOf(success));
                    } else {
                        return JSONObjResult.toJSONObj("保存用户登录日志失败");
                    }
                } else {
                    subject.logout();
                    return JSONObjResult.toJSONObj(model.getErrorMessage());

                }
            } else
                return JSONObjResult.toJSONObj("你认证未通过，请重新登录");

        } catch (CustomException e) {
            return JSONObjResult.toJSONObj(e.getMessage());

        } catch (CaptchaException e) {

            e.printStackTrace();
            return JSONObjResult.toJSONObj("验证码错误");
        } catch (UnknownAccountException e) {
            e.printStackTrace();
            return JSONObjResult.toJSONObj("账号或者密码不正确");
        } catch (InvalidPassWordException e) {
            e.printStackTrace();
            return JSONObjResult.toJSONObj("密码已经失效");
        } catch (LockedAccountException e) {
            e.printStackTrace();
            return JSONObjResult.toJSONObj("帐号已被锁");
        } catch (Exception e) {
            e.printStackTrace();
            return JSONObjResult.toJSONObj("系统异常，请联系管理员");
        }
    }

    /**
     * 重置密码
     *
     * @param id
     * @return
     */
    @GetMapping("resetPassWord")
    @ResponseBody
    public JSONObject resetPassWord(@RequestParam String id) {
        log.info("重置密码");
        try {
            if (StringUtils.isEmpty(id))
                return JSONObjResult.toJSONObj("用户主键不能为空");
            String loginUserId = LoginStoreUserUtil.getUserId();
            if (StringUtils.isEmpty(loginUserId)) {
                return JSONObjResult.toJSONObj("未获取到登录用户信息");
            }
            RestMessageModel model = storeUserService.resetPassWord(id,
                    loginUserId);
            return JSONObjResult.toJSONObj(model);
        } catch (CustomException e) {
            return JSONObjResult.toJSONObj(e.getMessage());
        } catch (Exception e) {
            log.error("重置密码异常" + e);
            return JSONObjResult.toJSONObj("重置密码失败");
        }
    }

    /**
     * 当前登录用户修改密码
     *
     * @param oldPassWord
     * @param newPassWord
     * @return
     */
    @PostMapping("updatePassWord")
    @ResponseBody
    public JSONObject updatePassWord(@RequestParam String oldPassWord,
                                     @RequestParam String newPassWord) {
        log.info("当前登录用户修改密码");
        RestMessageModel model = storeUserService.updatePassWord(newPassWord,
                oldPassWord);
        return JSONObjResult.toJSONObj(model);
    }

    /**
     * 保存用户角色
     *
     * @return
     */
    @PostMapping("saveRole")
    @ResponseBody
    public JSONObject saveRole(@RequestParam String roleIds,
                               @RequestParam String userId) {
        log.info("保存用户角色");
        log.info("角色主键集合 ids=" + roleIds);
        try {
            if (StringUtils.isEmpty(roleIds))
                return JSONObjResult.toJSONObj("角色主键不能为空");
            if (StringUtils.isEmpty(userId))
                return JSONObjResult.toJSONObj("用户主键不能为空");

            StoreUser old = storeUserService.findById(userId);
            if (old == null) {
                return JSONObjResult.toJSONObj("没有查询到对象");
            }
            if (CommonEnum.YesOrNo.YES.getCode().equalsIgnoreCase(old.getIsAdmin())) {
                return JSONObjResult.toJSONObj("不能对管理员进行删除操作");
            }
            String ids[] = roleIds.split(",");
            List<StoreUserRole> list = new ArrayList<StoreUserRole>();
            for (String id : ids) {
                StoreUserRole re = new StoreUserRole();
                re.setRoleId(id);
                re.setUserId(userId);
                list.add(re);
            }
            RestMessageModel model = storeUserRoleService.saveBatch(list, userId);
            return JSONObjResult.toJSONObj(model);
        } catch (CustomException e) {
            return JSONObjResult.toJSONObj(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("异常信息" + e.getMessage());
            return JSONObjResult.toJSONObj("设置权限失败");
        }
    }

    /**
     * 根据用户主键查找所拥有的权限
     *
     * @param userId
     * @param enable
     * @return
     */
    @GetMapping("queryAuthority")
    @ResponseBody
    public JSONObject queryAuthority(@RequestParam String userId,
                                     @RequestParam String enable) {
        log.info("根据用户主键查找所拥有的权限");
        log.info("用户主键集合 ids=" + userId);
        if (StringUtils.isEmpty(userId))
            return JSONObjResult.toJSONObj("用户主键不能为空");
        try {
            List<StoreAuthority> list = storeUserService.queryAuthority(userId,
                    enable);
            return JSONObjResult.toJSONObj(list, true, "");
        } catch (CustomException e) {
            return JSONObjResult.toJSONObj(e.getMessage());
        } catch (Exception e) {
            log.error("异常信息" + e);
            return JSONObjResult.toJSONObj("根据用户主键查找所拥有的权限失败");
        }

    }

    /**
     * 获取当前登录用户信息
     *
     * @return
     */
    @GetMapping("getLoginUserInfo")
    @ResponseBody
    public JSONObject getLoginUserInfo() {
        log.info("获取当前登录用户信息  start");
        StoreUser storeUser = LoginStoreUserUtil.getStoreUser();
        StoreUser result = new StoreUser();
        result.setUserName(storeUser.getUserName());
        List<StoreAuthority> other = new ArrayList<StoreAuthority>();
        List<StoreAuthority> menu = new ArrayList<StoreAuthority>();
        List<StoreAuthority> list = null;
        if (CommonEnum.YesOrNo.YES.getCode().equalsIgnoreCase(storeUser.getIsAdmin())){
            list = storeAuthorityService.queryAll(null);
        }
        else{
            list = storeUserService.queryAuthority(storeUser.getId(),
                    CommonEnum.YesOrNo.YES.getCode());
        }
        if (list != null) {
            for (StoreAuthority storeAuthority : list) {
                if (storeAuthority.getType() == CommonEnum.MenuOrButton.MENU.getCode()){
                    menu.add(storeAuthority);
                }
                else{
                    other.add(storeAuthority);
                }

            }
        }

        result.setMenuArray(menu);
        result.setOtherArray(other);

        return JSONObjResult.toJSONObj(result, true, "");

    }

    /**
     * 根据用户主键查找所拥有的角色
     *
     * @param userId
     * @param enable
     * @return
     */
    @GetMapping("queryRole")
    @ResponseBody
    public JSONObject queryRole(@RequestParam String userId, String enable) {
        log.info("根据用户主键查找所拥有的角色");
        log.info("用户主键 id=" + userId);
        try {
            if (StringUtils.isEmpty(userId))
                return JSONObjResult.toJSONObj("用户主键不能为空");
            List<StoreRole> list = storeRoleService.queryByUserId(userId, enable);
            return JSONObjResult.toJSONObj(list, true, "");
        } catch (CustomException e) {
            return JSONObjResult.toJSONObj(e.getMessage());
        } catch (Exception e) {
            log.error("异常信息" + e);
            return JSONObjResult.toJSONObj("根据用户主键查找所拥有的角色失败");
        }

    }

    /**
     * 功能描述：简述功能 <br/>
     * 作 者： wdz <br/>
     * 创建时间：2016年11月3日 下午5:36:22 <br/>
     * 实现逻辑：详述实现逻辑 <br/>
     * 修 改 人： <br/>
     * 修改时间： <br/>
     * 修改说明： <br/>
     *
     * @param pageUtil
     * @param user
     * @return
     */
    @PostMapping("queryUserList")
    @ResponseBody
    public JSONObject queryUserList(PageUtil pageUtil, StoreUser user) {
        log.info("查询用户列表");
        try {
            user.setStoreId(LoginStoreUserUtil.getStoreId());
            pageUtil = storeUserService.findShowListByPage(pageUtil, user);
        } catch (CustomException e) {
            return JSONObjResult.toJSONObj(e.getMessage());
        } catch (Exception e) {
            log.error("查询用户列表异常" + e);
            return JSONObjResult.toJSONObj("系统异常，请联系管理员");
        }
        return JSONObjResult.toJSONObj(pageUtil, true, "查询成功");
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @GetMapping("removeById")
    @ResponseBody
    public JSONObject delelteUser(@RequestParam String id) {
        log.info("删除用户信息");
        RestMessageModel model = null;
        try {
            if (StringUtils.isEmpty(id)) {
                return JSONObjResult.toJSONObj("主键不能为空");
            }

            String logingUserId = LoginStoreUserUtil.getUserId();
            if (StringUtils.isEmpty(logingUserId)) {
                return JSONObjResult.toJSONObj("未获取到登录用户信息");
            }

            model = storeUserService.deleteUsr(id, logingUserId);
        } catch (CustomException e) {
            return JSONObjResult.toJSONObj(e.getMessage());
        } catch (Exception e) {
            log.error("异常信息" + e);
            return JSONObjResult.toJSONObj("删除异常");
        }
        return JSONObjResult.toJSONObj(model);
    }

    /**
     * 保存 此处只能新增普通用户
     *
     * @param storeUser
     * @return
     */
    @PostMapping("save")
    @ResponseBody
    public JSONObject save(StoreUser storeUser) {
        log.info("保存信息");
        try {
            RestMessageModel restMessageModel;

            String loginUserId = LoginStoreUserUtil.getUserId();
            if (StringUtils.isEmpty(loginUserId)) {
                return JSONObjResult.toJSONObj("未获取到登录用户信息");
            }
            restMessageModel = storeUserService.save(storeUser, CommonEnum.YesOrNo.NO.getCode(),LoginStoreUserUtil.getUserId());
            return JSONObjResult.toJSONObj(restMessageModel);
        } catch (CustomException e) {
            return JSONObjResult.toJSONObj(e.getMessage());
        } catch (Exception e) {
            log.error("保存信息 异常" + e);
            return JSONObjResult.toJSONObj("系统异常，请联系管理员");
        }
    }

    /**
     * 修改
     *
     * @param
     * @return
     */
    @PostMapping("update")
    @ResponseBody
    public JSONObject update(StoreUser storeUser) {
        log.info("修改信息");
        try {
            RestMessageModel restMessageModel;


            String loginUserId = LoginStoreUserUtil.getUserId();
            if (StringUtils.isEmpty(loginUserId)) {
                return JSONObjResult.toJSONObj("未获取到登录用户信息");
            }

            if (storeUser.getId().equalsIgnoreCase(loginUserId))
                return JSONObjResult.toJSONObj("不能对当前登录人进行修改");



            restMessageModel = storeUserService.update(storeUser, loginUserId);
            return JSONObjResult.toJSONObj(restMessageModel);
        } catch (Exception e) {
            log.error("修改信息 异常" + e);
            return JSONObjResult.toJSONObj("系统异常，请联系管理员");
        }

    }

    /**
     * 校验用户名唯一性
     *
     * @param
     * @return
     */
    @GetMapping("checkedUserName")
    @ResponseBody
    public JSONObject checkedUserName(@RequestParam String userName, String id) {
        log.info("校验用户名唯一性");
        try {
            if (StringUtils.isEmpty(userName)) {
                return JSONObjResult.toJSONObj("用户名不能为空");
            } else {
                boolean result = storeUserService.uniqueUserName(userName, id);
                String error = "此用户名不能使用";
                if (result) {
                    error = "";
                }
                return JSONObjResult.toJSONObj(null, result, error);
            }

        } catch (CustomException e) {
            return JSONObjResult.toJSONObj(e.getMessage());
        } catch (Exception e) {
            log.error("校验用户名唯一性 异常" + e);
            return JSONObjResult.toJSONObj("系统异常，请联系管理员");
        }

    }

    /**
     * 查询用户信息
     *
     * @param id
     * @return
     */
    @GetMapping("queryById")
    @ResponseBody
    public JSONObject queryById(@RequestParam String id) {
        log.info("查询信息 id=" + id);
        try {
            if (StringUtils.isEmpty(id)) {
                return JSONObjResult.toJSONObj("主键不能为空");
            }
            // 如果不存在则返回
            if (!storeUserService.checkIdExist(id)) {
                return JSONObjResult.toJSONObj("用户不存在");
            }
            StoreUser user = storeUserService.findById(id);
            return JSONObjResult.toJSONObj(user, true, "");
        } catch (CustomException e) {
            return JSONObjResult.toJSONObj(e.getMessage());
        } catch (Exception e) {
            log.error("查询用户信息异常" + e);
            return JSONObjResult.toJSONObj("查询失败");
        }

    }

    /**
     * 登出 , method = RequestMethod.GET
     *
     * @return
     */
    @GetMapping("logout")
    @ResponseBody
    public JSONObject logout(HttpServletRequest request) {
        log.info("登出 ");
        try {
            Subject subject = SecurityUtils.getSubject();
            if (subject != null && subject.getPrincipals() != null) {
                StoreUser storeUser = (StoreUser) subject.getPrincipal();
                String ip = IpUtil.getIpAddr(request);
                Date now = new Date();
                StoreUserLoginInOut inOut = new StoreUserLoginInOut();
                inOut.setInoutType(CommonEnum.LoginInOut.OUT.getValue());
                inOut.setIp(ip);
                inOut.setRecordDate(now);
                inOut.setUserId(storeUser.getId());
                RestMessageModel model = storeUserLoginInOutService.save(inOut);// 保存登录日志信息
                subject.logout();
                if (model.isSuccess()) {
                    return JSONObjResult.toJSONObj(model);
                } else {
                    return JSONObjResult.toJSONObj("保存用户登录日志失败");
                }
            } else
                return JSONObjResult.toJSONObj(null, true, "没有获取到用户登录信息,自动跳转到登录页");
        } catch (CustomException e) {
            return JSONObjResult.toJSONObj(e.getMessage());
        } catch (Exception e) {
            log.error("登出异常" + e);
            return JSONObjResult.toJSONObj("注销异常");
        }

    }
}
