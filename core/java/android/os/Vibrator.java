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

package android.os;

<<<<<<< HEAD
import android.content.Context;

=======
>>>>>>> 54b6cfa... Initial Contribution
/**
 * Class that operates the vibrator on the device.
 * <p>
 * If your process exits, any vibration you started with will stop.
<<<<<<< HEAD
 * </p>
 *
 * To obtain an instance of the system vibrator, call
 * {@link Context#getSystemService} with {@link Context#VIBRATOR_SERVICE} as argument.
 */
public abstract class Vibrator {
    /**
     * @hide to prevent subclassing from outside of the framework
     */
    public Vibrator() {
    }

    /**
     * Check whether the hardware has a vibrator.
     *
     * @return True if the hardware has a vibrator, else false.
     */
    public abstract boolean hasVibrator();
    
    /**
     * Vibrate constantly for the specified period of time.
     * <p>This method requires the caller to hold the permission
     * {@link android.Manifest.permission#VIBRATE}.
     *
     * @param milliseconds The number of milliseconds to vibrate.
     */
    public abstract void vibrate(long milliseconds);
=======
 */
public class Vibrator
{
    IHardwareService mService;

    /** @hide */
    public Vibrator()
    {
        mService = IHardwareService.Stub.asInterface(
                ServiceManager.getService("hardware"));
    }

    /**
     * Turn the vibrator on.
     *
     * @param milliseconds How long to vibrate for.
     */
    public void vibrate(long milliseconds)
    {
        try {
            mService.vibrate(milliseconds);
        } catch (RemoteException e) {
        }
    }
>>>>>>> 54b6cfa... Initial Contribution

    /**
     * Vibrate with a given pattern.
     *
     * <p>
<<<<<<< HEAD
     * Pass in an array of ints that are the durations for which to turn on or off
     * the vibrator in milliseconds.  The first value indicates the number of milliseconds
     * to wait before turning the vibrator on.  The next value indicates the number of milliseconds
     * for which to keep the vibrator on before turning it off.  Subsequent values alternate
     * between durations in milliseconds to turn the vibrator off or to turn the vibrator on.
     * </p><p>
     * To cause the pattern to repeat, pass the index into the pattern array at which
     * to start the repeat, or -1 to disable repeating.
     * </p>
     * <p>This method requires the caller to hold the permission
     * {@link android.Manifest.permission#VIBRATE}.
     *
     * @param pattern an array of longs of times for which to turn the vibrator on or off.
     * @param repeat the index into pattern at which to repeat, or -1 if
     *        you don't want to repeat.
     */
    public abstract void vibrate(long[] pattern, int repeat);

    /**
     * Turn the vibrator off.
     * <p>This method requires the caller to hold the permission
     * {@link android.Manifest.permission#VIBRATE}.
     */
    public abstract void cancel();
=======
     * Pass in an array of ints that are the times at which to turn on or off
     * the vibrator.  The first one is how long to wait before turning it on,
     * and then after that it alternates.  If you want to repeat, pass the
     * index into the pattern at which to start the repeat.
     *
     * @param pattern an array of longs of times to turn the vibrator on or off.
     * @param repeat the index into pattern at which to repeat, or -1 if
     *        you don't want to repeat.
     */
    public void vibrate(long[] pattern, int repeat)
    {
        // catch this here because the server will do nothing.  pattern may
        // not be null, let that be checked, because the server will drop it
        // anyway
        if (repeat < pattern.length) {
            try {
                mService.vibratePattern(pattern, repeat, new Binder());
            } catch (RemoteException e) {
            }
        } else {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    /**
     * Turn the vibrator off.
     */
    public void cancel()
    {
        try {
            mService.cancelVibrate();
        } catch (RemoteException e) {
        }
    }
>>>>>>> 54b6cfa... Initial Contribution
}
