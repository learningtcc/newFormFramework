package com.drore.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.drore.cloud.sdk.domain.util.RequestExample;
import com.drore.cloud.sdk.domain.util.RequestExample.Criteria;
import com.drore.cloud.sdk.domain.util.RequestExample.Param;
import com.drore.constant.CommonConstant;
import com.drore.domain.DataDictionary;
import com.drore.enums.CommonEnum;
import com.drore.exception.CustomException;
import com.drore.service.DataDictionaryService;
import com.drore.util.LoginStoreUserUtil;
import com.drore.util.PageUtil;
import com.drore.util.RestMessageModel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2016<br/>
 * 说明:数据字典服务层 <br/>
 * 项目名称: drore-base <br/>
 * 创建日期: 2016年11月2日 下午3:20:29 <br/>
 * 作者: xwb
 */
@Service
public class DataDictionaryServiceImpl extends BaseServiceImpl implements DataDictionaryService {

    /**
     * 判断编码是否唯一 true 表示唯一，false表示已经存在
     *
     * @param id
     * @param code
     * @return
     */
    public boolean checkUniqueCode(String id, String code) {
        RequestExample example = new RequestExample(1, 1);
        Criteria cri = example.create();
        if (!StringUtils.isEmpty(id)) {
            Param param = example.createParam();
            param.addTerm("id", id);
            cri.getMustNot().add(param);
        }
        Param param = example.createParam();
        param.addTerm("code", code);
        cri.getMust().add(param);

        List<DataDictionary> list = this.queryList(DataDictionary.table,
                example, DataDictionary.class);
        if (list == null || list.size() == 0)
            return true;
        else
            return false;
    }

    /**
     * 新增数据字典
     *
     * @param dataDictionary
     * @return
     */
    public RestMessageModel save(DataDictionary dataDictionary) {
        if (!checkUniqueCode(null, dataDictionary.getCode())) {
            throw new CustomException("已经存在此编号");
        }
        if (StringUtils.isEmpty(dataDictionary.getParentId()))
            dataDictionary.setParentId(CommonConstant.ROOT);// 设置默认顶级
        dataDictionary.setCreator(LoginStoreUserUtil.getUserId());
        return this.saveObject(JSONObject.toJSON(dataDictionary),
                DataDictionary.table);
    }

