/*
* Copyright (C) 2016 Zhejiang DRORE Technology CO.,LTD.
* All rights reserved.
* Official Web Site: http://www.drore.com.
* Developer Web Site: http://open.drore.com.
*/
package com.drore.cloud.model;

import com.drore.cloud.sdk.domain.SystemModel;
import com.drore.cloud.sdk.exception.IllegalRequestParametersException;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.StringUtils;

/**
 * 互动主题表
 * Created by zhangh on 2017/9/12 0001.
 */
public class InteractiveTheme extends SystemModel {

    public static String table="interactive_theme";

    @SerializedName("theme_name")
    private String themeName;

    @SerializedName("theme_pic")
    private String themePic;

    private Integer number;

    public String getThemeName() {
        return themeName;
    }

    public String getThemePic() {
        return themePic;
    }

    public Integer getNumber() {
        return number;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public void setThemePic(String themePic) {
        this.themePic = themePic;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public void validate() {
        if (StringUtils.isBlank(this.themeName)) {
            throw new IllegalRequestParametersException("名称不能为空");
        }
        if (StringUtils.isBlank(this.themePic)) {
            throw new IllegalRequestParametersException("图片不能为空");
        }
    }
}
