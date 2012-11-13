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

import android.content.Context;
<<<<<<< HEAD
import android.content.DialogInterface;
=======
>>>>>>> 54b6cfa... Initial Contribution
import android.os.Handler;
import android.os.Message;

class FactoryErrorDialog extends BaseErrorDialog {
    public FactoryErrorDialog(Context context, CharSequence msg) {
        super(context);
        setCancelable(false);
        setTitle(context.getText(com.android.internal.R.string.factorytest_failed));
        setMessage(msg);
<<<<<<< HEAD
        setButton(DialogInterface.BUTTON_POSITIVE,
                context.getText(com.android.internal.R.string.factorytest_reboot),
=======
        setButton(context.getText(com.android.internal.R.string.factorytest_reboot),
>>>>>>> 54b6cfa... Initial Contribution
                mHandler.obtainMessage(0));
        getWindow().setTitle("Factory Error");
    }
    
    public void onStop() {
    }

    private final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            throw new RuntimeException("Rebooting from failed factory test");
        }
    };
}
