package com.qttx.toolslibrary.net.basbean;

import java.util.List;

/**
 * Created by huangyr
 * on 2018/3/8.
 */

public class ResultListBean<T> {

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    private List<T> list;
    // "point": 2,     "balance": "99550.00"
    private String point;
    private String balance;

    public String getPoint() {
        return point;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public int getTotal_user_num() {
        return total_user_num;
    }

    public void setTotal_user_num(int total_user_num) {
        this.total_user_num = total_user_num;
    }

    private int total_user_num;

    public ResultPageBean getPage() {
        return page;
    }

    public void setPage(ResultPageBean page) {
        this.page = page;
    }

    private ResultPageBean page;

    public String getQiniu_domain() {
        return qiniu_domain;
    }

    public void setQiniu_domain(String qiniu_domain) {
        this.qiniu_domain = qiniu_domain;
    }

    private String qiniu_domain;

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    private int max;

    public String getTo_id() {
        return to_id;
    }

    public void setTo_id(String to_id) {
        this.to_id = to_id;
    }

    private String to_id;

}
