package com.fff.wifimonitor.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.fff.wifimonitor.activity.MainActivity;
import com.fff.wifimonitor.activity.WifiGraphActivity;

import static android.content.ContentValues.TAG;

/**
 * Created by fwz on 2016/11/19.
 */

public class ScanResultReceiver extends BroadcastReceiver {

    Handler handler;

    public ScanResultReceiver(Handler handler) {

        this.handler = handler;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        if (action.equals(MainActivity.ACTION_AP_SWITCH)) {
            Log.d(TAG, "onReceive: action ap switch");
            Message msg = handler.obtainMessage();
            msg.what = WifiGraphActivity.MSG_SCANRESULT_GET;
            handler.sendMessage(msg);

        }
    }

}
