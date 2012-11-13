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

package android.widget;

import android.app.INotificationManager;
import android.app.ITransientNotification;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.PixelFormat;
<<<<<<< HEAD
import android.os.Handler;
import android.os.RemoteException;
=======
import android.os.RemoteException;
import android.os.Handler;
>>>>>>> 54b6cfa... Initial Contribution
import android.os.ServiceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManagerImpl;
<<<<<<< HEAD
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
=======
>>>>>>> 54b6cfa... Initial Contribution

/**
 * A toast is a view containing a quick little message for the user.  The toast class
 * helps you create and show those.
 * {@more}
 *
 * <p>
 * When the view is shown to the user, appears as a floating view over the
 * application.  It will never receive focus.  The user will probably be in the
 * middle of typing something else.  The idea is to be as unobtrusive as
 * possible, while still showing the user the information you want them to see.
 * Two examples are the volume control, and the brief message saying that your
 * settings have been saved.
 * <p>
 * The easiest way to use this class is to call one of the static methods that constructs
 * everything you need and returns a new Toast object.
<<<<<<< HEAD
 *
 * <div class="special reference">
 * <h3>Developer Guides</h3>
 * <p>For information about creating Toast notifications, read the
 * <a href="{@docRoot}guide/topics/ui/notifiers/toasts.html">Toast Notifications</a> developer
 * guide.</p>
 * </div>
=======
>>>>>>> 54b6cfa... Initial Contribution
 */ 
public class Toast {
    static final String TAG = "Toast";
    static final boolean localLOGV = false;

    /**
     * Show the view or text notification for a short period of time.  This time
     * could be user-definable.  This is the default.
     * @see #setDuration
     */
    public static final int LENGTH_SHORT = 0;

    /**
     * Show the view or text notification for a long period of time.  This time
     * could be user-definable.
     * @see #setDuration
     */
    public static final int LENGTH_LONG = 1;

