/*
* Copyright (C) 2016 Zhejiang DRORE Technology CO.,LTD.
* All rights reserved.
* Official Web Site: http://www.drore.com.
* Developer Web Site: http://open.drore.com.
*/
package com.drore.model;

import com.drore.cloud.sdk.domain.SystemModel;
import com.drore.cloud.sdk.exception.IllegalRequestParametersException;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.StringUtils;

/**
 * 主题活动表
 * Created by zhangh on 2017/9/1 0001.
 */

public class ThemeActivities extends SystemModel {

    public static String table="theme_activities";

    private String title;

    private String address;

    private String type;

    private String explains;

    @SerializedName("is_newest")
    private String isNewest;

    @SerializedName("is_top")
    private String isTop;

    @SerializedName("top_time")
    private String topTime;

    private int clicks;

    @SerializedName("act_start_time")
    private String startTime;

    @SerializedName("act_end_time")
    private String endTime;

    @SerializedName("is_release")
    private String isRelease;

    @SerializedName("release_time")
    private String releaseTime;

    private String publisher;

    @SerializedName("theme_pic")
    private String themePic;

    @SerializedName("is_using")
    private String isUsing;

    @SerializedName("type_name")
    private String typeName;

    public String getTitle() {
        return title;
    }

    public String getAddress() {
        return address;
    }

    public String getType() {
        return type;
    }

    public String getExplains() {
        return explains;
    }

    public String getIsNewest() {
        return isNewest;
    }

    public String getIsTop() {
        return isTop;
    }

    public String getTopTime() {
        return topTime;
    }


    public int getClicks() {
        return clicks;
    }

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getIsRelease() {
        return isRelease;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getThemePic() {
        return themePic;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setExplains(String explains) {
        this.explains = explains;
    }

    public void setIsNewest(String isNewest) {
        this.isNewest = isNewest;
    }

    public void setIsTop(String isTop) {
        this.isTop = isTop;
    }

    public void setTopTime(String topTime) {
        this.topTime = topTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setIsRelease(String isRelease) {
        this.isRelease = isRelease;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setThemePic(String themePic) {
        this.themePic = themePic;
    }

    public String getIsUsing() {
        return isUsing;
    }

    public void setIsUsing(String isUsing) {
        this.isUsing = isUsing;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void validate() {
        if (StringUtils.isBlank(this.title)){
            throw new IllegalRequestParametersException("标题不能为空");
        }
        if (StringUtils.isBlank(this.type)){
            throw new IllegalRequestParametersException("活动类型不能为空");
        }
        if (StringUtils.isBlank(this.address)){
            throw new IllegalRequestParametersException("活动地址不能为空");
        }
        if (StringUtils.isBlank(this.themePic)){
            throw new IllegalRequestParametersException("主图不能为空");
        }

        if (StringUtils.isBlank(this.startTime)){
            throw new IllegalRequestParametersException("活动开始时间不能为空");
        }

        if (StringUtils.isBlank(this.endTime)){
            throw new IllegalRequestParametersException("活动结束时间不能为空");
        }

        if (StringUtils.isBlank(this.explains)){
            throw new IllegalRequestParametersException("活动详情不能为空");
        }

        if (StringUtils.isBlank(this.isRelease)){
            throw new IllegalRequestParametersException("是否发布不能为空");
        }
    }
}
