package com.fff.wifimonitor.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.fff.wifimonitor.R;
import com.fff.wifimonitor.util.WifiUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fwz on 2016/11/19.
 */

public class WifiInfoActivity extends Activity {
    private ListView lvWifiInfo;

    private List<Map<String, Object>> mapList;
    private SimpleAdapter lvAdapter;
    private static final String[] from = {"name", "value"};
    private static final int[] to = {R.id.tv_item_wifi_info_name, R.id.tv_item_wifi_info_value};

    private String[] itemName;

    private String[] itemValue;


    private WifiUtil myWifiUtil;
    private WifiManager wifiManager;
    private WifiInfo wifiInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_info);

        myWifiUtil = WifiUtil.getInstance(this);
        wifiManager = myWifiUtil.getWifiManager();
        wifiInfo = myWifiUtil.getWifiInfo();


        initViewId();
        initViewData();

    }

    private void initViewData() {
        mapList = new ArrayList<Map<String, Object>>();
        initMapListData();
        lvAdapter = new SimpleAdapter(this, mapList, R.layout.item_wifi_info, from, to);
        lvWifiInfo.setAdapter(lvAdapter);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initMapListData() {
        if (mapList != null || mapList.size() > 0) {
            mapList.clear();
        }

        itemName = new String[]{getString(R.string.State), getString(R.string.SSID), getString(R.string.BSSID), getString(R.string.Rssi), getString(R.string.LinkSpeed), getString(R.string.MacAdress), getString(R.string.NetworkId), getString(R.string.IpAddress), getString(R.string.Frequency), getString(R.string.Channel)};

        itemValue = new String[]{getString(R.string.connected), wifiInfo.getSSID(), wifiInfo.getBSSID(), wifiInfo.getRssi() + "dBm", wifiInfo.getLinkSpeed() + WifiInfo.LINK_SPEED_UNITS, wifiInfo.getMacAddress(), wifiInfo.getNetworkId() + "", intToIpString(wifiInfo.getIpAddress()) + "", wifiInfo.getFrequency() + "", ((wifiInfo.getFrequency() - 2412) / 5 + 1) + ""};

        for (int i = 0; i < itemName.length; ++i) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(from[0], itemName[i]);
            map.put(from[1], itemValue[i]);
            mapList.add(map);
        }

    }

    private void initViewId() {
        lvWifiInfo = (ListView) findViewById(R.id.lv_wifi_info);
    }

    private String intToIpString(int ipAddress) {
        int byte1 = ipAddress & 0xff;
        int byte2 = (ipAddress >> 8) & 0xff;
        int byte3 = (ipAddress >> 16) & 0xff;
        int byte4 = (ipAddress >> 24) & 0xff;
        return byte1 + "." + byte2 + "." + byte3 + "." + byte4;
    }
}

