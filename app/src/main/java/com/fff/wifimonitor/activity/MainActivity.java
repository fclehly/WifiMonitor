package com.fff.wifimonitor.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fff.wifimonitor.R;
import com.fff.wifimonitor.fragment.ClearNetworkDialogFragment;
import com.fff.wifimonitor.service.WifiScanService;
import com.fff.wifimonitor.util.WifiUtil;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    public static final String ACTION_AP_SWITCH = "action_ap_switch";

    private WifiManager wifiManager;
    private WifiUtil myWifiUtil;
    private WifiInfo wifiInfo;

    private ImageView imgCurrentWifiStrength;
    private TextView tvCurrentWifiSSID;

    private WifiStateChangeReceiver mWifiStateChangeReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WifiUtil.getInstance(this);
        bindViewID();
        myWifiUtil = WifiUtil.getInstance(this);
        wifiManager = myWifiUtil.getWifiManager();
        wifiInfo = myWifiUtil.getWifiInfo();
        registerSomeReceiver();
        initViewData();
        startWifiScanService();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mWifiStateChangeReceiver);
    }

    private void initViewData() {
        setCurrentWifiSSIDText();
        setCurrentWifiLevelIcon();
    }

    private void registerSomeReceiver() {
        mWifiStateChangeReceiver = new WifiStateChangeReceiver();
        IntentFilter filter = new IntentFilter();
//        filter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(WifiManager.RSSI_CHANGED_ACTION);
        registerReceiver(mWifiStateChangeReceiver, filter);
    }

    private void startWifiScanService() {
        Intent intent = new Intent(this, WifiScanService.class);
        startService(intent);
    }


    private void bindViewID() {
        imgCurrentWifiStrength = (ImageView) findViewById(R.id.img_current_wifi_strength);
        tvCurrentWifiSSID = (TextView) findViewById(R.id.tv_current_wifi_ssid);
    }

    public void onClickFunction(View view) {
        switch (view.getId()) {
            case R.id.item_func_wifi_info:
                Intent intent1 = new Intent(this, WifiInfoActivity.class);
                startActivity(intent1);
                break;
            case R.id.item_func_wifi_graph:
                Intent intent2 = new Intent(this, WifiGraphActivity.class);
                startActivity(intent2);
                break;
            case R.id.item_func_ap_switch:
                Intent intent3 = new Intent(this, ApAutoSwitchActivity.class);
                startActivity(intent3);
                break;
            case R.id.item_func_traffic_state:
                Intent intent4 = new Intent(this, TrafficGraphActivity.class);
                startActivity(intent4);
                break;
            case R.id.item_func_clear_network:
                ClearNetworkDialogFragment dialogFragment = new ClearNetworkDialogFragment();
                dialogFragment.show(getFragmentManager(), "clearNetwork");
                break;
            case R.id.item_func_more:
                Intent intent6 = new Intent(this, AboutActivity.class);
                startActivity(intent6);
                break;
        }
    }

    private void setCurrentWifiSSIDText() {
        if (wifiManager.isWifiEnabled()) {
            tvCurrentWifiSSID.setText(wifiInfo.getSSID());
        } else {
            tvCurrentWifiSSID.setText("disconnect");
        }

    }

    private void setCurrentWifiLevelIcon() {
        if (wifiManager.isWifiEnabled()) {
            if (wifiInfo.getRssi() > -40) {
                imgCurrentWifiStrength.setImageResource(R.drawable.wifi_level3);
            } else if (wifiInfo.getRssi() > -60) {
                imgCurrentWifiStrength.setImageResource(R.drawable.wifi_level2);
            } else if (wifiInfo.getRssi() > -80) {
                imgCurrentWifiStrength.setImageResource(R.drawable.wifi_level1);
            } else {
                imgCurrentWifiStrength.setImageResource(R.drawable.wifi_level0);
            }
        } else {
            imgCurrentWifiStrength.setImageResource(R.drawable.wifi_level0);
        }

    }

    class WifiStateChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (wifiManager.isWifiEnabled()) {
                myWifiUtil.setWifiInfo();
                if (!wifiInfo.getSSID().equals(myWifiUtil.getWifiInfo().getSSID())) {
                    wifiInfo = myWifiUtil.getWifiInfo();
                    setCurrentWifiSSIDText();
                }
                if (wifiInfo.getSSID().equals(myWifiUtil.getWifiInfo().getSSID()) && !wifiInfo.getBSSID().equals(myWifiUtil.getWifiInfo().getBSSID())) {
                    Toast.makeText(MainActivity.this, "ap has switch successfully", Toast.LENGTH_SHORT).show();
                    AsyncTask<Void,Void,Void> task = new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            try
                            {
                                Thread.currentThread().sleep(2000);
                                Intent i = new Intent(ACTION_AP_SWITCH);
                                sendBroadcast(i);
                            }
                            catch(Exception e)
                            {
                                e.printStackTrace();
                            }
                            return null;
                        }
                    };
                    task.execute();
                }
            }

            wifiInfo = myWifiUtil.getWifiInfo();
            setCurrentWifiLevelIcon();
        }
    }
}
