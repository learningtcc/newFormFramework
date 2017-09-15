package com.drore.service.sys;

import com.drore.domain.sys.SysUserLoginInOut;
import com.drore.util.PageUtil;
import com.drore.util.RestMessageModel;

import java.text.ParseException;

/**
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2017<br/>
 * 说明:     <br/>
 * 项目名称: yw-jpj-scenic <br/>
 * 创建日期:  2017/8/31 13:52  <br/>
 * 作者:    wdz
 */
public interface SysUserLoginInOutService {
    /**
     * 保存
     *
     * @param resortsUserLoginInOut
     * @return
     */
    public RestMessageModel save(SysUserLoginInOut resortsUserLoginInOut);

    public SysUserLoginInOut findById(String id);


    /**
     * 分页查询用户登录登出信息
     * @param pageUtil
     * @param userLoginInOut
     * @return
     * @throws ParseException
     */
    public PageUtil findByPage(PageUtil pageUtil,
                               SysUserLoginInOut userLoginInOut) throws ParseException;
}
