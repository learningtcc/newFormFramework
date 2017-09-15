package com.drore.controller.sys;

import com.alibaba.fastjson.JSONObject;
import com.drore.domain.sys.SysAuthority;
import com.drore.domain.sys.SysRole;
import com.drore.domain.sys.SysRoleAuthority;
import com.drore.exception.CustomException;
import com.drore.redis.RedisService;
import com.drore.service.sys.SysAuthorityService;
import com.drore.service.sys.SysRoleAuthorityService;
import com.drore.service.sys.SysRoleService;
import com.drore.util.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 浙江卓锐科技股份有限公司 版权所有 © Copyright 2016<br/>
 * 说明: 系统角色控制器 <br/>
 * 项目名称: guided-assistant-manage <br/>
 * 创建日期: 2016年8月19日 下午12:29:03 <br/>
 * 作者: wdz
 */
@Controller
@RequestMapping("/role/")
public class SysRoleController {
	private Logger log = LoggerFactory.getLogger(SysRoleController.class);
	@Autowired
	SysRoleService sysRoleService;
	@Autowired
	RedisService redisService;
	@Autowired
	SysAuthorityService sysAuthorityService;
	@Autowired
	SysRoleAuthorityService sysRoleAuthorityService;

	/**
	 * 删除 已测试
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("removeById")
	@ResponseBody
	public JSONObject removeById(@RequestParam String id) {
		log.info("查询信息 id=" + id);
		try {
			if (StringUtils.isEmpty(id)) {
				return JSONObjResult.toJSONObj("主键不能为空");
			}
			// 如果不存在则返回
			if (!sysRoleService.checkIdExit(id))  {
				return JSONObjResult.toJSONObj("角色不存在");
			}
			RestMessageModel model = sysRoleService.deleteById(id);
			return JSONObjResult.toJSONObj(model);
		} 
		catch(CustomException e){
			return JSONObjResult.toJSONObj(e.getMessage());
		}catch (Exception e) {
			log.error("删除异常" + e);
			return JSONObjResult.toJSONObj("删除失败");
		}

	}

	/**
	 * 查询角色 已测试
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
			if (!sysRoleService.checkIdExit(id)) {
				return JSONObjResult.toJSONObj("角色不存在");
			}
			SysRole sysAuthority = sysRoleService.findById(id);
			return JSONObjResult.toJSONObj(sysAuthority, true, "");
		} 
		catch(CustomException e){
			return JSONObjResult.toJSONObj(e.getMessage());
		}catch (Exception e) {
			log.error("查询异常" + e);
			return JSONObjResult.toJSONObj("查询失败");
		}

	}

	/**
	 * 保存 已测试
	 * 
	 * @param sysRole
	 * @return
	 */
	@PostMapping("save")
	@ResponseBody
	public JSONObject save(SysRole sysRole) {
		log.info("保存信息");
		try {

			RestMessageModel restMessageModel = sysRoleService.save(sysRole);
			return JSONObjResult.toJSONObj(restMessageModel);
		} 
		catch(CustomException e){
			return JSONObjResult.toJSONObj(e.getMessage());
		}catch (Exception e) {
			log.error("保存信息 异常" + e);
			return JSONObjResult.toJSONObj("保存失败");
		}
	}

	/**
	 * 更新
	 * 
	 * @param sysRole
	 * @return
	 */
	@PostMapping("update")
	@ResponseBody
	public JSONObject update(SysRole sysRole) {
		log.info("修改信息");
		try {
			if (StringUtils.isEmpty(sysRole.getId())) {
				return JSONObjResult.toJSONObj("主键不能为空");
			}
			// 如果不存在则返回
			if (!sysRoleService.checkIdExit(sysRole.getId())) {
				return JSONObjResult.toJSONObj("角色不存在");
			}


			RestMessageModel restMessageModel = sysRoleService.update(sysRole);
			return JSONObjResult.toJSONObj(restMessageModel);
		}
		catch(CustomException e){
			return JSONObjResult.toJSONObj(e.getMessage());
		}catch (Exception e) {
			log.error("修改信息 异常" + e);
			return JSONObjResult.toJSONObj("更新失败");
		}
	}

