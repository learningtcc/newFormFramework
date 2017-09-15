package com.drore.model;

import com.drore.cloud.sdk.domain.SystemModel;
import com.google.gson.annotations.SerializedName;

/**
 * Created by wangl on 2017/9/1 0001.
 */
public class TeaCultureInfo extends SystemModel{
    public  static String table ="tea_culture";


    private String description;

    private String introduction;

    private int serial;

    private String title;

    @SerializedName("is_using")
    private String isUsing;

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

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
