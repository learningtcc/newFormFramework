package com.drore.model;

import com.drore.cloud.sdk.domain.SystemModel;
import com.google.gson.annotations.SerializedName;

/**
 * 消息表
 * Created by zhangh on 2017/09/15 0001.
 */
public class MessageInfo extends SystemModel{
    public  static String table = "message";

    private String  title;

    private String introduction;

    private String content;

    @SerializedName("is_read")
    private String isRead;

    private String time;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
