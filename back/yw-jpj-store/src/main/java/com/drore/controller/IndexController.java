package com.drore.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2016<br/>
 * 说明:登陆首页 <br/>
 * 项目名称: drore-base <br/>
 * 创建日期: 2016年8月16日 上午10:45:24 <br/>
 * 作者: lj
 */
@Api("登录页和首页跳转控制接口")
@Controller
@RequestMapping("/")
public class IndexController
{
	@ApiOperation("跳转到登录页")
	@GetMapping(value = "login")
	public ModelAndView login()
	{
		ModelAndView model = new ModelAndView("login");
		return model;
	}
	@ApiOperation("没有权限")
	@GetMapping(value = "unauthor")
	public ModelAndView unauthor()
	{
		ModelAndView model = new ModelAndView("redirect:login");
		return model;
	}

	@ApiOperation("跳转到首页")
	@GetMapping(value = "index")
	public ModelAndView index(ShiroHttpServletRequest request)
	{
		
		Subject subject = SecurityUtils.getSubject();
		// 设置独立的session会话超时时间
		if(subject==null || subject.getPrincipal()==null){
			return new ModelAndView("login");
		}
		ModelAndView model = new ModelAndView("index");
		return model;
	}
}
