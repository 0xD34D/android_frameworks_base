/*
 * Copyright (C) 2007 The Android Open Source Project
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

<<<<<<< HEAD

=======
 
>>>>>>> 54b6cfa... Initial Contribution
package android.app;

import android.content.Context;
import android.os.Binder;
import android.os.RemoteException;
import android.os.IBinder;
import android.os.ServiceManager;
<<<<<<< HEAD
import android.util.Slog;
import android.view.View;

import com.android.internal.statusbar.IStatusBarService;
=======
>>>>>>> 54b6cfa... Initial Contribution

/**
 * Allows an app to control the status bar.
 *
 * @hide
 */
public class StatusBarManager {
<<<<<<< HEAD

    public static final int DISABLE_EXPAND = View.STATUS_BAR_DISABLE_EXPAND;
    public static final int DISABLE_NOTIFICATION_ICONS = View.STATUS_BAR_DISABLE_NOTIFICATION_ICONS;
    public static final int DISABLE_NOTIFICATION_ALERTS
            = View.STATUS_BAR_DISABLE_NOTIFICATION_ALERTS;
    public static final int DISABLE_NOTIFICATION_TICKER
            = View.STATUS_BAR_DISABLE_NOTIFICATION_TICKER;
    public static final int DISABLE_SYSTEM_INFO = View.STATUS_BAR_DISABLE_SYSTEM_INFO;
    public static final int DISABLE_HOME = View.STATUS_BAR_DISABLE_HOME;
    public static final int DISABLE_RECENT = View.STATUS_BAR_DISABLE_RECENT;
    public static final int DISABLE_BACK = View.STATUS_BAR_DISABLE_BACK;
    public static final int DISABLE_CLOCK = View.STATUS_BAR_DISABLE_CLOCK;

    @Deprecated
    public static final int DISABLE_NAVIGATION = 
            View.STATUS_BAR_DISABLE_HOME | View.STATUS_BAR_DISABLE_RECENT;

    public static final int DISABLE_NONE = 0x00000000;

    public static final int DISABLE_MASK = DISABLE_EXPAND | DISABLE_NOTIFICATION_ICONS
            | DISABLE_NOTIFICATION_ALERTS | DISABLE_NOTIFICATION_TICKER
            | DISABLE_SYSTEM_INFO | DISABLE_RECENT | DISABLE_HOME | DISABLE_BACK | DISABLE_CLOCK;

    public static final int NAVIGATION_HINT_BACK_NOP      = 1 << 0;
    public static final int NAVIGATION_HINT_HOME_NOP      = 1 << 1;
    public static final int NAVIGATION_HINT_RECENT_NOP    = 1 << 2;
    public static final int NAVIGATION_HINT_BACK_ALT      = 1 << 3;

    private Context mContext;
    private IStatusBarService mService;
=======
    /**
     * Flag for {@link #disable} to make the status bar not expandable.  Unless you also
     * set {@link #DISABLE_NOTIFICATIONS}, new notifications will continue to show.
     */
    public static final int DISABLE_EXPAND = 0x00000001;

    /**
     * Flag for {@link #disable} to hide notification icons and ticker text.
     */
    public static final int DISABLE_NOTIFICATION_ICONS = 0x00000002;

    /**
     * Flag for {@link #disable} to disable incoming notification alerts.  This will not block
     * icons, but it will block sound, vibrating and other visual or aural notifications.
     */
    public static final int DISABLE_NOTIFICATION_ALERTS = 0x00000004;

    /**
     * Re-enable all of the status bar features that you've disabled.
     */
    public static final int DISABLE_NONE = 0x00000000;

    private Context mContext;
    private IStatusBar mService;
>>>>>>> 54b6cfa... Initial Contribution
    private IBinder mToken = new Binder();

    StatusBarManager(Context context) {
        mContext = context;
<<<<<<< HEAD
    }

