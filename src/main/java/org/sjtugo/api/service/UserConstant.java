package org.sjtugo.api.service;

public interface UserConstant {
    // 请求的网址
    public static final String WX_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session?";
    // 小程序appid
    public static final String WX_LOGIN_APPID = "wxde82431f805562ef";
    // 密匙
    public static final String WX_LOGIN_SECRET = "40fb5700a5742cdf2d744ef87df8da98";
    // 固定参数
    public static final String WX_LOGIN_GRANT_TYPE = "authorization_code";
}
