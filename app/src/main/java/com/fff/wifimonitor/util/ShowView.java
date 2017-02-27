package com.fff.wifimonitor.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.view.View;

import com.fff.wifimonitor.R;

import java.util.List;

/**
 * Created by fwz on 2016/11/19.
 */

public class ShowView extends View {

    WifiManager manager;
    List<ScanResult> list;
    int ScreenWidth;
    int ScreenHeight;
    Paint textPaint;
    Paint linePaint_normal;
    Paint linePaint_connect;
    Bitmap[] bitmaps;
    Bitmap phone;

    float scale = 0.15f;

    public ShowView(Context context, int width, int height) {
        super(context);
        // TODO Auto-generated constructor stub
        manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        list = null;
        ScreenWidth = width;
        ScreenHeight = height;

        textPaint = new Paint();
        textPaint.setColor(Color.BLUE);
        textPaint.setTextAlign(Align.CENTER);

        linePaint_normal = new Paint();
        linePaint_normal.setColor(Color.BLACK);
        linePaint_normal.setStrokeWidth(5);
        linePaint_connect = new Paint();
        linePaint_connect.setColor(Color.RED);
        linePaint_connect.setStrokeWidth(5);

        bitmaps = new Bitmap[4];
        bitmaps[0] = BitmapFactory.decodeResource(getResources(),
                R.drawable.wifi_1);
        bitmaps[1] = BitmapFactory.decodeResource(getResources(),
                R.drawable.wifi_2);
        bitmaps[2] = BitmapFactory.decodeResource(getResources(),
                R.drawable.wifi_3);
        bitmaps[3] = BitmapFactory.decodeResource(getResources(),
                R.drawable.wifi_4);
        phone = BitmapFactory.decodeResource(getResources(), R.drawable.phone);

        float size = (float) (bitmaps[0].getHeight() * scale / 4);
        textPaint.setTextSize(size);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        setMeasuredDimension(ScreenWidth * 2, ScreenHeight * 2);
    }

    public void SetList(List<ScanResult> l) {
        list = l;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);

        String SSID = "";
        int Rssi = 0;

        int px = (int) (ScreenWidth / 2 - phone.getWidth() * scale / 2);
        int py = (int) (ScreenHeight / 2 - phone.getHeight() * scale / 2);
        canvas.save();
        canvas.translate(px, py);
        canvas.scale(scale, scale);
        canvas.drawBitmap(phone, 0, 0, null);
        canvas.restore();

        if (list != null) {
            int total = list.size();
            int size = total;
            if (total >= 10)
                size = 10;
            int degree = 360 / size;
            int channel = 0;
            int frequency = 0;
            for (int i = 0; i < size; ++i) {
                ScanResult ap = list.get(i);
                SSID = ap.SSID;
                Rssi = ap.level;
                frequency = ap.frequency;
                channel = getChannel(frequency);
                String BSSID = ap.BSSID;

                int d = i * degree;
                int R = getDistance(Rssi);
                double radian = (double) d * Math.PI / 180;
                int x = (int) (px + R * Math.cos(radian));
                int y = (int) (py - R * Math.sin(radian));
                drawItem(canvas, SSID, Rssi, channel, BSSID, x, y);
                int width = (int) (bitmaps[0].getWidth() * scale * 0.5);
                int height = (int) (bitmaps[0].getHeight() * scale * 0.5);
                if (manager.getConnectionInfo().getBSSID().equals(BSSID)) {
                    canvas.drawLine(px + phone.getWidth() * scale / 2, py
                            + phone.getHeight() * scale / 2, x + width, y
                            + height, linePaint_connect);
                } else {
                    canvas.drawLine(px + phone.getWidth() * scale / 2, py
                            + phone.getHeight() * scale / 2, x + width, y
                            + height, linePaint_normal);
                }
            }
        }

    }

    private void drawItem(Canvas canvas, String ssid, int rssi, int channel,
                          String BSSID, int x, int y) {

        canvas.save();
        canvas.translate(x, y);
        canvas.scale(scale, scale);
        int index = getBitmapIndex(rssi);
        Bitmap bitmap = bitmaps[index];
        canvas.drawBitmap(bitmap, 0, 0, null);
        canvas.restore();

        int width = (int) (bitmap.getWidth() * scale);
        canvas.drawText(ssid, x + width + 50, y, textPaint);
        canvas.drawText(rssi + "dBm", x + width + 50,
                y + textPaint.getTextSize(), textPaint);
        canvas.drawText("channel:"+channel + "", x + width + 50,
                y + textPaint.getTextSize() * 2, textPaint);
        canvas.drawText(BSSID, x + width + 50, y + textPaint.getTextSize() * 3,
                textPaint);
    }

    private int getBitmapIndex(int rssi) {
        rssi = Math.abs(rssi);
        int ret = 3;

        if (rssi >= 10 && rssi < 40)
            ret = 0;
        else if (rssi >= 40 && rssi < 60)
            ret = 1;
        else if (rssi >= 60 && rssi < 70)
            ret = 2;
        else if (rssi >= 70 && rssi < 90)
            ret = 3;

        return ret;
    }

    private int getChannel(int frequency) {
        if (frequency <= 2472) {
            int d = frequency - 2412;
            d /= 5;
            return 1 + d;
        } else if (frequency == 2484) {
            return 14;
        } else if (frequency >= 5745) {
            int d = frequency - 5745;
            d /= 20;
            return 149 + 4 * d;
        } else
            return 0;
    }

    private int getDistance(int level) {
        level = Math.abs(level);
        double d = ((80 - level) / 80.0);
        d = 1 - d;
        int min = (int) (Math.min(ScreenWidth - phone.getWidth() * scale,
                ScreenHeight - phone.getHeight() * scale) / 2);
        // int max = (int) Math.max(bitmaps[0].getWidth() * 0.1,
        // bitmaps[0].getHeight() * 0.1);
        // min -= max;
        return (int) ((int) (min * d) + phone.getWidth() * scale / 2);
    }
}