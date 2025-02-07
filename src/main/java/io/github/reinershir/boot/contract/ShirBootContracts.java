package io.github.reinershir.boot.contract;

public class ShirBootContracts {

    public static final String RESP_CODE_SUCCESS = "00000";
    public static final String RESP_CODE_SUCCESS_DESC = "操作成功";

    public static final String RESP_CODE_FAILE = "00001";
    public static final String RESP_CODE_FAILE_DESC = "系统异常";

    //参数为空错误码
    public static final String RESP_CODE_FAILE_NULLPARAM = "00002";
    //登陆失败
    public static final String RESP_CODE_FAILE_LOGIN_FAILE = "00003";
    //参数不合法
    public static final String RESP_CODE_FAILE_PARAM = "00004";
    //超出文件上传大小限制
    public static final String RESP_CODE_UPLOAD_MAX_FAILE = "00010";

    /**
     * Feign调用服务时出现异常
     */
    public static final String RESP_CODE_FEIGN_EXCEPTION = "00010";

    //查询出错
    public static final String RESP_CODE_QUERY_ERROR = "00100";
    /**
     * 修改数据时出错码
     */
    public static final String RESP_CODE_DATA_UPDATE_ERROR = "00101";

    //web端请求服务端时的token盐
    public static final String HTTP_ACCESS_TOKEN_SALT = "httpWebSalt";

    /**
     * 密码加密密钥
     */
    public static final String LOGIN_SALT = "ShirBootloginSalt";
    /**
     * 用户信息加密密钥
     */
    public static final String USER_INFO_SALT = "ShirBootUserInfoSalt";

    //禁用状态
    public static final int STATUS_DISABLE = 1;
    //启用状态
    public static final int STATUS_ENABLE = 0;

    //删除状态
    public static final int STATUS_DELETED = 1;
    //正常状态
    public static final int STATUS_NORMAL = 0;
    
    /**
     * token失效时间，默认30分钟 
     */
    public static final int LOGIN_TOKEN_EXPIRE_MINUTES=30;
    
    /**
     * 微信接口的交易类型：扫码
     */
    public static final String WECHAT_TRADE_TYPE_NATIVE="NATIVE";
    /**
     * 微信交易类型：小程序
     */
    public static final String WECHAT_TRADE_TYPE_JSAPI="JSAPI";
    /**
     * APP
     */
    public static final String WECHAT_TRADE_TYPE_APP="APP";
    
    public static final int USER_GROUP_TYPE_STATION = 1;
    
    public static final int USER_GROUP_TYPE_SALE = 2;
}
