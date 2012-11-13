/*
 * Copyright (C) 2007 The Android Open Source Project
<<<<<<< HEAD
 * Copyright (c) 2010-2011, Code Aurora Forum. All rights reserved.
=======
>>>>>>> 54b6cfa... Initial Contribution
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

package android.os;

<<<<<<< HEAD
import java.io.IOException;
import android.os.ServiceManager;

=======
>>>>>>> 54b6cfa... Initial Contribution
/**
 * Class that provides access to some of the power management functions.
 *
 * {@hide}
 */
public class Power
{
    // can't instantiate this class
    private Power()
    {
    }

    /**
     * Wake lock that ensures that the CPU is running.  The screen might
     * not be on.
     */
    public static final int PARTIAL_WAKE_LOCK = 1;

    /**
     * Wake lock that ensures that the screen is on.
     */
    public static final int FULL_WAKE_LOCK = 2;

    public static native void acquireWakeLock(int lock, String id);
    public static native void releaseWakeLock(String id);

    /**
<<<<<<< HEAD
=======
     * Flag to turn on and off the keyboard light.
     */
    public static final int KEYBOARD_LIGHT = 0x00000001;

    /**
     * Flag to turn on and off the screen backlight.
     */
    public static final int SCREEN_LIGHT = 0x00000002;

    /**
     * Flag to turn on and off the button backlight.
     */
    public static final int BUTTON_LIGHT = 0x00000004;

    /**
     * Flags to turn on and off all the backlights.
     */
    public static final int ALL_LIGHTS = (KEYBOARD_LIGHT|SCREEN_LIGHT|BUTTON_LIGHT);

    /**
>>>>>>> 54b6cfa... Initial Contribution
     * Brightness value for fully off
     */
    public static final int BRIGHTNESS_OFF = 0;

    /**
     * Brightness value for dim backlight
     */
    public static final int BRIGHTNESS_DIM = 20;
<<<<<<< HEAD

=======
    
>>>>>>> 54b6cfa... Initial Contribution
    /**
     * Brightness value for fully on
     */
    public static final int BRIGHTNESS_ON = 255;
<<<<<<< HEAD

=======
    
>>>>>>> 54b6cfa... Initial Contribution
    /**
     * Brightness value to use when battery is low
     */
    public static final int BRIGHTNESS_LOW_BATTERY = 10;

    /**
     * Threshold for BRIGHTNESS_LOW_BATTERY (percentage)
     * Screen will stay dim if battery level is <= LOW_BATTERY_THRESHOLD
     */
    public static final int LOW_BATTERY_THRESHOLD = 10;

    /**
<<<<<<< HEAD
     * Low-level function turn the device off immediately, without trying
     * to be clean.  Most people should use
     * {@link android.internal.app.ShutdownThread} for a clean shutdown.
     *
     * @deprecated
     * @hide
     */
    @Deprecated
    public static native void shutdown();

    /**
     * Reboot the device.
     * @param reason code to pass to the kernel (e.g. "recovery"), or null.
     *
     * @throws IOException if reboot fails for some reason (eg, lack of
     *         permission)
     */
    public static void reboot(String reason) throws IOException
    {
        rebootNative(reason);
    }

    private static native void rebootNative(String reason) throws IOException ;

    /**
     * Activate/DeActivate Unstable Memory block
     *
     * @param on Whether you want Activate(true) or DeActive(False)
     * @hide
     */
    public static native int SetUnstableMemoryState(boolean on);
}
=======
     * Set the brightness for one or more lights
     *
     * @param mask flags indicating which lights to change brightness
     * @param brightness new brightness value (0 = off, 255 = fully bright)
     */
    public static native int setLightBrightness(int mask, int brightness);

    /**
     * Turn the screen on or off
     *
     * @param on Whether you want the screen on or off
     */
    public static native int setScreenState(boolean on);

    public static native int setLastUserActivityTimeout(long ms);
    
    /**
     * Turn the device off.
     * 
     * This method is considered deprecated in favor of 
     * {@link android.policy.ShutdownThread.shutdownAfterDisablingRadio()}.
     *
     * @deprecated
     * @hide
     */
    @Deprecated
    public static native void shutdown();

    /**
     * Reboot the device.
     * @param reason code to pass to the kernel (e.g. "recovery"), or null.
     */
    public static native void reboot(String reason);
}

>>>>>>> 54b6cfa... Initial Contribution
