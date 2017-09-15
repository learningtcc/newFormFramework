package com.drore.service;

import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.model.InteractiveContent;
import com.drore.model.InteractiveTheme;
import com.drore.model.ThemeActivities;
import com.drore.util.PageUtil;

/**
 * Created by zhangh on 2017/9/4 0001.
 */
public interface InteractiveService {

    /**
     * 获取互动主题列表
     * @param page_size
     * @param current_page
     * @return
     */
    public PageUtil getInteractiveThemeList(Integer page_size, Integer current_page);

    /**
     * 新增互动主题
     * @param interactiveTheme
     * @return
     */
    public RestMessage saveInteractiveTheme(InteractiveTheme interactiveTheme);

    /**
     * 删除互动主题
     * @param id
     * @return
     */
    public RestMessage  deleteInteractiveTheme(String id);

    /**
     * 修改互动主题
     * @param interactiveTheme
     * @return
     */
    public RestMessage updateInteractiveTheme(InteractiveTheme interactiveTheme);

    /**
     * 获取互动主题
     * @param id
     * @return
     */
    public RestMessage queryInteractiveTheme(String id);

    /**
     * 获取互动内容列表
     * @param content
     * @param startTime
     * @param endTime
     * @param page_size
     * @param current_page
     * @return
     */
    public PageUtil getInteractiveContentList(InteractiveContent content, String startTime, String endTime, Integer page_size, Integer current_page);


    /**
     * 获取互动内容
     * @param id
     * @return
     */
    public RestMessage queryInteractiveContent(String id);

    /**
     * 获取互动内容评价列表
     * @param page_size
     * @param current_page
     * @return
     */
    public PageUtil getInteractiveEvaluationList(String contentId,Integer page_size, Integer current_page);

}
