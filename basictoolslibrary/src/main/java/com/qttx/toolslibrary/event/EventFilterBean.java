package com.qttx.toolslibrary.event;


/**
 * Created by huangyr
 * on 2017/11/10.
 * 事件分发bean
 */

public class EventFilterBean {

    public String type;

    public String tag;

    public Object value;

    public EventFilterBean(String type, Object value) {
        this.type = type;
        this.value = value;
    }

    public EventFilterBean(String type, String tag, Object value) {
        this.type = type;
        this.tag = tag;
        this.value = value;
    }

    @Override
    public String toString() {
        return type;
    }
}
