package com.fff.wifimonitor.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.fff.wifimonitor.service.WifiScanService;

/**
 * Created by fwz on 2016/11/19.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, WifiScanService.class);
        context.startService(i);
    }
}
