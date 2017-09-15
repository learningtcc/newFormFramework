package com.drore.cloud.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by wangl on 2017/9/12 0012.
 */
public class Traces implements Serializable{

    private Date AcceptTime;
    private String AcceptStation;

    public Date getAcceptTime() {
        return AcceptTime;
    }

    public void setAcceptTime(Date acceptTime) {
        AcceptTime = acceptTime;
    }

    public String getAcceptStation() {
        return AcceptStation;
    }

    public void setAcceptStation(String acceptStation) {
        AcceptStation = acceptStation;
    }
}
