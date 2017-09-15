package com.drore.controller.store;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.drore.domain.store.StoreAuthority;
import com.drore.domain.vo.StoreAuthVo;
import com.drore.enums.CommonEnum;
import com.drore.exception.CustomException;
import com.drore.redis.RedisName;
import com.drore.redis.RedisService;
import com.drore.service.store.StoreAuthorityService;
import com.drore.util.CommonConstant;
import com.drore.util.JSONObjResult;
import com.drore.util.RestMessageModel;
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
 * 说明: 系统 权限控制器<br/>
 * 项目名称: guided-assistant-manage <br/>
 * 创建日期: 2016年8月19日 下午12:27:21 <br/>
 * 作者: wdz
 */
@Controller
@RequestMapping("/authority/")
public class StoreAuthorityController {
	private Logger log = LoggerFactory.getLogger(StoreAuthorityController.class);
	@Autowired
	StoreAuthorityService storeAuthorityService;
	@Autowired
	RedisService redisService;

	/**
	 * 删除 已经测试
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
			if (!storeAuthorityService.checkIdExist(id )) {
				return JSONObjResult.toJSONObj("权限不存在");
			}
			RestMessageModel model = storeAuthorityService.deleteById(id );
            if(model.isSuccess())
            	//删除缓存
    			redisService.del(RedisName.authorityThree);

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
	 * 查询权限详情 已经测试
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
			if (!storeAuthorityService.checkIdExist(id )) {
				return JSONObjResult.toJSONObj("权限不存在");
			}
			StoreAuthority storeAuthority = storeAuthorityService.findById(id);
			return JSONObjResult.toJSONObj(storeAuthority, true, "");
		}
		catch(CustomException e){
			return JSONObjResult.toJSONObj(e.getMessage());
		}catch (Exception e) {
			log.error("查询信息异常" + e);
			return JSONObjResult.toJSONObj("查询失败");
		}

	}

	/**
	 * 保存 已经测试
	 * 
	 * @param storeAuthority
	 * @return
	 */
	@PostMapping("save")
	@ResponseBody
	public JSONObject save(StoreAuthority storeAuthority) {
		log.info("保存信息");
		try {

			RestMessageModel restMessageModel = storeAuthorityService
					.save(storeAuthority);
			
			 if(restMessageModel.isSuccess())
			//删除缓存
			redisService.del(RedisName.authorityThree);
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
	 * 校验编号唯一性 已测试
	 * @param code
	 * @param id
     * @return
     */
	@GetMapping("checkCode")
	@ResponseBody
	public JSONObject checkCode(@RequestParam String code, String id) {
		log.info("校验编号唯一性");
		try {
			if (StringUtils.isEmpty(code)) {
				return JSONObjResult.toJSONObj("编号不能为空");
			} else {
				boolean result = storeAuthorityService.uniqueCode(code, id);
				String error = "此用编号不能使用";
				if (result) {
					error = "";
				}
				return JSONObjResult.toJSONObj(null, result, error);
			}
		} 
		catch(CustomException e){
			return JSONObjResult.toJSONObj(e.getMessage());
		}catch (Exception e) {
			log.error("校验编号唯一性 异常" + e);
			return JSONObjResult.toJSONObj("系统异常，请联系管理员");
		}

	}

	/**
	 * 更新
	 * 
	 * @param storeAuthority
	 * @return
	 */
	@PostMapping("update")
	@ResponseBody
	public JSONObject update(StoreAuthority storeAuthority) {

		log.info("修改信息");
		try {
			if (null == storeAuthority
					|| StringUtils.isEmpty(storeAuthority.getId())) {
				return JSONObjResult.toJSONObj("主键不能为空");
			}
			// 如果不存在则返回
			if (!storeAuthorityService.checkIdExist(storeAuthority.getId())) {
				return JSONObjResult.toJSONObj("权限不存在");
			}



			String isEnable = storeAuthority.getIsEnable();
			// 如果禁用，查询是否有子节点，如果有，子节点也要被禁用
			if (CommonConstant.ISNOTENABLE.equals(isEnable)) {
				// 子节点的权限都禁用
				if (!storeAuthorityService.disableChildrenByParentId(storeAuthority
						.getId())) {
					return JSONObjResult.toJSONObj("禁用子节点权限失败");
				}

			} else {
				// 如果是启用看父节点是否被禁用,一级的不需要判断
				if (!CommonConstant.ROOT.equals(storeAuthority.getParentId())) {
					StoreAuthority queryOne = storeAuthorityService.queryOne(
							storeAuthority.getParentId() );

					if (null != queryOne ) {
						if (CommonEnum.YesOrNo.NO.getCode().equals(
								queryOne.getIsEnable())) {
							return JSONObjResult.toJSONObj("父节点已被禁用，请先启用父节点");
						}
					} else {
						return JSONObjResult.toJSONObj("父节点异常");
					}
				}
			}
			RestMessageModel restMessageModel = storeAuthorityService
					.update(storeAuthority);
			return JSONObjResult.toJSONObj(restMessageModel);
		} 
		catch(CustomException e){
			return JSONObjResult.toJSONObj(e.getMessage());
		}catch (Exception e) {
			log.error("修改信息 异常" + e);
			return JSONObjResult.toJSONObj("修改失败");
		}
	}

	/**
	 * 根据父节点查询子节点（非递归）
	 * 
	 * @param parentId
	 * @return
	 */
	@GetMapping("queryByParentId")
	@ResponseBody
	public JSONObject queryByParentId(String parentId) {
		log.info("根据父节点查询");
		try {
			if (StringUtils.isEmpty(parentId)) {
				parentId = CommonConstant.ROOT;
			}
			// 如果不存在则返回
			if (!CommonConstant.ROOT.equals(parentId)
					&& !storeAuthorityService.checkIdExist(parentId )) {
				return JSONObjResult.toJSONObj("父节点不存在");
			}
			List<StoreAuthority> queryByParentId = storeAuthorityService
					.queryByParentId(parentId, null);
			return JSONObjResult.toJSONObj(queryByParentId, true, "");
		} 
		catch(CustomException e){
			return JSONObjResult.toJSONObj(e.getMessage());
		}catch (Exception e) {
			log.error("查询异常" + e);
			return JSONObjResult.toJSONObj("查询失败");
		}
	}

	/**
	 * 根据是否启用查询所有权限
	 * 
	 * @param enable
	 * @return
	 */
	@GetMapping("queryAllAuthByEnable")
	@ResponseBody
	public JSONObject queryAllAuthorityByEnable(@RequestParam String enable) {
		log.info("查询所有可用角色");
		try {
			List<StoreAuthority> authoritys = storeAuthorityService
					.queryAll(enable);
			return JSONObjResult.toJSONObj(authoritys, true, "");
		} 
		catch(CustomException e){
			return JSONObjResult.toJSONObj(e.getMessage());
		}catch (Exception e) {
			log.error("根据角色主键查找权限信息 异常" + e);
			return JSONObjResult.toJSONObj("根据角色主键查找权限失败");
		}
	}

	/**
	 * 递归查询所有角色
	 * 
	 * @param enable
	 * @return
	 */
	@GetMapping("queryTree")
	@ResponseBody
	public JSONObject queryTree(String enable) {
		log.info("查询所有可用角色");
		try {
			List<StoreAuthVo> authoritys = null;
			boolean tag = redisService.exists(RedisName.authorityThree);
			if (tag) {
				String result = redisService.get(RedisName.authorityThree);
				authoritys = JSONArray.parseArray(result, StoreAuthVo.class);
			}
            if(authoritys==null || authoritys.size()==0){
				authoritys =storeAuthorityService. queryAllChildren(CommonConstant.ROOT,
						new ArrayList<StoreAuthVo>(), enable);
				// 设置缓存
				redisService.set(RedisName.authorityThree,
						JSONArray.toJSONString(authoritys));
            }


			return JSONObjResult.toJSONObj(authoritys, true, "");
		} 
		catch(CustomException e){
			return JSONObjResult.toJSONObj(e.getMessage());
		}catch (Exception e) {
			log.error("根据角色主键查找权限信息 异常" + e);
			return JSONObjResult.toJSONObj("根据角色主键查找权限失败");
		}
	}

 
}