    final Context mContext;
    final TN mTN;
    int mDuration;
<<<<<<< HEAD
=======
    int mGravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
    int mX, mY;
    float mHorizontalMargin;
    float mVerticalMargin;
    View mView;
>>>>>>> 54b6cfa... Initial Contribution
    View mNextView;

    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context  The context to use.  Usually your {@link android.app.Application}
     *                 or {@link android.app.Activity} object.
     */
    public Toast(Context context) {
        mContext = context;
<<<<<<< HEAD
        mTN = new TN();
        mTN.mY = context.getResources().getDimensionPixelSize(
=======
        mTN = new TN(context);
        mY = context.getResources().getDimensionPixelSize(
>>>>>>> 54b6cfa... Initial Contribution
                com.android.internal.R.dimen.toast_y_offset);
    }
    
    /**
     * Show the view for the specified duration.
     */
    public void show() {
        if (mNextView == null) {
            throw new RuntimeException("setView must have been called");
        }

        INotificationManager service = getService();
<<<<<<< HEAD
        String pkg = mContext.getPackageName();
        TN tn = mTN;
        tn.mNextView = mNextView;
=======

        String pkg = mContext.getPackageName();

        TN tn = mTN;
>>>>>>> 54b6cfa... Initial Contribution

        try {
            service.enqueueToast(pkg, tn, mDuration);
        } catch (RemoteException e) {
            // Empty
        }
    }

    /**
     * Close the view if it's showing, or don't show it if it isn't showing yet.
     * You do not normally have to call this.  Normally view will disappear on its own
     * after the appropriate duration.
     */
    public void cancel() {
        mTN.hide();
<<<<<<< HEAD

        try {
            getService().cancelToast(mContext.getPackageName(), mTN);
        } catch (RemoteException e) {
            // Empty
        }
=======
        // TODO this still needs to cancel the inflight notification if any
>>>>>>> 54b6cfa... Initial Contribution
    }
    
    /**
     * Set the view to show.
     * @see #getView
     */
    public void setView(View view) {
        mNextView = view;
    }

    /**
     * Return the view.
     * @see #setView
     */
    public View getView() {
        return mNextView;
    }

    /**
     * Set how long to show the view for.
     * @see #LENGTH_SHORT
     * @see #LENGTH_LONG
     */
    public void setDuration(int duration) {
        mDuration = duration;
    }

    /**
     * Return the duration.
     * @see #setDuration
     */
    public int getDuration() {
        return mDuration;
    }
    
    /**
     * Set the margins of the view.
     *
     * @param horizontalMargin The horizontal margin, in percentage of the
     *        container width, between the container's edges and the
     *        notification
     * @param verticalMargin The vertical margin, in percentage of the
     *        container height, between the container's edges and the
     *        notification
     */
    public void setMargin(float horizontalMargin, float verticalMargin) {
<<<<<<< HEAD
        mTN.mHorizontalMargin = horizontalMargin;
        mTN.mVerticalMargin = verticalMargin;
=======
        mHorizontalMargin = horizontalMargin;
        mVerticalMargin = verticalMargin;
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Return the horizontal margin.
     */
    public float getHorizontalMargin() {
<<<<<<< HEAD
        return mTN.mHorizontalMargin;
=======
        return mHorizontalMargin;
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Return the vertical margin.
     */
    public float getVerticalMargin() {
<<<<<<< HEAD
        return mTN.mVerticalMargin;
=======
        return mVerticalMargin;
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Set the location at which the notification should appear on the screen.
     * @see android.view.Gravity
     * @see #getGravity
     */
    public void setGravity(int gravity, int xOffset, int yOffset) {
<<<<<<< HEAD
        mTN.mGravity = gravity;
        mTN.mX = xOffset;
        mTN.mY = yOffset;
=======
        mGravity = gravity;
        mX = xOffset;
        mY = yOffset;
>>>>>>> 54b6cfa... Initial Contribution
    }

     /**
     * Get the location at which the notification should appear on the screen.
     * @see android.view.Gravity
     * @see #getGravity
     */
    public int getGravity() {
<<<<<<< HEAD
        return mTN.mGravity;
=======
        return mGravity;
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Return the X offset in pixels to apply to the gravity's location.
     */
    public int getXOffset() {
<<<<<<< HEAD
        return mTN.mX;
=======
        return mX;
>>>>>>> 54b6cfa... Initial Contribution
    }
    
    /**
     * Return the Y offset in pixels to apply to the gravity's location.
     */
    public int getYOffset() {
<<<<<<< HEAD
        return mTN.mY;
=======
        return mY;
>>>>>>> 54b6cfa... Initial Contribution
    }
    
    /**
     * Make a standard toast that just contains a text view.
     *
     * @param context  The context to use.  Usually your {@link android.app.Application}
     *                 or {@link android.app.Activity} object.
     * @param text     The text to show.  Can be formatted text.
     * @param duration How long to display the message.  Either {@link #LENGTH_SHORT} or
     *                 {@link #LENGTH_LONG}
     *
     */
    public static Toast makeText(Context context, CharSequence text, int duration) {
        Toast result = new Toast(context);

<<<<<<< HEAD
        LayoutInflater inflate = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
=======
        LayoutInflater inflate = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
>>>>>>> 54b6cfa... Initial Contribution
        View v = inflate.inflate(com.android.internal.R.layout.transient_notification, null);
        TextView tv = (TextView)v.findViewById(com.android.internal.R.id.message);
        tv.setText(text);
        
        result.mNextView = v;
        result.mDuration = duration;

        return result;
    }

    /**
     * Make a standard toast that just contains a text view with the text from a resource.
     *
     * @param context  The context to use.  Usually your {@link android.app.Application}
     *                 or {@link android.app.Activity} object.
     * @param resId    The resource id of the string resource to use.  Can be formatted text.
     * @param duration How long to display the message.  Either {@link #LENGTH_SHORT} or
     *                 {@link #LENGTH_LONG}
     *
     * @throws Resources.NotFoundException if the resource can't be found.
     */
    public static Toast makeText(Context context, int resId, int duration)
                                throws Resources.NotFoundException {
        return makeText(context, context.getResources().getText(resId), duration);
    }

    /**
     * Update the text in a Toast that was previously created using one of the makeText() methods.
     * @param resId The new text for the Toast.
     */
    public void setText(int resId) {
        setText(mContext.getText(resId));
    }
    
    /**
     * Update the text in a Toast that was previously created using one of the makeText() methods.
     * @param s The new text for the Toast.
     */
    public void setText(CharSequence s) {
        if (mNextView == null) {
            throw new RuntimeException("This Toast was not created with Toast.makeText()");
        }
        TextView tv = (TextView) mNextView.findViewById(com.android.internal.R.id.message);
        if (tv == null) {
            throw new RuntimeException("This Toast was not created with Toast.makeText()");
        }
        tv.setText(s);
    }
<<<<<<< HEAD

=======
    
>>>>>>> 54b6cfa... Initial Contribution
    // =======================================================================================
    // All the gunk below is the interaction with the Notification Service, which handles
    // the proper ordering of these system-wide.
    // =======================================================================================

    private static INotificationManager sService;

<<<<<<< HEAD
    static private INotificationManager getService() {
=======
    static private INotificationManager getService()
    {
>>>>>>> 54b6cfa... Initial Contribution
        if (sService != null) {
            return sService;
        }
        sService = INotificationManager.Stub.asInterface(ServiceManager.getService("notification"));
        return sService;
    }

<<<<<<< HEAD
    private static class TN extends ITransientNotification.Stub {
        final Runnable mShow = new Runnable() {
            public void run() {
                handleShow();
            }
        };

        final Runnable mHide = new Runnable() {
            public void run() {
                handleHide();
                // Don't do this in handleHide() because it is also invoked by handleShow()
                mNextView = null;
            }
        };

        private final WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
        final Handler mHandler = new Handler();    

        int mGravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
        int mX, mY;
        float mHorizontalMargin;
        float mVerticalMargin;

       
        View mView;
        View mNextView;
        
        WindowManagerImpl mWM;

        TN() {
            // XXX This should be changed to use a Dialog, with a Theme.Toast
            // defined that sets up the layout params appropriately.
            final WindowManager.LayoutParams params = mParams;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.width = WindowManager.LayoutParams.WRAP_CONTENT;
            params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
            params.format = PixelFormat.TRANSLUCENT;
            params.windowAnimations = com.android.internal.R.style.Animation_Toast;
            params.type = WindowManager.LayoutParams.TYPE_TOAST;
            params.setTitle("Toast");
=======
    private class TN extends ITransientNotification.Stub
    {
        TN(Context context)
        {
            // XXX This should be changed to use a Dialog, with a Theme.Toast
            // defined that sets up the layout params appropriately.
            mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
            mParams.format = PixelFormat.TRANSLUCENT;
            mParams.windowAnimations = com.android.internal.R.style.Animation_Toast;
            mParams.type = WindowManager.LayoutParams.TYPE_TOAST;
            mParams.setTitle("Toast");
>>>>>>> 54b6cfa... Initial Contribution
        }

        /**
         * schedule handleShow into the right thread
         */
<<<<<<< HEAD
        public void show() {
=======
        public void show()
        {
>>>>>>> 54b6cfa... Initial Contribution
            if (localLOGV) Log.v(TAG, "SHOW: " + this);
            mHandler.post(mShow);
        }

        /**
         * schedule handleHide into the right thread
         */
<<<<<<< HEAD
        public void hide() {
=======
        public void hide()
        {
>>>>>>> 54b6cfa... Initial Contribution
            if (localLOGV) Log.v(TAG, "HIDE: " + this);
            mHandler.post(mHide);
        }

<<<<<<< HEAD
        public void handleShow() {
=======
        public void handleShow()
        {
>>>>>>> 54b6cfa... Initial Contribution
            if (localLOGV) Log.v(TAG, "HANDLE SHOW: " + this + " mView=" + mView
                    + " mNextView=" + mNextView);
            if (mView != mNextView) {
                // remove the old view if necessary
                handleHide();
                mView = mNextView;
                mWM = WindowManagerImpl.getDefault();
                final int gravity = mGravity;
                mParams.gravity = gravity;
                if ((gravity & Gravity.HORIZONTAL_GRAVITY_MASK) == Gravity.FILL_HORIZONTAL) {
                    mParams.horizontalWeight = 1.0f;
                }
                if ((gravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.FILL_VERTICAL) {
                    mParams.verticalWeight = 1.0f;
                }
                mParams.x = mX;
                mParams.y = mY;
                mParams.verticalMargin = mVerticalMargin;
                mParams.horizontalMargin = mHorizontalMargin;
                if (mView.getParent() != null) {
<<<<<<< HEAD
                    if (localLOGV) Log.v(TAG, "REMOVE! " + mView + " in " + this);
=======
                    if (localLOGV) Log.v(
                            TAG, "REMOVE! " + mView + " in " + this);
>>>>>>> 54b6cfa... Initial Contribution
                    mWM.removeView(mView);
                }
                if (localLOGV) Log.v(TAG, "ADD! " + mView + " in " + this);
                mWM.addView(mView, mParams);
<<<<<<< HEAD
                trySendAccessibilityEvent();
            }
        }

        private void trySendAccessibilityEvent() {
            AccessibilityManager accessibilityManager =
                    AccessibilityManager.getInstance(mView.getContext());
            if (!accessibilityManager.isEnabled()) {
                return;
            }
            // treat toasts as notifications since they are used to
            // announce a transient piece of information to the user
            AccessibilityEvent event = AccessibilityEvent.obtain(
                    AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED);
            event.setClassName(getClass().getName());
            event.setPackageName(mView.getContext().getPackageName());
            mView.dispatchPopulateAccessibilityEvent(event);
            accessibilityManager.sendAccessibilityEvent(event);
        }        

        public void handleHide() {
=======
            }
        }

        public void handleHide()
        {
>>>>>>> 54b6cfa... Initial Contribution
            if (localLOGV) Log.v(TAG, "HANDLE HIDE: " + this + " mView=" + mView);
            if (mView != null) {
                // note: checking parent() just to make sure the view has
                // been added...  i have seen cases where we get here when
                // the view isn't yet added, so let's try not to crash.
                if (mView.getParent() != null) {
<<<<<<< HEAD
                    if (localLOGV) Log.v(TAG, "REMOVE! " + mView + " in " + this);
                    mWM.removeView(mView);
                }

                mView = null;
            }
        }
    }
}
=======
                    if (localLOGV) Log.v(
                            TAG, "REMOVE! " + mView + " in " + this);
                    mWM.removeView(mView);
                }
                mView = null;
            }
        }

        Runnable mShow = new Runnable() {
            public void run() {
                handleShow();
            }
        };

        Runnable mHide = new Runnable() {
            public void run() {
                handleHide();
            }
        };

        private final WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
        
        WindowManagerImpl mWM;
    }

    final Handler mHandler = new Handler();
}

>>>>>>> 54b6cfa... Initial Contribution
