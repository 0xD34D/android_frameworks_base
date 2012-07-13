/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.systemui.statusbar.policy;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.util.Slog;
import android.widget.CompoundButton;

public class WifiModeController extends BroadcastReceiver
        implements CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "StatusBar.WifiModeController";

    private Context mContext;
    private CompoundButton mCheckBox;

    private boolean mWifiEnabled;
    private final WifiManager mWifiManager;

    public WifiModeController(Context context, CompoundButton checkbox) {
        mContext = context;
        mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        mWifiEnabled = getWifiEnabled();
        mCheckBox = checkbox;
        checkbox.setChecked(mWifiEnabled);
        checkbox.setOnCheckedChangeListener(this);

        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        context.registerReceiver(this, filter);

    }

    public void release() {
        mContext.unregisterReceiver(this);
    }

    public void onCheckedChanged(CompoundButton view, boolean checked) {
        if (checked != mWifiEnabled) {
            mWifiEnabled = checked;
  			if (mWifiManager.setWifiEnabled(checked)) {
            	// Intent has been taken into account, disable until new state is active
			    mCheckBox.setEnabled(false);
			}
        }
    }

    public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {
                handleWifiStateChanged(intent.getIntExtra(
                        WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN));
            }
    }

    private boolean getWifiEnabled() {
        final int wifiState = mWifiManager.getWifiState();
        boolean isEnabled = wifiState == WifiManager.WIFI_STATE_ENABLED;
        //boolean isDisabled = wifiState == WifiManager.WIFI_STATE_DISABLED;
		return isEnabled;
    }

    private void handleWifiStateChanged(int state) {
        switch (state) {
            case WifiManager.WIFI_STATE_ENABLING:
                mCheckBox.setEnabled(false);
                break;
            case WifiManager.WIFI_STATE_ENABLED:
                mCheckBox.setChecked(true);
                mCheckBox.setEnabled(true);
                break;
            case WifiManager.WIFI_STATE_DISABLING:
                mCheckBox.setEnabled(false);
                break;
            case WifiManager.WIFI_STATE_DISABLED:
                mCheckBox.setChecked(false);
                mCheckBox.setEnabled(true);
                break;
            default:
                mCheckBox.setChecked(false);
                mCheckBox.setEnabled(true);
                break;
        }
    }
}

