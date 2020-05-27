package org.sjtugo.api.service;

public interface UserConstant {
    // 请求的网址
    public static final String WX_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session?";
    // 小程序appid
    public static final String WX_LOGIN_APPID = "";
    // 密匙
    public static final String WX_LOGIN_SECRET = "";
    // 固定参数
    public static final String WX_LOGIN_GRANT_TYPE = "authorization_code";
}
