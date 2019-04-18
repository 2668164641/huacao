package com.qttx.toolslibrary.event;

public class BaseEventType {

    /**
     * LOGIN==>登录事件分发
     * LOGINOUT==>登出事件分发
     * TAB_CHANGE==>发送底部tab切换
     * WEIXIN_PAY==>微信支付事件
     * WEIXIN_SHARE==>微信分享事件
     * WEIXIN_LOGIN==>微信登录事件
     * PUSH_MSG==>收到推送
     */


    public final static String LOGIN = "LOGIN";
    public final static String LOGINOUT = "LOGINOUT";
    public final static String TAB_CHANGE = "TAB_CHANGE";
    public final static String WEIXIN_PAY = "WEIXIN_PAY";
    public final static String WEIXIN_LOGIN = "WEIXIN_LOGIN";
    public final static String WEIXIN_SHARE = "WEIXIN_SHARE";
    public final static String PUSH_MSG = "PUSH_MSG";
}
