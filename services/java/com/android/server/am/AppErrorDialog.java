/*
 * Copyright (C) 2006 The Android Open Source Project
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

package com.android.server.am;

import static android.view.WindowManager.LayoutParams.FLAG_SYSTEM_ERROR;

import android.content.Context;
<<<<<<< HEAD
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.util.Slog;
import android.view.WindowManager;

class AppErrorDialog extends BaseErrorDialog {
    private final static String TAG = "AppErrorDialog";

=======
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;

class AppErrorDialog extends BaseErrorDialog {
>>>>>>> 54b6cfa... Initial Contribution
    private final AppErrorResult mResult;
    private final ProcessRecord mProc;

    // Event 'what' codes
    static final int FORCE_QUIT = 0;
<<<<<<< HEAD
    static final int FORCE_QUIT_AND_REPORT = 1;
=======
    static final int DEBUG = 1;
>>>>>>> 54b6cfa... Initial Contribution

    // 5-minute timeout, then we automatically dismiss the crash dialog
    static final long DISMISS_TIMEOUT = 1000 * 60 * 5;
    
<<<<<<< HEAD
    public AppErrorDialog(Context context, AppErrorResult result, ProcessRecord app) {
=======
    public AppErrorDialog(Context context, AppErrorResult result,
            ProcessRecord app, int flags,
            String shortMsg, String longMsg) {
>>>>>>> 54b6cfa... Initial Contribution
        super(context);
        
        Resources res = context.getResources();
        
        mProc = app;
        mResult = result;
        CharSequence name;
<<<<<<< HEAD
        if ((app.pkgList.size() == 1) &&
=======
        if (app.uniquePackage != null &&
>>>>>>> 54b6cfa... Initial Contribution
                (name=context.getPackageManager().getApplicationLabel(app.info)) != null) {
            setMessage(res.getString(
                    com.android.internal.R.string.aerr_application,
                    name.toString(), app.info.processName));
        } else {
            name = app.processName;
            setMessage(res.getString(
                    com.android.internal.R.string.aerr_process,
                    name.toString()));
        }

        setCancelable(false);

<<<<<<< HEAD
        setButton(DialogInterface.BUTTON_POSITIVE,
                res.getText(com.android.internal.R.string.force_close),
                mHandler.obtainMessage(FORCE_QUIT));

        if (app.errorReportReceiver != null) {
            setButton(DialogInterface.BUTTON_NEGATIVE,
                    res.getText(com.android.internal.R.string.report),
                    mHandler.obtainMessage(FORCE_QUIT_AND_REPORT));
        }

        setTitle(res.getText(com.android.internal.R.string.aerr_title));
        getWindow().addFlags(FLAG_SYSTEM_ERROR);
        getWindow().setTitle("Application Error: " + app.info.processName);
        if (app.persistent) {
            getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ERROR);
        }
=======
        setButton(res.getText(com.android.internal.R.string.force_close),
                    mHandler.obtainMessage(FORCE_QUIT));
        if ((flags&1) != 0) {
            setButton(res.getText(com.android.internal.R.string.debug),
                    mHandler.obtainMessage(DEBUG));
        }
        setTitle(res.getText(com.android.internal.R.string.aerr_title));
        getWindow().addFlags(FLAG_SYSTEM_ERROR);
        getWindow().setTitle("Application Error: " + app.info.processName);
>>>>>>> 54b6cfa... Initial Contribution

        // After the timeout, pretend the user clicked the quit button
        mHandler.sendMessageDelayed(
                mHandler.obtainMessage(FORCE_QUIT),
                DISMISS_TIMEOUT);
    }
<<<<<<< HEAD
=======
    
    public void onStop() {
    }
>>>>>>> 54b6cfa... Initial Contribution

    private final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            synchronized (mProc) {
                if (mProc != null && mProc.crashDialog == AppErrorDialog.this) {
                    mProc.crashDialog = null;
                }
            }
            mResult.set(msg.what);

            // If this is a timeout we won't be automatically closed, so go
            // ahead and explicitly dismiss ourselves just in case.
            dismiss();
        }
    };
}
