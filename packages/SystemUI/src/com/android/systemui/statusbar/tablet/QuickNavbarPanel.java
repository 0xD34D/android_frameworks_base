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

package com.android.systemui.statusbar.tablet;

import com.android.systemui.R;
import com.android.systemui.statusbar.PieControl;
import com.android.systemui.statusbar.PieControl.OnNavButtonPressedListener;
import com.android.systemui.statusbar.BaseStatusBar;

import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManagerNative;
import android.app.IActivityManager;
import android.content.Context;
import android.graphics.Rect;
import android.hardware.input.InputManager;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Slog;
import android.view.InputDevice;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.List;

/**
 * Needed for takeScreenshot
 */
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.ComponentName;
import android.os.IBinder;
import android.os.Messenger;
import android.os.RemoteException;


public class QuickNavbarPanel extends FrameLayout implements StatusBarPanel, OnNavButtonPressedListener {
    private static final boolean DEBUG = true;
    private Handler mHandler;
    boolean mShowing;
    private PieControl mPieControl;
    private int mInjectKeycode;
    private long mDownTime;
    private Context mContext;
    private boolean mHideOnPress = false;
    private boolean mLongpressKillsApp = false;
    
    ViewGroup mContentFrame;
    Rect mContentArea = new Rect();

    private TabletStatusBar mStatusBar;

    public QuickNavbarPanel(Context context) {
        this(context, null);
    }

