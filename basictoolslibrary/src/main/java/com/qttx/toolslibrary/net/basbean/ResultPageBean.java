package com.qttx.toolslibrary.net.basbean;

/**
 * Created by huang on 2017/2/15.
 */

public class ResultPageBean {


    private boolean hasMore;
    /**
     * total_count : 4
     * page_count : 1
     * page_current : 1
     */

    private int total_count;
    private int page_count;

    public int getPage_current() {
        return page_current;
    }

    public void setPage_current(int page_current) {
        this.page_current = page_current;
    }

    private int page_current;


    public boolean isHasMore() {
        return page_current<page_count;
    }


    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public int getPage_count() {
        return page_count;
    }

    public void setPage_count(int page_count) {
        this.page_count = page_count;
    }

}
