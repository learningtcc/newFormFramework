package com.drore.controller.store;

import com.alibaba.fastjson.JSONObject;
import com.drore.domain.store.StoreUserLoginInOut;
import com.drore.service.store.StoreUserLoginInOutService;
import com.drore.util.JSONObjResult;
import com.drore.util.PageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 */
@Controller
@RequestMapping("/userLoginInOut/")
public class StoreUserLoginInOutController {
    private Logger log = LoggerFactory.getLogger(StoreUserLoginInOutController.class);
    @Autowired
    private StoreUserLoginInOutService userLoginInOutService;

    @PostMapping("queryListByPage")
    @ResponseBody
    public JSONObject queryListByPage(PageUtil pageUtil, StoreUserLoginInOut userLoginInOut) {
        log.info("分页查询");
        try {
            pageUtil = userLoginInOutService.findByPage(pageUtil,userLoginInOut);
            return JSONObjResult.toJSONObj(pageUtil, true, "");
        } catch (Exception e) {
            log.error("分页查询异常" + e);
            return JSONObjResult.toJSONObj("分页查询失败");
        }
    }
}
