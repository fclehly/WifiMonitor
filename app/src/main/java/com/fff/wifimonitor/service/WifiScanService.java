package com.fff.wifimonitor.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.fff.wifimonitor.receiver.AlarmReceiver;
import com.fff.wifimonitor.util.WifiUtil;

/**
 * Created by fwz on 2016/11/19.
 */

public class WifiScanService extends Service {




    private WifiUtil myWifiUtil;
    private WifiManager wifiManager;
    private ScanResultAvailableReceiver scanResultAvailableReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                wifiManager.startScan();
            }
        }).start();

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int halfMinute = 10 * 1000;

        long triggerTime = SystemClock.elapsedRealtime() + halfMinute;

        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent operation = PendingIntent.getBroadcast(this, 0, i, 0);

        alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, operation);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myWifiUtil = myWifiUtil.getInstance(this);
        wifiManager = myWifiUtil.getWifiManager();
        registerScanResultAvailableReceiver();
    }


    class ScanResultAvailableReceiver extends BroadcastReceiver {
        private static final String TAG = "ScanResultReceiver";

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: broadcast received");
            //refresh scan results
            myWifiUtil.setScanResults();

            //ap switch
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (myWifiUtil.isApAutoSwitch()) {
                        int maxRssi = myWifiUtil.getWifiInfo().getRssi();
                        boolean isApSwitch = false;
                        for (ScanResult result : myWifiUtil.getScanResults()) {
                            if (result.level > (maxRssi + 10) && myWifiUtil.getWifiInfo().getSSID().equals("\"" + result.SSID + "\"") && !myWifiUtil.getWifiInfo().getBSSID().equals(result.BSSID)) {
                                isApSwitch = true;
                                break;
                            }
                        }
                        if (isApSwitch) {
                            wifiManager.disconnect();
                            wifiManager.reconnect();
                        }
                    }
                }
            }).start();

        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(scanResultAvailableReceiver);
    }

    private void registerScanResultAvailableReceiver() {
        IntentFilter filter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        scanResultAvailableReceiver = new ScanResultAvailableReceiver();
        registerReceiver(scanResultAvailableReceiver, filter);
    }
}