    /**
     * 更新数据字典 此处code是不允许修改的，否则要出错
     *
     * @param dataDictionary
     * @return
     */
    public RestMessageModel update(DataDictionary dataDictionary) {
        DataDictionary old = findById(dataDictionary.getId());
        old.setDescription(dataDictionary.getDescription());
        old.setIsEnable(dataDictionary.getIsEnable());
        old.setName(dataDictionary.getName());
        old.setValue(dataDictionary.getValue());
        old.setModifier(LoginStoreUserUtil.getUserId());
        return this.updateObject(dataDictionary.getId(),
                JSONObject.toJSON(old), DataDictionary.table);
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    public DataDictionary findById(String id) {
        return this.queryOne(id, DataDictionary.table, DataDictionary.class);
    }

    /**
     * 根据父类，是否启用查询
     *
     * @param parentId
     * @param enable
     * @return
     */
    public List<DataDictionary> findListByParentEnable(String parentId,
                                                       String enable) {
        RequestExample example = new RequestExample(10000, 1);
        Criteria cri = example.create();
        if (!StringUtils.isEmpty(enable)) {
            Param param = example.createParam();
            param.addTerm("is_enable", enable);
            cri.getMust().add(param);
        }
        // 不为空，查询子类
        if (!StringUtils.isEmpty(parentId)) {
            Param param = example.createParam();
            param.addTerm("parent_id", parentId);
            cri.getMust().add(param);
        }
        // 为空查询父类
        else {
            Param param = example.createParam();
            param.addTerm("parent_id", CommonConstant.ROOT);
            cri.getMust().add(param);
        }
        Param param = example.createParam();
        param.addTerm("is_deleted", CommonEnum.YesOrNo.NO.getCode());
        cri.getMust().add(param);
        List<DataDictionary> list = this.queryList(DataDictionary.table, example,
                DataDictionary.class);
        return list;
    }

    /**
     * 根据编码查询
     *
     * @param code
     * @param enable
     * @return
     */
    public List<DataDictionary> findListByCode(String code, String enable) {
        String sql = "select * from " + DataDictionary.table + " where  is_deleted = 'N' ";
        StringBuffer sb = new StringBuffer(sql);
        if (StringUtils.isNotEmpty(code)) {
            sb.append(" and code ='").append(code).append("'");
        }
        if (StringUtils.isNotEmpty(enable)) {
            sb.append(" and is_enable ='").append(enable).append("'");
        }
        PageUtil pager = new PageUtil();

        pager.setPageNo(1);
        pager.setPageSize(1);
        PageUtil querySql = this.querySql(sb.toString(), pager,
                DataDictionary.class);
        if (querySql.isSuccess() && CollectionUtils.isNotEmpty(querySql.getRoot())) {
            DataDictionary dataDictionary = (DataDictionary) querySql.getRoot().get(0);

            if (null != dataDictionary) {
                String parentId = dataDictionary.getId();
                return queryByParentId(parentId, enable);
            }

        }
        return new ArrayList<DataDictionary>();

    }

    /**
     * 根据编码模糊查询
     *
     * @param parentCode
     * @param enable
     * @param child
     * @return
     */
    public List<DataDictionary> findListByFuzzy(String parentCode, String enable, DataDictionary child) {
        String sql = "select * from " + DataDictionary.table + " where  is_deleted = 'N' ";
        StringBuffer sb = new StringBuffer(sql);
        if (StringUtils.isNotEmpty(parentCode)) {
            sb.append(" and code ='").append(parentCode).append("'");
        }
        if (StringUtils.isNotEmpty(enable)) {
            sb.append(" and is_enable ='").append(enable).append("'");
        }
        PageUtil pager = new PageUtil();

        pager.setPageNo(1);
        pager.setPageSize(1);
        PageUtil querySql = this.querySql(sb.toString(), pager,
                DataDictionary.class);
        if (querySql.isSuccess() && CollectionUtils.isNotEmpty(querySql.getRoot())) {
            DataDictionary dataDictionary = (DataDictionary) querySql.getRoot().get(0);

            if (null != dataDictionary) {
                String parentId = dataDictionary.getId();
                return queryByChildNameFuzzy(parentId, child);
            }

        }
        return new ArrayList<DataDictionary>();

    }

    /**
     * 分页查询
     *
     * @param pagerUtil
     * @param dataDictionary
     * @return
     */
    public PageUtil findListByPage(PageUtil pagerUtil, DataDictionary dataDictionary) {

        Map<String, Object> term = new HashMap<String, Object>();
        String parentId = dataDictionary.getParentId();
        if (StringUtils.isEmpty(parentId))
            term.put("parent_id", CommonConstant.ROOT);
        else {
            term.put("parent_id", parentId);
        }
        return this.queryPage(DataDictionary.table, term,
                pagerUtil.getPageNo(), pagerUtil.getPageSize(),
                DataDictionary.class);
    }

    /**
     * 查询所有子节点
     *
     * @param parentId
     * @param enable
     * @return
     */
    public List<DataDictionary> queryByParentId(String parentId, String enable) {
        RequestExample example = new RequestExample(10000, 1);
        Criteria cri = example.create();
        Param param = example.createParam();
        param.addTerm("parent_id", parentId);
        if (StringUtils.isNotEmpty(enable)) {
            param.addTerm("is_enable", enable);
        }
        param.addTerm("is_deleted", CommonEnum.YesOrNo.NO.getCode());

        cri.getMust().add(param);
        // 获取结果
        List<DataDictionary> queryList = queryList(DataDictionary.table,
                example, DataDictionary.class);
        return queryList;
    }

    /**
     * 条件查询所有子节点
     *
     * @param parentId
     * @param child
     * @return
     */
    public List<DataDictionary> queryByChildNameFuzzy(String parentId, DataDictionary child) {
        RequestExample example = new RequestExample(10000, 1);
        Criteria cri = example.create();
        Param param = example.createParam();
        param.addTerm("parent_id", parentId);
        if (StringUtils.isNotEmpty(child.getIsEnable())) {
            param.addTerm("is_enable", child.getIsEnable());
        }
        if (StringUtils.isNotEmpty(child.getName())) {
            param.addFuzzy("name", child.getName());
        }
        if (StringUtils.isNotEmpty(child.getCode())) {
            param.addTerm("code", child.getCode());
        }
        param.addTerm("is_deleted", CommonEnum.YesOrNo.NO.getCode());

        cri.getMust().add(param);
        // 获取结果
        List<DataDictionary> queryList = queryList(DataDictionary.table,
                example, DataDictionary.class);
        return queryList;
    }

    public RestMessageModel deleteById(String id) {
        return super.deleteById(id, DataDictionary.table);
    }

}
