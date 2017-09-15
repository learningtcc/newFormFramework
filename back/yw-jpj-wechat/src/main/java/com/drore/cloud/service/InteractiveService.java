package com.drore.cloud.service;

import com.drore.cloud.model.InteractiveContent;
import com.drore.cloud.model.InteractiveEvaluation;
import com.drore.cloud.sdk.common.resp.RestMessage;

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
    public RestMessage getInteractiveThemeList(Integer page_size, Integer current_page);

    /**
     * 获取互动内容列表（根据主题id）
     * @param interactivThemeFk
     * @param page_size
     * @param current_page
     * @return
     */
    public RestMessage getInteractiveContentList(String interactivThemeFk, Integer page_size, Integer current_page);

    /**
     * 获取互动内容列表（根据发布人id）
     * @param page_size
     * @param current_page
     * @return
     */
    public RestMessage getInteractiveContentListByPublisher(Integer page_size, Integer current_page);

    /**
     * 获取互动内容详情
     * @param id
     * @param page_size
     * @param current_page
     * @return
     */
    public RestMessage queryInteractiveContent(String id,Integer page_size, Integer current_page);

    /**
     * 保存互动内容评价
     * @param interactiveEvaluation
     * @return
     */
    public RestMessage saveInteractiveEvaluation(InteractiveEvaluation interactiveEvaluation);

    /**
     * 保存互动内容
     * @param interactiveContent
     * @return
     */
    public RestMessage saveInteractiveContent(InteractiveContent interactiveContent);

    /**
     * 获取互动内容详情
     * @param id
     * @param page_size
     * @param current_page
     * @return
     */

    /**
     * 删除互动内容
     * @param id
     * @return
     */
    public RestMessage deleteInteractiveContent(String id);

}
