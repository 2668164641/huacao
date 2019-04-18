package com.qttx.toolslibrary.net.basbean;

/**
 * Created by huangyr
 * on 2018/3/8.
 */

public class ResultCacheBean {
    /**
     * useCache : N
     * queryGroupId : 4003
     * dataTime : 2017-05-05 15:04:31
     */

    private String useCache;
    private String queryGroupId;
    private String dataTime;

    private String serviceCode;


    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getUseCache() {
        return useCache;
    }

    public void setUseCache(String useCache) {
        this.useCache = useCache;
    }

    public String getQueryGroupId() {
        return queryGroupId;
    }

    public void setQueryGroupId(String queryGroupId) {
        this.queryGroupId = queryGroupId;
    }

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }
}
