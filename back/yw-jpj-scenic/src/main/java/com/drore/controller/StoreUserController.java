package com.drore.controller;

import com.alibaba.fastjson.JSONObject;
import com.drore.domain.store.StoreUser;
import com.drore.enums.CommonEnum;
import com.drore.exception.CustomException;
import com.drore.redis.RedisService;
import com.drore.service.SystemConfigService;
import com.drore.service.store.*;
import com.drore.util.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 浙江卓锐科技股份有限公司 版权所有 © Copyright 2016<br/>
 * 说明: 系统用户 控制器 <br/>
 * 项目名称: guided-assistant-manage <br/>
 * 创建日期: 2016年8月19日 下午12:25:18 <br/>
 * 作者: wdz
 */

@Controller
@RequestMapping("/storeUser/")
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
     * 保存 此处只能新增超级用户
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
            restMessageModel = storeUserService.save(storeUser, CommonEnum.YesOrNo.NO.getCode(),LoginSysUserUtil.getUserId());
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

            restMessageModel = storeUserService.updateScenic(storeUser);
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

}
