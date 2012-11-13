/*
 * Copyright (C) 2008 The Android Open Source Project
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

package android.widget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
<<<<<<< HEAD
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
=======
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnLongClickListener;

>>>>>>> 54b6cfa... Initial Contribution

public class ZoomButton extends ImageButton implements OnLongClickListener {

    private final Handler mHandler;
    private final Runnable mRunnable = new Runnable() {
        public void run() {
<<<<<<< HEAD
            if (hasOnClickListeners() && mIsInLongpress && isEnabled()) {
                callOnClick();
=======
            if ((mOnClickListener != null) && mIsInLongpress && isEnabled()) {
                mOnClickListener.onClick(ZoomButton.this);
>>>>>>> 54b6cfa... Initial Contribution
                mHandler.postDelayed(this, mZoomSpeed);
            }
        }
    };
<<<<<<< HEAD
=======
    private final GestureDetector mGestureDetector;
>>>>>>> 54b6cfa... Initial Contribution
    
    private long mZoomSpeed = 1000;
    private boolean mIsInLongpress;
    
    public ZoomButton(Context context) {
        this(context, null);
    }

    public ZoomButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public ZoomButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mHandler = new Handler();
<<<<<<< HEAD
=======
        mGestureDetector = new GestureDetector(new SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent e) {
                onLongClick(ZoomButton.this);
            }
        });
>>>>>>> 54b6cfa... Initial Contribution
        setOnLongClickListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
<<<<<<< HEAD
=======
        mGestureDetector.onTouchEvent(event);
>>>>>>> 54b6cfa... Initial Contribution
        if ((event.getAction() == MotionEvent.ACTION_CANCEL)
                || (event.getAction() == MotionEvent.ACTION_UP)) {
            mIsInLongpress = false;
        }
        return super.onTouchEvent(event);
    }
        
    public void setZoomSpeed(long speed) {
        mZoomSpeed = speed;
    }

    public boolean onLongClick(View v) {
        mIsInLongpress = true;
        mHandler.post(mRunnable);
        return true;
    }
        
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        mIsInLongpress = false;
        return super.onKeyUp(keyCode, event);
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        if (!enabled) {
            
            /* If we're being disabled reset the state back to unpressed
             * as disabled views don't get events and therefore we won't
             * get the up event to reset the state.
             */
            setPressed(false);
        }
        super.setEnabled(enabled);
    }
    
    @Override
    public boolean dispatchUnhandledMove(View focused, int direction) {
        clearFocus();
        return super.dispatchUnhandledMove(focused, direction);
    }
<<<<<<< HEAD

    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(ZoomButton.class.getName());
    }

    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(ZoomButton.class.getName());
    }
=======
>>>>>>> 54b6cfa... Initial Contribution
}
