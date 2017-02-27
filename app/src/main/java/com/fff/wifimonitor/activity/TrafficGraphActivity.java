package com.fff.wifimonitor.activity;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.fff.wifimonitor.R;
import com.fff.wifimonitor.util.NetSpeed;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by fwz on 2016/11/19.
 */

public class TrafficGraphActivity extends Activity {
    private LineChart downloadChart1;
    private LineChart downloadChart2;

    private NetSpeed QQ = null;
    private NetSpeed thunder = null;

    private Handler handler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trafficgraph);

        downloadChart1 = (LineChart) findViewById(R.id.download1);
        downloadChart2 = (LineChart) findViewById(R.id.download2);
        initChart(downloadChart1, "QQ");
        initChart(downloadChart2, "thunder");

        initHandler();
        initVars();
    }

    private void initChart(LineChart chart, String des) {
        Description desc = new Description();
        desc.setText(des);
        chart.setDescription(desc);

        chart.setDrawBorders(false);
        chart.setDrawGridBackground(true);
        chart.setGridBackgroundColor(Color.WHITE);

        chart.setBackgroundColor(Color.WHITE);

        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setPinchZoom(false);

        chart.setNoDataText("no data");

        YAxis lefty = chart.getAxisLeft();
        lefty.setStartAtZero(false);

        YAxis righty = chart.getAxisRight();
        righty.setEnabled(false);

        chart.getXAxis().setPosition(XAxisPosition.BOTTOM);

        LineData data = new LineData();
        chart.setData(data);
    }

    private void initVars() {
        PackageManager pm = getPackageManager();
        List<ApplicationInfo> apps = pm.getInstalledApplications(0);
        for (ApplicationInfo app : apps) {
            String name = app.loadLabel(pm).toString();
            if (name.equals("QQ")) {
                QQ = new NetSpeed(handler, app.uid);
            } else if (name.equals("迅雷")) {
                thunder = new NetSpeed(handler, app.uid);
            }
        }
    }

    private LineDataSet createLineDataSet(String text) {
        LineDataSet set = new LineDataSet(null, text);
        set.setLineWidth(1.0f);
        set.setColor(Color.BLUE);
        set.setCircleSize(2.0f);
        set.setDrawFilled(true);
        set.setFillAlpha(150);
        set.setFillColor(Color.RED);
        set.setDrawHighlightIndicators(true);
        set.setHighLightColor(Color.GREEN);

        set.setValueFormatter(new IValueFormatter() {

            @Override
            public String getFormattedValue(float value, Entry entry,
                                            int dataSetIndex, ViewPortHandler viewPortHandler) {
                long bytes = (long) value;

                return format(bytes);
            }
        });
        return set;

    }

    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == NetSpeed.HANDLER_RETURN) {
                    Bundle data = msg.getData();
                    int uid = data.getInt(NetSpeed.UID);
                    long down = data.getLong(NetSpeed.DOWNSPEED);

                    LineData linedata = null;
                    int index = 1;
                    String str = "";
                    if (uid == QQ.getUid()) {
                        linedata = downloadChart1.getData();
                        index = 1;
                        str = "QQ";
                    } else if (uid == thunder.getUid()) {
                        linedata = downloadChart2.getData();
                        index = 2;
                        str = "thunder";
                    }
                    LineDataSet set = (LineDataSet) linedata
                            .getDataSetByIndex(0);
                    if (set == null) {
                        set = createLineDataSet(str);
                        linedata.addDataSet(set);
                    }
                    Entry entry = new Entry(set.getEntryCount(), down);
                    linedata.addEntry(entry, 0);
                    if (index == 1) {
                        downloadChart1.notifyDataSetChanged();
                        downloadChart1.moveViewToX(set.getEntryCount() - 5);
                    } else if (index == 2) {
                        downloadChart2.notifyDataSetChanged();
                        downloadChart2.moveViewToX(set.getEntryCount() - 5);
                    }

                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (QQ != null)
            QQ.startCalculateNetSpeed();
        if (thunder != null)
            thunder.startCalculateNetSpeed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (QQ != null) QQ.stopCalculateNetSpeed();
        if (thunder != null) thunder.stopCalculateNetSpeed();
    }

    private String format(long bytes) {
        DecimalFormat df = new DecimalFormat("######0.00");

        double res = (double) bytes;
        if (res < 1024) {
            return df.format(res) + "B/s";
        } else {
            res /= 1024;
            if (res < 1024) {
                return df.format(res) + "KB/s";
            } else {
                res /= 1024;
                if (res < 1024) {
                    return df.format(res) + "MB/s";
                } else {
                    res /= 1024;
                    if (res < 1024) {
                        return df.format(res) + "GB/s";
                    } else
                        return df.format(res) + "GB/s";
                }
            }
        }
    }
}
