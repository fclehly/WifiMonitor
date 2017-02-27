package com.fff.wifimonitor.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by fwz on 2016/11/19.
 */

public class TrafficListItemBean {
    private String name;
    private Drawable icon;
    private long upload;
    private long download;

    public TrafficListItemBean(String name, Drawable icon, long upload, long download)
    {
        this.name=name;
        this.icon=icon;
        this.upload=upload;
        this.download=download;
    }

    public String getName()
    {
        return this.name;
    }

    public Drawable getIcon()
    {
        return this.icon;
    }

    public long getUpload()
    {
        return this.upload;
    }

    public long getDownload()
    {
        return this.download;
    }

    @Override
    public boolean equals(Object o) {
        TrafficListItemBean it = (TrafficListItemBean)o;
        return this.name.equals(it.name);
    }
}