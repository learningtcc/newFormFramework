package com.drore.service;

import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.model.StreetCultureInfo;
import com.google.gson.JsonObject;

/**
 * Created by wangl on 2017/9/1 0001.
 */
public interface StreetCultureService {

    public RestMessage cultureList(Integer page_size,Integer current_page,JsonObject params);

    public RestMessage addCulture(StreetCultureInfo streetCultureInfo);

    public RestMessage setTop(String id);

    public RestMessage delCulture(String id);

    public RestMessage setIsUsing(String id,String status);

    public RestMessage detail(String id);


    /**
     * 功能描述：api调用使用，计算点击率                  <br/>
     * 作    者：wdz                         <br/>
     * 创建时间：2017/9/6  10:25           <br/>
     * 实现逻辑：详述实现逻辑                 <br/>
     * 修 改 人：                            <br/>
     * 修改时间：                            <br/>
     * 修改说明：                            <br/>
     * @param id  对应主键
     * @param memberId  会员主键
     * @param memberName  会员名称
     * @param name  对应名称或者主题
     */

    StreetCultureInfo queryDetailApiById(String id, String name, String memberId, String memberName);

}
