package com.drore.cloud.service;

import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.domain.Pagination;

import java.util.Map;

/**
 * Created by 仁杰 on 2017/9/7
 */
public interface UserService {



    /**
     * 用户收藏
     * @param commodity_id  店铺/商品id
     * @param collection_type   收藏类型
     * @param is_collection 是否收藏
     * @return
     */
    RestMessage collection(String commodity_id,String collection_type,String is_collection);


    /**
     * 我的收藏
     * @param collection_type 收藏类型
     * @param current_page
     * @param page_size
     * @return
     */
    Pagination<Map> userCollection(String collection_type,Integer current_page,Integer page_size);

    /**
     * 个人信息
     * @return
     */
    RestMessage userInfo();


    /**
     * 修改个人信息—昵称
     * @param nickName
     * @return
     */
    RestMessage updateUserNickName(String nickName);

    /**
     * 修改个人信息—手机
     * @param tel
     * @return
     */
    RestMessage updateUserTel(String tel);
}