    public QuickNavbarPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mPieControl = new PieControl(context);
        mPieControl.setOnNavButtonPressedListener(this);
    }

    public void setBar(BaseStatusBar phoneStatusBar) {
        mStatusBar = (TabletStatusBar)phoneStatusBar;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mStatusBar.updateAutoHideTimer();
        return mPieControl.onTouchEvent(event);
        //return super.onTouchEvent(event);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onAttachedToWindow () {
        super.onAttachedToWindow();
    }

    public void setHandler(Handler h) {
        mHandler = h;
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();

        mContentFrame = (ViewGroup)findViewById(R.id.content_frame);
        setWillNotDraw(false);
        mShowing = false;
        mPieControl.attachToContainer(this);
        mPieControl.forceToTop(this);
    }

    /**
     * Whether the panel is showing, or, if it's animating, whether it will be
     * when the animation is done.
     */
    public boolean isShowing() {
        return mShowing;
    }

    public void show(boolean show, boolean animate) {
        mShowing = show;
        setVisibility(show ? View.VISIBLE : View.GONE);
        if (show) {
            mPieControl.setNotificationsCount(mStatusBar.getNotificationCount());
            mPieControl.setCenter(this.getWidth() / 2, this.getHeight());
        }
    
        mPieControl.show(show);
    }

    public boolean isInContentArea(int x, int y) {
        mContentArea.left = mContentFrame.getLeft() + mContentFrame.getPaddingLeft();
        mContentArea.top = mContentFrame.getTop() + mContentFrame.getPaddingTop();
        mContentArea.right = mContentFrame.getRight() - mContentFrame.getPaddingRight();
        mContentArea.bottom = mContentFrame.getBottom() - mContentFrame.getPaddingBottom();

        return mContentArea.contains(x, y);
    }

    public void setHideOnPress(boolean hide) {
        mHideOnPress = hide;
    }

    public void setLongpressKillsApp(boolean kill) {
        mLongpressKillsApp = kill;
    }

    public void onNavButtonPressed(String buttonName) {
        if (buttonName.equals(PieControl.BACK_BUTTON)) {
            injectKeyDelayed(KeyEvent.KEYCODE_BACK);
        } else if (buttonName.equals(PieControl.HOME_BUTTON)) {
            injectKeyDelayed(KeyEvent.KEYCODE_HOME);
        } else if (buttonName.equals(PieControl.MENU_BUTTON)) {
            injectKeyDelayed(KeyEvent.KEYCODE_MENU);
        } else if (buttonName.equals(PieControl.SEARCH_BUTTON)) {
            injectKeyDelayed(KeyEvent.KEYCODE_SEARCH);
        } else if (buttonName.equals(PieControl.RECENT_BUTTON)) {
            Message peekMsg = mHandler.obtainMessage(TabletStatusBar.MSG_TOGGLE_RECENTS_PANEL);
            mHandler.sendMessage(peekMsg);
        } else if (buttonName.equals(PieControl.NOTIFICATION_BUTTON)) {
            Message peekMsg = mHandler.obtainMessage(TabletStatusBar.MSG_OPEN_NOTIFICATION_PANEL);
            mHandler.sendMessage(peekMsg);
        } else if (buttonName.equals(PieControl.SETTINGS_BUTTON)) {
            Message peekMsg = mHandler.obtainMessage(TabletStatusBar.MSG_OPEN_SETTINGS_PANEL);
            mHandler.sendMessage(peekMsg);
        } else if (buttonName.equals(PieControl.SCREENSHOT_BUTTON)) {
            takeScreenshot();
            show(false, false);
        }
        if (mHideOnPress)
            show(false, false);
    }

    public void onNavButtonLongPressed(String buttonName) {
        if (buttonName.equals(PieControl.BACK_BUTTON)) {
            if (mLongpressKillsApp)
                postDelayed(mKillTask, 0);
        }
        if (mHideOnPress)
            show(false, false);
    }

    public void injectKeyDelayed(int keycode){
    	mInjectKeycode = keycode;
        mDownTime = SystemClock.uptimeMillis();
    	mHandler.removeCallbacks(onInjectKeyDelayed);
      	mHandler.postDelayed(onInjectKeyDelayed, 100);
    }

    final Runnable onInjectKeyDelayed = new Runnable() {
    	public void run() {
            final long eventTime = SystemClock.uptimeMillis();
            InputManager.getInstance().injectInputEvent(
                    new KeyEvent(mDownTime, eventTime - 100, KeyEvent.ACTION_DOWN, mInjectKeycode, 0,
                            0, KeyCharacterMap.VIRTUAL_KEYBOARD, 0,
                            KeyEvent.FLAG_FROM_SYSTEM | KeyEvent.FLAG_VIRTUAL_HARD_KEY,
                            InputDevice.SOURCE_KEYBOARD),
                        InputManager.INJECT_INPUT_EVENT_MODE_ASYNC);
            InputManager.getInstance().injectInputEvent(
                    new KeyEvent(mDownTime, eventTime - 50, KeyEvent.ACTION_UP, mInjectKeycode, 0,
                            0, KeyCharacterMap.VIRTUAL_KEYBOARD, 0,
                            KeyEvent.FLAG_FROM_SYSTEM | KeyEvent.FLAG_VIRTUAL_HARD_KEY,
                            InputDevice.SOURCE_KEYBOARD),
                        InputManager.INJECT_INPUT_EVENT_MODE_ASYNC);
    	}
    };

    Runnable mKillTask = new Runnable() {
        public void run() {
            try {
                IActivityManager mgr = ActivityManagerNative.getDefault();
                List<RunningAppProcessInfo> apps = mgr.getRunningAppProcesses();
                final int myPid = Process.myPid();
                for (RunningAppProcessInfo appInfo : apps) {
                    int uid = appInfo.uid;
                    // Make sure it's a foreground user application (not system,
                    // root, phone, etc.)
                    if (uid >= Process.FIRST_APPLICATION_UID && uid <= Process.LAST_APPLICATION_UID
                            && appInfo.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                            && appInfo.pid != myPid) {
                        // Kill the entire pid
                        if (appInfo.pkgList != null && (apps.size() > 0)) {
                            mgr.forceStopPackage(appInfo.pkgList[0]);
                        } else {
                            Process.killProcess(appInfo.pid);
                        }
                        Toast.makeText(getContext(), 
                                String.format("Killed %s", appInfo.processName), Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            } catch (RemoteException remoteException) {
                // Do nothing; just let it go.
            }
        }
    };

    /**
    * functions needed for taking screenhots.  
    * This leverages the built in ICS screenshot functionality 
    */
   final Object mScreenshotLock = new Object();
   ServiceConnection mScreenshotConnection = null;

   final Runnable mScreenshotTimeout = new Runnable() {
       @Override public void run() {
           synchronized (mScreenshotLock) {
               if (mScreenshotConnection != null) {
                   mContext.unbindService(mScreenshotConnection);
                   mScreenshotConnection = null;
               }
           }
       }
   };

   private void takeScreenshot() {
       synchronized (mScreenshotLock) {
           if (mScreenshotConnection != null) {
               return;
           }
           ComponentName cn = new ComponentName("com.android.systemui",
                   "com.android.systemui.screenshot.TakeScreenshotService");
           Intent intent = new Intent();
           intent.setComponent(cn);
           ServiceConnection conn = new ServiceConnection() {
               @Override
               public void onServiceConnected(ComponentName name, IBinder service) {
                   synchronized (mScreenshotLock) {
                       if (mScreenshotConnection != this) {
                           return;
                       }
                       Messenger messenger = new Messenger(service);
                       Message msg = Message.obtain(null, 1);
                       final ServiceConnection myConn = this;
                       Handler h = new Handler(mHandler.getLooper()) {
                           @Override
                           public void handleMessage(Message msg) {
                               synchronized (mScreenshotLock) {
                                   if (mScreenshotConnection == myConn) {
                                       mContext.unbindService(mScreenshotConnection);
                                       mScreenshotConnection = null;
                                       mHandler.removeCallbacks(mScreenshotTimeout);
                                   }
                               }
                           }
                       };
                       msg.replyTo = new Messenger(h);
                       msg.arg1 = msg.arg2 = 0;

                       /* wait for the panel to close */
                       try {
                           Thread.sleep(500); 
                       } catch (InterruptedException ie) {
                       }
                       
                       /* take the screenshot */
                       try {
                           messenger.send(msg);
                       } catch (RemoteException e) {
                       }
                   }
               }
               @Override
               public void onServiceDisconnected(ComponentName name) {}
           };
           if (mContext.bindService(intent, conn, Context.BIND_AUTO_CREATE)) {
               mScreenshotConnection = conn;
               mHandler.postDelayed(mScreenshotTimeout, 10000);
           }
       }
   }
}
