package com.drore.cloud.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by wangl on 2017/9/12 0012.
 */
public class Logistics implements Serializable {

    private String EBusinessID;
    private String ShipperCode;
    private String Success;
    private String LogisticCode;
    private String State;
    private List<Map> Traces;

    public String getEBusinessID() {
        return EBusinessID;
    }

    public void setEBusinessID(String EBusinessID) {
        this.EBusinessID = EBusinessID;
    }

    public String getShipperCode() {
        return ShipperCode;
    }

    public void setShipperCode(String shipperCode) {
        ShipperCode = shipperCode;
    }

    public String getSuccess() {
        return Success;
    }

    public void setSuccess(String success) {
        Success = success;
    }

    public String getLogisticCode() {
        return LogisticCode;
    }

    public void setLogisticCode(String logisticCode) {
        LogisticCode = logisticCode;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public List<Map> getTraces() {
        return Traces;
    }

    public void setTraces(List<Map> traces) {
        Traces = traces;
    }
}
