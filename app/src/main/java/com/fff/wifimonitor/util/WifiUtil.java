package com.fff.wifimonitor.util;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.util.List;

/**
 * Created by fwz on 2016/11/19.
 */

public class WifiUtil {

    private static WifiUtil mWifiUtil;


    private static final String DebugTag = "debugTag";
    private Context context;

    private WifiManager wifiManager;

    private WifiInfo wifiInfo;
    private List<ScanResult> scanResults;
    private List<WifiConfiguration> wifiConfigurations;

    private boolean apAutoSwitch;

    private WifiUtil(Context context) {
        this.context = context;

        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifiInfo = wifiManager.getConnectionInfo();
        wifiConfigurations = wifiManager.getConfiguredNetworks();
        scanResults = wifiManager.getScanResults();

        apAutoSwitch = false;


    }

    public static WifiUtil getInstance(Context context) {
        if (mWifiUtil == null) {
            synchronized (WifiUtil.class) {
                if (mWifiUtil == null) {
                    mWifiUtil = new WifiUtil(context);
                }
            }
        }
        return mWifiUtil;
    }

    public WifiManager getWifiManager() {
        return wifiManager;
    }

    public WifiInfo getWifiInfo() {
        return wifiInfo;
    }

    public List<ScanResult> getScanResults() {
        return scanResults;
    }

    public List<WifiConfiguration> getWifiConfigurations() {
        return wifiConfigurations;
    }

    public boolean isApAutoSwitch() {
        return apAutoSwitch;
    }

    public void setWifiInfo() {
        this.wifiInfo = wifiManager.getConnectionInfo();
    }

    public void setScanResults() {
        this.scanResults = wifiManager.getScanResults();
    }

    public void setApAutoSwitch(boolean apAutoSwitch) {
        this.apAutoSwitch = apAutoSwitch;
    }

    public void setWifiConfigurations() {
        this.wifiConfigurations = wifiManager.getConfiguredNetworks();
    }
}
