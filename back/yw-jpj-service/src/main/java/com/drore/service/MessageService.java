package com.drore.service;

import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.model.MessageInfo;

/**
 * Created by zhangh on 2017/9/15 0001.
 */
public interface MessageService {

    /**
     * 保存消息
     * @param messageInfo
     * @return
     */
    public RestMessage saveMessage(MessageInfo messageInfo);

}
