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

package android.view;

import android.content.Context;
import android.hardware.SensorListener;
<<<<<<< HEAD
=======
import android.hardware.SensorManager;
import android.util.Config;
import android.util.Log;
>>>>>>> 54b6cfa... Initial Contribution

/**
 * Helper class for receiving notifications from the SensorManager when
 * the orientation of the device has changed.
<<<<<<< HEAD
 *  @deprecated use {@link android.view.OrientationEventListener} instead.
 *  This class internally uses the OrientationEventListener.
 */
@Deprecated
public abstract class OrientationListener implements SensorListener {
    private OrientationEventListener mOrientationEventLis;
=======
 */
public abstract class OrientationListener implements SensorListener {

    private static final String TAG = "OrientationListener";
    private static final boolean DEBUG = false;
    private static final boolean localLOGV = DEBUG ? Config.LOGD : Config.LOGV;
    private SensorManager mSensorManager;
    private int mOrientation = ORIENTATION_UNKNOWN;
    private boolean mEnabled = false;
>>>>>>> 54b6cfa... Initial Contribution
    
    /**
     * Returned from onOrientationChanged when the device orientation cannot be determined
     * (typically when the device is in a close to flat position).
     *
     *  @see #onOrientationChanged
     */
<<<<<<< HEAD
    public static final int ORIENTATION_UNKNOWN = OrientationEventListener.ORIENTATION_UNKNOWN;
=======
    public static final int ORIENTATION_UNKNOWN = -1;
>>>>>>> 54b6cfa... Initial Contribution

    /**
     * Creates a new OrientationListener.
     * 
     * @param context for the OrientationListener.
     */
    public OrientationListener(Context context) {
<<<<<<< HEAD
        mOrientationEventLis = new OrientationEventListenerInternal(context);
    }

    /**
     * Creates a new OrientationListener.
     * 
     * @param context for the OrientationListener.
     * @param rate at which sensor events are processed (see also
     * {@link android.hardware.SensorManager SensorManager}). Use the default
     * value of {@link android.hardware.SensorManager#SENSOR_DELAY_NORMAL 
     * SENSOR_DELAY_NORMAL} for simple screen orientation change detection.
     */
    public OrientationListener(Context context, int rate) {
        mOrientationEventLis = new OrientationEventListenerInternal(context, rate);
    }
    
    class OrientationEventListenerInternal extends OrientationEventListener {
        OrientationEventListenerInternal(Context context) {
            super(context);
        }
        
        OrientationEventListenerInternal(Context context, int rate) {
            super(context, rate);
            // register so that onSensorChanged gets invoked
            registerListener(OrientationListener.this);
        }
                
        public void onOrientationChanged(int orientation) {
            OrientationListener.this.onOrientationChanged(orientation);
        }
    }
    
    /**
=======
        mSensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
    }

    /**
>>>>>>> 54b6cfa... Initial Contribution
     * Enables the OrientationListener so it will monitor the sensor and call
     * {@link #onOrientationChanged} when the device orientation changes.
     */
    public void enable() {
<<<<<<< HEAD
        mOrientationEventLis.enable();
=======
        if (mEnabled == false) {
            if (localLOGV) Log.d(TAG, "OrientationListener enabled");
            mSensorManager.registerListener(this, SensorManager.SENSOR_ACCELEROMETER);
            mEnabled = true;
        }
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Disables the OrientationListener.
     */
    public void disable() {
<<<<<<< HEAD
        mOrientationEventLis.disable();
    }
    
    public void onAccuracyChanged(int sensor, int accuracy) {
    }
    
    public void onSensorChanged(int sensor, float[] values) {
        // just ignore the call here onOrientationChanged is invoked anyway
    }


    /**
     * Look at {@link android.view.OrientationEventListener#onOrientationChanged}
     * for method description and usage
=======
        if (mEnabled == true) {
            if (localLOGV) Log.d(TAG, "OrientationListener disabled");
            mSensorManager.unregisterListener(this);
            mEnabled = false;
        }
    }

    /**
     * 
     */
    public void onSensorChanged(int sensor, float[] values) {
        int orientation = ORIENTATION_UNKNOWN;
        float X = values[SensorManager.RAW_DATA_X];
        float Y = values[SensorManager.RAW_DATA_Y];
        float Z = values[SensorManager.RAW_DATA_Z];        
        float magnitude = X*X + Y*Y;
        // Don't trust the angle if the magnitude is small compared to the y value
        if (magnitude * 4 >= Z*Z) {
            float OneEightyOverPi = 57.29577957855f;
            float angle = (float)Math.atan2(-Y, X) * OneEightyOverPi;
            orientation = 90 - (int)Math.round(angle);
            // normalize to 0 - 359 range
            while (orientation >= 360) {
                orientation -= 360;
            } 
            while (orientation < 0) {
                orientation += 360;
            }
        }

        if (orientation != mOrientation) {
            mOrientation = orientation;
            onOrientationChanged(orientation);
        }
    }

    public void onAccuracyChanged(int sensor, int accuracy) {
        // TODO Auto-generated method stub
        
    }

    /**
     * Called when the orientation of the device has changed.
     * orientation parameter is in degrees, ranging from 0 to 359.
     * orientation is 0 degrees when the device is oriented in its natural position,
     * 90 degrees when its left side is at the top, 180 degrees when it is upside down, 
     * and 270 degrees when its right side is to the top.
     * {@link #ORIENTATION_UNKNOWN} is returned when the device is close to flat
     * and the orientation cannot be determined.
     *
>>>>>>> 54b6cfa... Initial Contribution
     * @param orientation The new orientation of the device.
     *
     *  @see #ORIENTATION_UNKNOWN
     */
    abstract public void onOrientationChanged(int orientation);
<<<<<<< HEAD
    
=======
>>>>>>> 54b6cfa... Initial Contribution
}
