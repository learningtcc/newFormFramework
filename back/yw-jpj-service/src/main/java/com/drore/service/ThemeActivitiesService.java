package com.drore.service;

import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.model.ThemeActivities;
import com.drore.util.PageUtil;

/**
 * Created by zhangh on 2017/9/1 0001.
 */

public interface ThemeActivitiesService {

     /**
     * 获取主题活动列表
      * @param themeActivities
     * @return
     */
    public PageUtil getThemeActivities(ThemeActivities themeActivities, Integer pageSize, Integer pageNo);

    /**
     * 保存主题活动
     * @param themeActivities
     * @return
     */
    public RestMessage saveThemeActivitie(ThemeActivities themeActivities);

    /**
     * 删除主题活动
     * @param id
     * @return
     */
    public RestMessage  deleteThemeActivity(String id);

    /**
     * 修改主题活动
     * @param themeActivities
     * @return
     */
    public RestMessage updateThemeActivity(ThemeActivities themeActivities);

    /**
     * 获取主题活动
     * @param id
     * @return
     */
    public RestMessage queryThemeActivity(String id);

    /**
     * 设置启用禁用状态
     * @param id
     * @param status
     * @return
     */
    public RestMessage setIsUsing(String id,String status);

    /**
     * 设置置顶热门
     * @param id
     * @return
     */
    public RestMessage setTop(String id);


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

    ThemeActivities queryDetailApiById(String id, String name, String memberId, String memberName);

}
