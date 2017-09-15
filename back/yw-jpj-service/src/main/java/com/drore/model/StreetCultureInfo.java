package com.drore.model;

import com.drore.cloud.sdk.domain.SystemModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Created by wangl on 2017/9/1 0001.
 */
public class StreetCultureInfo extends SystemModel {

    public  static String table="street_culture_info";


    private String description;
    private String title;
    private int clicks;

    @SerializedName("is_hot")
    private String isHot;

    @SerializedName("is_using")
    private String isUsing;

    @SerializedName("is_release")
    private String isRelease;

    @SerializedName("video_url")
    private String videoUrl;

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("announcer_time")
    private Date announcerTime;

    @SerializedName("announcer_id")
    private String announcerId;

    @SerializedName("top_time")
    private Date topTime;

    /**
     *图集 ，临时字段
     */
    @Expose(serialize=false)
    private List<String> pics;

    public List<String> getPics() {
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }


    public int getClicks() {
        return clicks;
    }

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }

    public Date getTopTime() {
        return topTime;
    }

    public void setTopTime(Date topTime) {
        this.topTime = topTime;
    }

    public String getIsUsing() {
        return isUsing;
    }

    public void setIsUsing(String isUsing) {
        this.isUsing = isUsing;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsHot() {
        return isHot;
    }

    public void setIsHot(String isHot) {
        this.isHot = isHot;
    }

    public String getIsRelease() {
        return isRelease;
    }

    public void setIsRelease(String isRelease) {
        this.isRelease = isRelease;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Date getAnnouncerTime() {
        return announcerTime;
    }

    public void setAnnouncerTime(Date announcerTime) {
        this.announcerTime = announcerTime;
    }

    public String getAnnouncerId() {
        return announcerId;
    }

    public void setAnnouncerId(String announcerId) {
        this.announcerId = announcerId;
    }
}