    private synchronized IStatusBarService getService() {
        if (mService == null) {
            mService = IStatusBarService.Stub.asInterface(
                    ServiceManager.getService(Context.STATUS_BAR_SERVICE));
            if (mService == null) {
                Slog.w("StatusBarManager", "warning: no STATUS_BAR_SERVICE");
            }
        }
        return mService;
=======
        mService = IStatusBar.Stub.asInterface(
                ServiceManager.getService(Context.STATUS_BAR_SERVICE));
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Disable some features in the status bar.  Pass the bitwise-or of the DISABLE_* flags.
     * To re-enable everything, pass {@link #DISABLE_NONE}.
     */
    public void disable(int what) {
        try {
<<<<<<< HEAD
            final IStatusBarService svc = getService();
            if (svc != null) {
                svc.disable(what, mToken, mContext.getPackageName());
            }
=======
            mService.disable(what, mToken, mContext.getPackageName());
>>>>>>> 54b6cfa... Initial Contribution
        } catch (RemoteException ex) {
            // system process is dead anyway.
            throw new RuntimeException(ex);
        }
    }
    
    /**
     * Expand the status bar.
     */
    public void expand() {
        try {
<<<<<<< HEAD
            final IStatusBarService svc = getService();
            if (svc != null) {
                svc.expand();
            }
=======
            mService.activate();
>>>>>>> 54b6cfa... Initial Contribution
        } catch (RemoteException ex) {
            // system process is dead anyway.
            throw new RuntimeException(ex);
        }
    }
    
    /**
     * Collapse the status bar.
     */
    public void collapse() {
        try {
<<<<<<< HEAD
            final IStatusBarService svc = getService();
            if (svc != null) {
                svc.collapse();
            }
=======
            mService.deactivate();
        } catch (RemoteException ex) {
            // system process is dead anyway.
            throw new RuntimeException(ex);
        }
    }
    
    /**
     * Toggle the status bar.
     */
    public void toggle() {
        try {
            mService.toggle();
>>>>>>> 54b6cfa... Initial Contribution
        } catch (RemoteException ex) {
            // system process is dead anyway.
            throw new RuntimeException(ex);
        }
    }

<<<<<<< HEAD
    public void setIcon(String slot, int iconId, int iconLevel, String contentDescription) {
        try {
            final IStatusBarService svc = getService();
            if (svc != null) {
                svc.setIcon(slot, mContext.getPackageName(), iconId, iconLevel,
                    contentDescription);
            }
=======
    public IBinder addIcon(String slot, int iconId, int iconLevel) {
        try {
            return mService.addIcon(slot, mContext.getPackageName(), iconId, iconLevel);
>>>>>>> 54b6cfa... Initial Contribution
        } catch (RemoteException ex) {
            // system process is dead anyway.
            throw new RuntimeException(ex);
        }
    }

<<<<<<< HEAD
    public void removeIcon(String slot) {
        try {
            final IStatusBarService svc = getService();
            if (svc != null) {
                svc.removeIcon(slot);
            }
=======
    public void updateIcon(IBinder key, String slot, int iconId, int iconLevel) {
        try {
            mService.updateIcon(key, slot, mContext.getPackageName(), iconId, iconLevel);
>>>>>>> 54b6cfa... Initial Contribution
        } catch (RemoteException ex) {
            // system process is dead anyway.
            throw new RuntimeException(ex);
        }
    }

<<<<<<< HEAD
    public void setIconVisibility(String slot, boolean visible) {
        try {
            final IStatusBarService svc = getService();
            if (svc != null) {
                svc.setIconVisibility(slot, visible);
            }
=======
    public void removeIcon(IBinder key) {
        try {
            mService.removeIcon(key);
>>>>>>> 54b6cfa... Initial Contribution
        } catch (RemoteException ex) {
            // system process is dead anyway.
            throw new RuntimeException(ex);
        }
    }
}
