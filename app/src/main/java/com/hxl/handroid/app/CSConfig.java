package com.hxl.handroid.app;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.hxl.handroid.util.SPUtil;


public class CSConfig {
    public static final String TAG = "App.log.CS";

    public static final String IP_KEY = "ip";
    public static final String PORT_KEY = "port";
    public static final String VERSION_KEY = "version";
    public static final String DEBUG_KEY = "debug";
    public static final String UIN_KEY = "uin";
    public static final String SESSION_KEY = "session";

    private static CSConfig config;
    private String mIP;
    private int mPort;
    private int version;
    private Boolean debug;

    private Long uin;
    private String session;

    public static CSConfig instance() {
        if (config == null) {
            synchronized (CSConfig.class) {
                if (config == null) {
                    config = new CSConfig();
                }
            }
        }
        return config;
    }

    public void setIP(Context context, String ip) {
        mIP = ip;
        SPUtil.put(context, CSConfig.IP_KEY, ip);
    }

    public String getIP() {
        if (TextUtils.isEmpty(mIP)) {
            mIP = (String) SPUtil.get(App.getApplication(), CSConfig.IP_KEY, "");
        }
        return mIP;
    }

    public int getPort() {
        if (mPort == 0) {
            mPort = (int) SPUtil.get(App.getApplication(), CSConfig.PORT_KEY, 0);
        }
        return mPort;
    }

    public void setPort(Context context, int port) {
        SPUtil.put(context, CSConfig.PORT_KEY, port);
        this.mPort = port;
    }

    public void setVersion(Context context, int version) {
        this.version = version;
        SPUtil.put(context, CSConfig.VERSION_KEY, version);
    }

    public int getVersion() {
        if (version == 0) {
            version = (int) SPUtil.get(App.getApplication(), CSConfig.VERSION_KEY, 0);
        }
        return version;
    }

    public boolean getDebug() {
        if (debug == null) {
            debug = (Boolean) SPUtil.get(App.getApplication(), CSConfig.DEBUG_KEY, true);
        }
        return debug;
    }

    public Long getUin() {
        if (uin == null || uin == 0) {
            uin = (Long) SPUtil.get(App.getApplication(), UIN_KEY, 0L);
        }
        return uin;
    }

    public void setUin(Context context, Long uin) {
        SPUtil.put(context, UIN_KEY, uin);
        this.uin = uin;
    }

    public String getSession() {
        if (TextUtils.isEmpty(session)) {
            session = (String) SPUtil.get(App.getApplication(), SESSION_KEY, "");
        }
        return session;
    }

    public void setSession(Context context, String session) {
        this.session = session;
        SPUtil.put(context, SESSION_KEY, session);
    }

    /**
     * 初始化
     *
     * @param ip
     * @param port
     * @param version
     */
    public void init(Context context, String ip, int port, int version) {
        Log.d(TAG, "ip:" + ip + "    port:" + port + "    version:" + version);
        setIP(context, ip);
        setPort(context, port);
        setVersion(context, version);
    }

    /**
     * 设置user uin和session
     *
     * @param context
     * @param uin
     * @param session
     */
    public void setSession(Context context, Long uin, String session) {
        Log.d(TAG, "uin:" + uin + "    session:" + session);
        setUin(context, uin);
        setSession(context, session);
    }

}
