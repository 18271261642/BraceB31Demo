package com.brace.android.b31.constant;

/**
 * Created by Admin
 * Date 2019/11/5
 */
public class Constant {


    /**
     * 保存连接成功的Mac
     */
    public static final String CONN_BLE_MAC = "ble_mac";
    /**
     * 保存连接成功的Name
     */
    public static final String CONN_BLE_NAME = "ble_name";

    /**
     * 保存设备密码的key
     */
    public static final String DEVICE_PWD_KEY = "device_pwd_key";
    /**
     * 设备的协议版本key，普通版本为0,精准睡眠的设备版本为3
     */
    public static final String DEVICE_VERSION_KEY = "device_version_key";
    //设置的版本号和固件版本
    public static final String DEVICE__VERSION_NUMBER = "device_version_number";


    /**
     * 连接成功的action
     */
    public static final String DEVICE_CONNECT_ACTION = "com.brace.android.b31.connected";

    //断开连接的action
    public static final String DEVICE_DISANCE_ACTION = "com.brace.android.b31.disconnected";

    //提示输入密码
    public static final String DEVICE_INPUT_PWD_CODE = "com.brace.android.b31.input_pwd_code";





    //B31HRV的数据读取完了，
    public static final String B31_HRV_COMPLETE = "com.example.bozhilun.android.b31.hrv_complete";
    //B31血氧的数据读取完了
    public static final String B31_SPO2_COMPLETE = "com.example.bozhilun.android.b31.spo2_complete";



    /**
     * 跳转页面附带的参数: 日期
     */
    public static final String DETAIL_DATE = "detail_date";


    /**
     * 用户信息
     */
    //用户信息key:0-男；1-女
    public static final String USER_SEX = "user_sex";
    //用户体重 单位kg 整形
    public static final String USER_WEIGHT = "user_weight";
    //用户身高 单位 cm 整形
    public static final String USER_HEIGHT = "user_height";
    //用户年龄 整形
    public static final String USER_AGE = "user_age";






    //运动数据保存时的标识
    /**
     * 详细步数
     */
    public static final String DB_TYPE_SPORT = "db_type_sport";
    /**
     * 心率
     */
    public static final String DB_TYPE_HEART = "db_type_heart";
    /**
     * 血压
     */
    public static final String DB_TYPE_BLOOD = "db_type_blood";
    /**
     * 总步数
     */
    public static final String DB_TYPE_SPTES = "db_type_steps";

    /**
     * 普通睡眠
     */
    public static final String DB_TYPE_GENERAL_SLEEP = "db_type_sleep";
    /**
     * 精准睡眠
     */
    public static final String DB_TYPE_PRECISION_SLEEP = "db_type_precision_sleep";

    //设备支持的主题风格数量key
    public static final String SP_DEVICE_STYLE_COUNT = "sp_device_style_count";



    /**
     * spkey
     */
    public static final String ISSkype = "is_skype";//是否开启 Skype 提醒
    public static final String ISWhatsApp = "is_whatsApp";//是否开启 WhatsApp 提醒
    public static final String ISFacebook = "is_facebook";//是否开启 Facebook 提醒
    public static final String ISLinkendln = "is_linkendln";//是否开启 Linkendln 提醒
    public static final String ISTwitter = "is_twitter";//是否开启 Twitter 提醒
    public static final String ISViber = "is_viber";//是否开启 Viber 提醒
    public static final String ISLINE = "is_line";//是否开启 LINE 提醒
    public static final String ISWechart = "is_wechat";//是否开启 微信 提醒
    public static final String ISQQ = "is_qq";//是否开启 QQ 提醒
    public static final String ISMsm = "is_msg";//是否开启 短息 提醒
    public static final String ISPhone = "is_phone";//是否开启 电话 提醒
    public static final String ISInstagram = "is_instagram";//是否开启 Instagram 提醒
    public static final String ISGmail = "is_gmail";//是否开启 Gmail 提醒
    public static final String ISSnapchart = "is_snapchart";//是否开启 Snapchart 提醒
    public static final String ISOhter = "is_ohter";//是否开启 Ohter 提醒




    /*
     * @param isHaveMetricSystem     是否拥有设置公英制的功能，需要先读取个性化设置，调用readCustomSetting方法
     * @param isMetric               true设置公制，false表示设置英制,设备语言设置成[英语或繁体]才能体现英制
     * @param is24Hour               ture表示24小时制，false表示12小时制
     * @param isOpenAutoHeartDetect  true表示打开了自动测量心率功能，false表示关闭自动测量心率功能
     * @param isOpenAutoBpDetect     true表示打开了自动测量血压功能，false表示关闭自动测量血压功能
     * @param isOpenSportRemain      SUPPORT_OPEN 表示打开了运动过量提醒功能，SUPPORT_CLOSE 表示关闭运动过量提醒功能; UNSUPPORT表示不支持
     * @param isOpenVoiceBpHeart     SUPPORT_OPEN 表示打开了心率/血氧/血压播报功能，SUPPORT_CLOSE 表示关闭心率/血氧/血压播报功能; UNSUPPORT表示不支持
     * @param isOpenFindPhoneUI      SUPPORT_OPEN 表示打开了手机查找功能，SUPPORT_CLOSE 表示关闭手机查找功能; UNSUPPORT表示不支持
     * @param isOpenStopWatch        SUPPORT_OPEN 表示打开了秒表功能功能，SUPPORT_CLOSE 表示关闭秒表功能功能; UNSUPPORT表示不支持
     * @param isOpenSpo2hLowRemind   SUPPORT_OPEN 表示打开了低氧提醒功能，SUPPORT_CLOSE 表示关闭低氧提醒功能; UNSUPPORT表示不支持
     * @param isOpenWearDetectSkin    SUPPORT_OPEN 表示偏白色肤色 ，SUPPORT_CLOSE 表示偏黑色肤色; UNSUPPORT表示不支持
     * @param isOpenAutoInCall       SUPPORT_OPEN 表示打开了自动监听功能，SUPPORT_CLOSE 表示关闭自动监听功能; UNSUPPORT表示不支持
     * @param isOpenDisconnectRemind SUPPORT_OPEN 表示打开了断接提醒功能，SUPPORT_CLOSE 表示关闭断接提醒功能; UNSUPPORT表示不支持
     * @param isOpenSOS              SUPPORT_OPEN 表示打开了求救功能，SUPPORT_CLOSE 表示关闭求救功能; UNSUPPORT表示不支持
     */

    //佩戴检测
    public static final String FUN_CHECK_WEAR_KEY = "fun_check_wear";
    //是否是24小时制
    public static final String FUN_IS_24_HOUR_KEY = "fun_is_24_hour";
    //公英制
    public static final String FUN_IS_METRIC_KEY = "fun_is_metric";


    //运动目标
    public static final String DEVICE_SPORT_GOAL = "device_sport_goal";
    //睡眠目标
    public static final String DEVICE_SLEEP_GOAL = "device_sleep_goal";


}
