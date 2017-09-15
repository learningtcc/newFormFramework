package com.drore.service;

import com.drore.cloud.sdk.common.resp.RestMessage;
import com.google.gson.JsonObject;

/**
 * Created by wangl on 2017/9/1 0001.
 */
public interface MemberService {

    public RestMessage getUserList(Integer page_size,Integer current_page,JsonObject params);

    public RestMessage getMemberAddress(String memberId);

    public RestMessage getMember(String memberId);
}
