package com.drore.service.impl;

import com.drore.cloud.sdk.client.CloudQueryRunner;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.domain.Pagination;
import com.drore.cloud.sdk.domain.util.RequestExample;
import com.drore.model.InteractiveContent;
import com.drore.model.InteractiveEvaluation;
import com.drore.model.InteractiveTheme;
import com.drore.model.MessageInfo;
import com.drore.service.ImageInfoService;
import com.drore.service.InteractiveService;
import com.drore.service.MessageService;
import com.drore.util.PageUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhangh on 2017/9/4 0001.
 */

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private CloudQueryRunner run;

    @Override
    public RestMessage saveMessage(MessageInfo messageInfo) {
        return run.insert(MessageInfo.table, messageInfo);
    }

}