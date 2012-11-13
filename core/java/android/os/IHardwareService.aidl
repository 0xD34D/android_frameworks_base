/**
 * Copyright (c) 2007, The Android Open Source Project
 *
<<<<<<< HEAD
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
=======
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *     http://www.apache.org/licenses/LICENSE-2.0 
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
>>>>>>> 54b6cfa... Initial Contribution
 * limitations under the License.
 */

package android.os;

/** {@hide} */
interface IHardwareService
{
<<<<<<< HEAD
    // obsolete flashlight support
    boolean getFlashlightEnabled();
    void setFlashlightEnabled(boolean on);
=======
    // Vibrator support
    void vibrate(long milliseconds);
    void vibratePattern(in long[] pattern, int repeat, IBinder token);
    void cancelVibrate();
    
    // flashlight support
    boolean getFlashlightEnabled();
    void setFlashlightEnabled(boolean on);
    void enableCameraFlash(int milliseconds);
    
    // backlight support
    void setScreenBacklight(int brightness);
    void setKeyboardBacklight(boolean on);
    void setButtonBacklight(boolean on);
    
    // LED support
    void setLedState(int colorARGB, int onMS, int offMS);
>>>>>>> 54b6cfa... Initial Contribution
}

