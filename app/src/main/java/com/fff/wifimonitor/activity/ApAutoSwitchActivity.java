package com.fff.wifimonitor.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.fff.wifimonitor.R;
import com.fff.wifimonitor.util.WifiUtil;

/**
 * Created by fwz on 2016/11/19.
 */

public class ApAutoSwitchActivity extends Activity {
    private Switch switchAp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ap_switch);
        initViewId();
        initViewEvent();
    }

    private void initViewEvent() {

        if (WifiUtil.getInstance(this).isApAutoSwitch()) {
            switchAp.setChecked(true);
        } else {
            switchAp.setChecked(false);
        }

        switchAp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    WifiUtil.getInstance(ApAutoSwitchActivity.this).setApAutoSwitch(true);
                } else {
                    WifiUtil.getInstance(ApAutoSwitchActivity.this).setApAutoSwitch(false);
                }

            }
        });
    }

    private void initViewId() {
        switchAp = (Switch) findViewById(R.id.switch_ap_auto_switch);
    }
}
