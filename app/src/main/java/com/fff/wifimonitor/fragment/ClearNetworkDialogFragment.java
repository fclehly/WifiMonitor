package com.fff.wifimonitor.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.fff.wifimonitor.R;
import com.fff.wifimonitor.util.WifiUtil;

/**
 * Created by fwz on 2016/11/19.
 */

public class ClearNetworkDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.AreYouSureToClearNetwork);
        builder.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        });
        builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                WifiUtil myWifiUtil = WifiUtil.getInstance(getActivity());
                int netId = myWifiUtil.getWifiInfo().getNetworkId();
                myWifiUtil.getWifiManager().removeNetwork(netId);
                dismiss();
            }
        });
        return builder.create();
    }
}

