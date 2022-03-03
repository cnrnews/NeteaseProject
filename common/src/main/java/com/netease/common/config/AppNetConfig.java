package com.netease.common.config;

/**
 * @Author Liudeli
 * @Describe：配置程序中所有接口请求的地址
 */
public class AppNetConfig {

    private static final String TAG = AppNetConfig.class.getSimpleName();

    /**
     * http://192.168.1.112:8080/qbank-gateway/api/" +/" + 接口名称
     */

    /**
     * 测试服务器HOST
     */
    private static final String TEST_HOST = "192.168.1.112";

    /**
     * 测试的端口号
     */
    private static final String TEST_PROT = "8080";

    /**
     * 正式服务器HOST
     */
    private static final String REGULAR_HOST = "199.169.......";

    /**
     * 正式的端口号
     */
    private static final String REGULAR_PROT = "9090";

    /**
     * 基础URL模型定义地址 http
     */
    public static final String BASE_HTTP_URL = "http://"+ TEST_HOST +":"+ TEST_PROT +"/qbank-gateway/api/";

    /**
     * 基础URL模型定义地址 https
     */
    public static final String BASE_HTTPS_URL = "https://"+ TEST_HOST +":"+ TEST_PROT +"/qbank-gateway/api/";

    /**
     * 模拟登录接口
     */
    public static final String LOGIN_URL = BASE_HTTPS_URL + "login";

    /**
     * 模拟注册接口
     */
    public static final String REGISTER_URL = BASE_HTTPS_URL + "register";
}
