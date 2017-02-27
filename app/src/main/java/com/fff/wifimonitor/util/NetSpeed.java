package com.fff.wifimonitor.util;

import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by fwz on 2016/11/19.
 */

public class NetSpeed {

    private long preRxBytes = 0;
    private long preTxBytes = 0;
    private Timer timer = null;
    private Handler handler;
    private int uid;
    private int interval_time=1000;

    public static final String UPSPEED ="upspeed";
    public static final String DOWNSPEED = "downspeed";
    public static final String UID = "uid";
    public static final int HANDLER_RETURN =1;

    public NetSpeed(Handler handler,int uid)
    {
        this.handler = handler;
        this.uid=uid;
    }

    public int getUid()
    {
        return uid;
    }

    private long getNetworkRxBytes()
    {
        long rxBytes = TrafficStats.getUidRxBytes(uid);
        if(rxBytes == TrafficStats.UNSUPPORTED)
        {
            rxBytes = TrafficStats.getTotalRxBytes();
        }
        return rxBytes;
    }

    private long getNetworkTxBytes()
    {
        long txBytes = TrafficStats.getUidTxBytes(uid);
        if(txBytes == TrafficStats.UNSUPPORTED)
        {
            txBytes = TrafficStats.getTotalTxBytes();
        }
        return txBytes;
    }
    public long getRxNetSpeed()
    {
        long curBytes = getNetworkRxBytes();
        long bytes = curBytes - preRxBytes;
        preRxBytes = curBytes;
        return bytes/(interval_time/1000);
    }

    public long getTxNetSpeed()
    {
        long curBytes = getNetworkTxBytes();
        long bytes = curBytes - preTxBytes;
        preTxBytes = curBytes;
        return bytes/(interval_time/1000);
    }
    public void startCalculateNetSpeed()
    {
        preRxBytes = getNetworkRxBytes();
        preTxBytes = getNetworkTxBytes();
        if(timer!=null)
        {
            timer.cancel();
            timer=null;
        }
        timer = new Timer();
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                Message msg = new Message();

                long upspeed = getTxNetSpeed();
                long downspeed = getRxNetSpeed();
                msg.what = HANDLER_RETURN;
                Bundle data = new Bundle();
                data.putLong(UPSPEED, upspeed);
                data.putLong(DOWNSPEED, downspeed);
                data.putInt(UID, uid);
                msg.setData(data);
                handler.sendMessage(msg);
            }
        };
        timer.schedule(task, 0, interval_time);
    }

    public void stopCalculateNetSpeed()
    {
        if(timer!=null)
        {
            timer.cancel();
            timer=null;
        }
    }
}