	/**
	 * 
	 * 校验编号唯一性 已测试
	 * 
	 * @param
	 * @return
	 */
	@GetMapping("checkCode")
	@ResponseBody
	public JSONObject checkCode(@RequestParam String code, String id) {
		log.info("校验编号唯一性");
		try {
			if (StringUtils.isEmpty(code))
				return JSONObjResult.toJSONObj("编号不能为空");
			boolean result = sysRoleService.uniqueCode(code, id);
			String error = "此用编号不能使用";
			if (result) {
				error = "";
			}
			return JSONObjResult.toJSONObj(null, result, error);

		}
		catch(CustomException e){
			return JSONObjResult.toJSONObj(e.getMessage());
		}catch (Exception e) {
			log.error("校验编号唯一性 异常" + e);
			return JSONObjResult.toJSONObj("系统异常，请联系管理员");
		}

	}

	/**
	 * 根据角色主键查找权限
	 * 
	 * @param roleId
	 * @return
	 */
	@GetMapping("queryAuthorityByRoleId")
	@ResponseBody
	public JSONObject queryAuthorityByRoleId(@RequestParam String roleId,
                                             String enable) {
		log.info("根据角色主键查找权限");
		try {
			if (StringUtils.isEmpty(roleId)) {
				return JSONObjResult.toJSONObj("角色主键不能为空");
			}
			// 如果不存在则返回
			if (!sysRoleService.checkIdExit(roleId)) {
				return JSONObjResult.toJSONObj("角色不存在");
			}
			List<SysAuthority> rAuthoritys = null ;
			  rAuthoritys = sysAuthorityService.queryByRoleId(
					roleId, enable);

			return JSONObjResult.toJSONObj(rAuthoritys, true, "");
		}
		catch(CustomException e){
			return JSONObjResult.toJSONObj(e.getMessage());
		}catch (Exception e) {
			log.error("根据角色主键查找权限信息 异常" + e);
			return JSONObjResult.toJSONObj("根据角色主键查找权限失败");
		}

	}

	/**
	 * 保存角色 权限主键
	 * 
	 * @param authoritys
	 * @param roleId
	 * @return
	 */
	@PostMapping("saveAuthority")
	@ResponseBody
	public JSONObject saveAuthority(@RequestParam String authoritys,
                                    @RequestParam String roleId) {
		log.info("权限主键集合 ids=" + authoritys);
		try {
			if (StringUtils.isEmpty(authoritys))
				return JSONObjResult.toJSONObj("权限主键集合不能为空");
			if (StringUtils.isEmpty(roleId))
				return JSONObjResult.toJSONObj("角色主键不能为空");

			// 如果不存在则返回
			if (!sysRoleService.checkIdExit(roleId)) {
				return JSONObjResult.toJSONObj("角色不存在");
			}
			String ids[] = authoritys.split(",");

			List<SysRoleAuthority> list = new ArrayList<SysRoleAuthority>();
			for (String id : ids) {
				SysRoleAuthority re = new SysRoleAuthority();
				re.setRoleId(roleId);
				re.setAuthorityId(id);
				list.add(re);
			}
			RestMessageModel model = sysRoleAuthorityService.saveBatch(list,
					roleId);
			return JSONObjResult.toJSONObj(model);
		} 
		catch(CustomException e){
			return JSONObjResult.toJSONObj(e.getMessage());
		}catch (Exception e) {
			log.error("保存角色 权限主键异常");
			return JSONObjResult.toJSONObj("保存失败");
		}
	}

	/**
	 * 根据是否启用查询所有角色
	 * 
	 * @param
	 * @return
	 */
	@PostMapping("queryListByPage")
	@ResponseBody
	public JSONObject queryListByPage(PageUtil pageUtil, SysRole role) {
		log.info("查询所有可用角色");
		try {
			PageUtil roles = sysRoleService
					.findShowListByPage(pageUtil, role);
			return JSONObjResult.toJSONObj(roles, true, "");
		} 
		catch(CustomException e){
			return JSONObjResult.toJSONObj(e.getMessage());
		}catch (Exception e) {
			log.error("根据角色主键查找权限信息 异常" + e);
			return JSONObjResult.toJSONObj("根据角色主键查找权限失败");
		}
	}

	@PostMapping("queryAllRoleByEnable")
	@ResponseBody
	public JSONObject queryAllRoleByEnable(PageUtil pageUtil, SysRole role) {
		log.info("查询所有可用角色");
		try {
			PageUtil roles = sysRoleService
					.findShowListByPage(pageUtil, role);
			return JSONObjResult.toJSONObj(roles, true, "");
		} 
		catch(CustomException e){
			return JSONObjResult.toJSONObj(e.getMessage());
		}catch (Exception e) {
			log.error("根据角色主键查找权限信息 异常" + e);
			return JSONObjResult.toJSONObj("根据角色主键查找权限失败");
		}
	}

}
