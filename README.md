# B31设备配套代码接入步骤：
一，下载代码，BraceB31Demo依赖于BraceB31，下载完成后直接跑起来查看效果；</br></br>
二，自己项目使用，Android Studio ,File ->> New ->> Import Module 导入BraceB31 Library，</br>
  工程配置：</br>
  1，app Module 的 build.gradle 中加入入 implementation project(path: ':BraceB31') 引入BraceB31库</br>
  2，android 下加入</br>
  repositories {</br>
        flatDir {</br>
            dirs 'libs', '/../BraceB31/libs'</br>
        }</br>
    }</br>
    注意：必须配置 '/../BraceB31/libs' 不然会找不到 BraceB31module中 so文件；</br>

三，具体使用：</br>
  1，新建自己工程的Application，如果已有，则 exetends BaseApplication 即可，BaseApplication为BraceB31中的application，最后在AndroidManifest.xml文件
  中application标签下配置 自己的Application；</br>
  2，设置目标步数、用户信息等资料，该资料保存在SharedPreferences 文件中，在连接设备前调用以下两个方法即可；</br>
        //设置目标步数
        SpUtils.setParam(OwnScanActivity.this,Constant.DEVICE_SPORT_GOAL,10000);
        //设置用户信息 性别、身高、体重、年龄
        BleConnDataOperate.getBleConnDataOperate().setBasicMsgData(0,175,60,25)
  3，搜索设备，已有自己的搜索页面或者我自己的搜索页面
    不使用自己的搜索页面：
      直接Intent ScanActivity.java 即可打开搜索页面搜索设备，搜索到设备后点击绑定即可；
    使用自己的搜索页面：
      注册广播添加action  Constant.DEVICE_CONNECT_ACTION，Constant.DEVICE_INPUT_PWD_CODE
      开始连接： 
        //开始连接，bleName,bleMac,pwd , pwd默认0000 参数都非空！
        BaseApplication.getBaseApplication().getBleConnStatusService().connBleB31Device(bleName,bleMac,pwd);
      连接成功：BroadCastReceiver的action为：Constant.DEVICE_CONNECT_ACTION 时表明已连接成功，连接成功后调用 BleConnDataOperate.getBleConnDataOperate().syncUserInfoData();
       方法给手环同步用户信息；调用 BleConnStatus.isScannInto = true 表示是从搜索页面进入的主页面中；最后进入主页面 Intent BraceHomeActivity.java 即可；
       
  
  
  更多使用请查看BraceB31Demo中的代码
