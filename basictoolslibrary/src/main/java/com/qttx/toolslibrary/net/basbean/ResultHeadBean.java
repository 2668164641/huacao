package com.qttx.toolslibrary.net.basbean;

/**
 * Created by huangyr
 * on 2018/3/8.
 */

public class ResultHeadBean {
    /**
     * code : 0000
     * msg : 处理成功
     * service : dataQuery
     * serviceCode : getNewsNoticeList
     * reqTime : 1493967871282
     * rspTime : 1493967871291
     * useTime : 9
     */

    private String code;
    private String msg;
    private String service;
    private String serviceCode;
    private long reqTime;
    private long rspTime;
    private int useTime;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public long getReqTime() {
        return reqTime;
    }

    public void setReqTime(long reqTime) {
        this.reqTime = reqTime;
    }

    public long getRspTime() {
        return rspTime;
    }

    public void setRspTime(long rspTime) {
        this.rspTime = rspTime;
    }

    public int getUseTime() {
        return useTime;
    }

    public void setUseTime(int useTime) {
        this.useTime = useTime;
    }
}
