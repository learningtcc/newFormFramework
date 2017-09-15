package com.drore.service;

import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.domain.Pagination;
import com.drore.model.StoreInfo;

import java.util.List;

/**
 * 浙江卓锐科技股份有限公司 版权所有 ? Copyright 2017<br/>
 * 说明: 商家信息服务层接口  删除无需要做<br/>
 * 项目名称: 商家信息服务层接口 <br/>
 * 创建日期: 2017/9/4 9:55<br/>
 * 作者: wdz
 */

public interface StoreService {
    /**
     * 功能描述：获取商家信息 原生的                    <br/>
     * 作    者：wdz                         <br/>
     * 创建时间：2017/9/4  17:13           <br/>
     * 实现逻辑：详述实现逻辑                 <br/>
     * 修 改 人：                            <br/>
     * 修改时间：                            <br/>
     * 修改说明：                            <br/>
     * ${tags}
     */

    StoreInfo queryById(String id );
    /**
     * 功能描述：获取详情                   <br/>
     * 作    者：wdz                         <br/>
     * 创建时间：2017/9/4  17:30           <br/>
     * 实现逻辑：详述实现逻辑                 <br/>
     * 修 改 人：                            <br/>
     * 修改时间：                            <br/>
     * 修改说明：                            <br/>
     * ${tags} 
     */
    
    StoreInfo queryDetailById(String id );

    /**
     * 根据当前登录用户商家获取商家详情
     * @return
     */
    StoreInfo queryDetailByStore();


    /**
     * 功能描述：获取详情使用，此处要计算点击率                    <br/>
     * 作    者：wdz                         <br/>
     * 创建时间：2017/9/4  18:21           <br/>
     * 实现逻辑：详述实现逻辑                 <br/>
     * 修 改 人：                            <br/>
     * 修改时间：                            <br/>
     * 修改说明：                            <br/>
     * ${tags}
     * @param id 商品主键
     * @param name 商品名称
     * @param memberId 会员主键
     * @param memberName 会员名称
     */
    
    StoreInfo queryDetailApiById(String id,String name,String memberId,String memberName);

    /**
     * 功能描述：新增商家信息，只有景区才有权限调用                    <br/>
     * 作    者：wdz                         <br/>
     * 创建时间：2017/9/4  17:13           <br/>
     * 实现逻辑：详述实现逻辑                 <br/>
     * 修 改 人：                            <br/>
     * 修改时间：                            <br/>
     * 修改说明：                            <br/>
     * ${tags}
     */

    RestMessage save(StoreInfo storeInfo);

    /**
     * 功能描述：修改商家信息，只有景区才有权限调用                   <br/>
     * 作    者：wdz                         <br/>
     * 创建时间：2017/9/5  14:32           <br/>
     * 实现逻辑：详述实现逻辑                 <br/>
     * 修 改 人：                            <br/>
     * 修改时间：                            <br/>
     * 修改说明：                            <br/>
     * ${tags}
     */

    RestMessage updateScenic(StoreInfo storeInfo);

    /**
     * 功能描述：修改商家信息，只有商家自己才能进行操作                    <br/>
     * 作    者：wdz                         <br/>
     * 创建时间：2017/9/4  17:24           <br/>
     * 实现逻辑：详述实现逻辑                 <br/>
     * 修 改 人：                            <br/>
     * 修改时间：                            <br/>
     * 修改说明：                            <br/>
     * ${tags}
     */

    RestMessage update(StoreInfo storeInfo);

    /**
     * 功能描述：设置置顶热门，只允许有一个，故设置的时候，要更新其它的 景区人员操作                    <br/>
     * 作    者：wdz                         <br/>
     * 创建时间：2017/9/4  18:26           <br/>
     * 实现逻辑：详述实现逻辑                 <br/>
     * 修 改 人：                            <br/>
     * 修改时间：                            <br/>
     * 修改说明：                            <br/>
     * ${tags}
     */

    RestMessage updateHot(String id);


    /**
     * 功能描述：分页查询                    <br/>
     * 作    者：wdz                         <br/>
     * 创建时间：2017/9/4  17:35           <br/>
     * 实现逻辑：详述实现逻辑                 <br/>
     * 修 改 人：                            <br/>
     * 修改时间：                            <br/>
     * 修改说明：                            <br/>
     * ${tags} 
     */
    
    Pagination queryByPage(Pagination pagination,StoreInfo storeInfo);

    /**
     * 功能描述：获取所有的商铺信息                    <br/>
     * 作    者：wdz                         <br/>
     * 创建时间：2017/9/6  16:55           <br/>
     * 实现逻辑：详述实现逻辑                 <br/>
     * 修 改 人：                            <br/>
     * 修改时间：                            <br/>
     * 修改说明：                            <br/>
     * ${tags}
     */

    List<StoreInfo> queryList();

}
