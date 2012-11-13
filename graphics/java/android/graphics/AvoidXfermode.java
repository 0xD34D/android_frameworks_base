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

package android.graphics;

/**
 * AvoidXfermode xfermode will draw the src everywhere except on top of the
 * opColor or, depending on the Mode, draw only on top of the opColor.
 */
<<<<<<< HEAD
@Deprecated
=======
>>>>>>> 54b6cfa... Initial Contribution
public class AvoidXfermode extends Xfermode {

    // these need to match the enum in SkAvoidXfermode.h on the native side
    public enum Mode {
        AVOID   (0),    //!< draw everywhere except on the opColor
        TARGET  (1);    //!< draw only on top of the opColor
        
        Mode(int nativeInt) {
            this.nativeInt = nativeInt;
        }
        final int nativeInt;
    }
    
<<<<<<< HEAD
    /** This xfermode draws, or doesn't draw, based on the destination's
     * distance from an op-color.
     *
     * There are two modes, and each mode interprets a tolerance value.
     *
     * Avoid: In this mode, drawing is allowed only on destination pixels that
     * are different from the op-color.
     * Tolerance near 0: avoid any colors even remotely similar to the op-color
     * Tolerance near 255: avoid only colors nearly identical to the op-color
     
     * Target: In this mode, drawing only occurs on destination pixels that
     * are similar to the op-color
     * Tolerance near 0: draw only on colors that are nearly identical to the op-color
     * Tolerance near 255: draw on any colors even remotely similar to the op-color
=======
    /**
     * This xfermode will draw the src everywhere except on top of the opColor
     * or, depending on the Mode, draw only on top of the opColor.
     *
     * @param opColor The color to avoid (or to target depending on Mode). Note
     *                that the alpha in opColor is ignored.
     * @param tolerance How closely we compare a pixel to the opColor.
     *                  0 - only operate if exact match
     *                  255 - maximum gradation (blending) based on how
     *                  similar the pixel is to our opColor (max tolerance)
     * @param mode If we should avoid or target the opColor
>>>>>>> 54b6cfa... Initial Contribution
     */
    public AvoidXfermode(int opColor, int tolerance, Mode mode) {
        if (tolerance < 0 || tolerance > 255) {
            throw new IllegalArgumentException("tolerance must be 0..255");
        }
        native_instance = nativeCreate(opColor, tolerance, mode.nativeInt);
    }

    private static native int nativeCreate(int opColor, int tolerance,
                                           int nativeMode);
}
