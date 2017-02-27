package com.fff.wifimonitor.activity;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.widget.LinearLayout;

import com.fff.wifimonitor.R;
import com.fff.wifimonitor.receiver.ScanResultReceiver;
import com.fff.wifimonitor.util.ShowView;
import com.fff.wifimonitor.util.WifiUtil;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by fwz on 2016/11/19.
 */

public class WifiGraphActivity extends Activity {
    public static final int MSG_SCANRESULT_GET = 123;

    LinearLayout ll;
    WifiManager manager;
    ShowView view;
   // boolean ison = false;
   // Thread thread;
    ScanResultReceiver receiver;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);

            if (msg.what == MSG_SCANRESULT_GET) {
                Log.d(TAG,"handler message:invalidate");
                List<ScanResult> list = manager.getScanResults();
                Comparator<ScanResult> com = new Comparator<ScanResult>() {

                    @Override
                    public int compare(ScanResult lhs, ScanResult rhs) {

                        int r1 = lhs.level;
                        int r2 = rhs.level;
                        if(r1<r2) return 1;
                        if(r1>r2) return -1;
                        return 0;
                    }
                };
                Collections.sort(list, com);
                view.SetList(list);
                view.invalidate();
            }
        }
    };

    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            Log.d("debug","onwindowsfocuschanged");
            Rect rect = new Rect();
            this.getWindow().findViewById(Window.ID_ANDROID_CONTENT)
                    .getDrawingRect(rect);
            int width = rect.width();
            int height = rect.height();
            view = new ShowView(this, width, height);
            ll.addView(view);
            WifiUtil.getInstance(this).setScanResults();
            List<ScanResult> l = WifiUtil.getInstance(this).getScanResults();
            Comparator<ScanResult> com = new Comparator<ScanResult>() {

                @Override
                public int compare(ScanResult lhs, ScanResult rhs) {

                    int r1 = lhs.level;
                    int r2 = rhs.level;
                    if(r1<r2) return 1;
                    if(r1>r2) return -1;
                    return 0;
                }
            };
            Collections.sort(l, com);
            view.SetList(l);
            view.invalidate();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifigraph);

        ll = (LinearLayout) findViewById(R.id.ll);
        manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        receiver = new ScanResultReceiver(handler);


    }

    /*private void initThread() {
        ison = true;
        thread = new Thread(new Runnable() {

            @Override
            public void run() {

                while (ison) {
                    try {
                        manager.startScan();
                        Thread.sleep(20 * 1000);
                    } catch (InterruptedException e) {

                        e.printStackTrace();
                    }
                }
            }
        });
    }
*/
    @Override
    protected void onResume() {

        super.onResume();
        IntentFilter filter = new IntentFilter(
                MainActivity.ACTION_AP_SWITCH);
        registerReceiver(receiver, filter);
//        initThread();
//        thread.start();
    }

    @Override
    protected void onPause() {

        super.onPause();
        //ison = false;
        unregisterReceiver(receiver);
    }
}
