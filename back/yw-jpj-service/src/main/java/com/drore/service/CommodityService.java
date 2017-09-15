package com.drore.service;

import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.domain.Pagination;
import com.drore.model.CommodityInfo;
import com.drore.model.TeaSpecies;

import java.util.List;

/**
 * Created by wangl on 2017/9/1 0001.
 */
public interface CommodityService {
    
    /**
     * 功能描述：分页查询                  <br/>
     * 作    者：wdz                         <br/>
     * 创建时间：2017/9/4  13:16           <br/>
     * 实现逻辑：详述实现逻辑                 <br/>
     * 修 改 人：                            <br/>
     * 修改时间：                            <br/>
     * 修改说明：                            <br/>
     * ${tags} 
     */
    
    Pagination queryByPage(Pagination pagination,CommodityInfo commodityInfo);
    /**
     * 功能描述：判断是否存在此种类的Id的商品                    <br/>
     * 作    者：wdz                         <br/>
     * 创建时间：2017/9/4  8:38           <br/>
     * 实现逻辑：详述实现逻辑                 <br/>
     * 修 改 人：                            <br/>
     * 修改时间：                            <br/>
     * 修改说明：                            <br/>
     * ${tags} 
     */
    
    boolean exitShelves(String shelvesId);

    /**
     * 功能描述：删除商品，要判断是否被                    <br/>
     * 作    者：wdz                         <br/>
     * 创建时间：2017/9/4  11:03           <br/>
     * 实现逻辑：详述实现逻辑                 <br/>
     * 修 改 人：                            <br/>
     * 修改时间：                            <br/>
     * 修改说明：                            <br/>
     * ${tags}
     */

    RestMessage deleteById(String id);

    /**
     * 功能描述：新增商品操作  此处是商店人员进行操作的                    <br/>
     * 作    者：wdz                         <br/>
     * 创建时间：2017/9/4  8:45           <br/>
     * 实现逻辑：详述实现逻辑                 <br/>
     * 修 改 人：                            <br/>
     * 修改时间：                            <br/>
     * 修改说明：                            <br/>
     * ${tags}
     */

    RestMessage save(CommodityInfo commodityInfo);


    /**
     * 功能描述：修改商品操作  此处是商店人员进行操作的                    <br/>
     * 作    者：wdz                         <br/>
     * 创建时间：2017/9/4  8:45           <br/>
     * 实现逻辑：详述实现逻辑                 <br/>
     * 修 改 人：                            <br/>
     * 修改时间：                            <br/>
     * 修改说明：                            <br/>
     * ${tags}
     */

    RestMessage update(CommodityInfo commodityInfo);

    /**
     * 功能描述：获取商品信息，后台使用的                    <br/>
     * 作    者：wdz                         <br/>
     * 创建时间：2017/9/4  8:53           <br/>
     * 实现逻辑：详述实现逻辑                 <br/>
     * 修 改 人：                            <br/>
     * 修改时间：                            <br/>
     * 修改说明：                            <br/>
     * ${tags} 
     */
    
    CommodityInfo queryDetailById(String id);

    /**
     * 获取信息，原生的，不做任何处理
     * @param id
     * @return
     */
    CommodityInfo queryById(String id);

    /**
     * 功能描述：获取商品信息，api使用的，此处有一个需要计算点击率的                    <br/>
     * 作    者：wdz                         <br/>
     * 创建时间：2017/9/4  8:54           <br/>
     * 实现逻辑：详述实现逻辑                 <br/>
     * 修 改 人：                            <br/>
     * 修改时间：                            <br/>
     * 修改说明：                            <br/>
     * ${tags}
     */

    CommodityInfo queryDetailByIdApi(String id,String name,String memberId,String memberName);


    /**
     * 功能描述：上下架                    <br/>
     * 作    者：wdz                         <br/>
     * 创建时间：2017/9/4  10:55           <br/>
     * 实现逻辑：对应商品主键，对应上架人的主键，可能是景区的可能是商家，此处要注意                <br/>
     * 修 改 人：                            <br/>
     * 修改时间：                            <br/>
     * 修改说明：                            <br/>
     * ${tags} 
     */
    
    RestMessage onOffShelves(String id,String shelvesId);

    /**
     * 功能描述：根据类型查找列表                    <br/>
     * 作    者：zhangh                         <br/>
     * 创建时间：2017/9/11  16:55           <br/>
     * 实现逻辑：                            <br/>
     * 修 改 人：                            <br/>
     * 修改时间：                            <br/>
     * 修改说明：                            <br/>
     * ${tags}
     */

    List<TeaSpecies> queryListByTypeId(String typeId);






}
