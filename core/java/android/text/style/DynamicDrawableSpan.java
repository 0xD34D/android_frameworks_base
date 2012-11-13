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

package android.text.style;

<<<<<<< HEAD
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.lang.ref.WeakReference;
=======
import java.lang.ref.WeakReference;

import android.graphics.drawable.Drawable;
import android.graphics.Canvas;
import android.graphics.Paint;
>>>>>>> 54b6cfa... Initial Contribution

/**
 *
 */
<<<<<<< HEAD
public abstract class DynamicDrawableSpan extends ReplacementSpan {
    private static final String TAG = "DynamicDrawableSpan";
    
    /**
     * A constant indicating that the bottom of this span should be aligned
     * with the bottom of the surrounding text, i.e., at the same level as the
     * lowest descender in the text.
     */
    public static final int ALIGN_BOTTOM = 0;
    
    /**
     * A constant indicating that the bottom of this span should be aligned
     * with the baseline of the surrounding text.
     */
    public static final int ALIGN_BASELINE = 1;
    
    protected final int mVerticalAlignment;
    
    public DynamicDrawableSpan() {
        mVerticalAlignment = ALIGN_BOTTOM;
    }

    /**
     * @param verticalAlignment one of {@link #ALIGN_BOTTOM} or {@link #ALIGN_BASELINE}.
     */
    protected DynamicDrawableSpan(int verticalAlignment) {
        mVerticalAlignment = verticalAlignment;
    }

    /**
     * Returns the vertical alignment of this span, one of {@link #ALIGN_BOTTOM} or
     * {@link #ALIGN_BASELINE}.
     */
    public int getVerticalAlignment() {
        return mVerticalAlignment;
    }

=======
public abstract class DynamicDrawableSpan
extends ReplacementSpan
{
>>>>>>> 54b6cfa... Initial Contribution
    /**
     * Your subclass must implement this method to provide the bitmap   
     * to be drawn.  The dimensions of the bitmap must be the same
     * from each call to the next.
     */
    public abstract Drawable getDrawable();

<<<<<<< HEAD
    @Override
    public int getSize(Paint paint, CharSequence text,
                         int start, int end,
                         Paint.FontMetricsInt fm) {
        Drawable d = getCachedDrawable();
        Rect rect = d.getBounds();

        if (fm != null) {
            fm.ascent = -rect.bottom; 
            fm.descent = 0; 
=======
    public int getSize(Paint paint, CharSequence text,
                         int start, int end,
                         Paint.FontMetricsInt fm) {
        Drawable b = getCachedDrawable();

        if (fm != null) {
            fm.ascent = -b.getIntrinsicHeight();
            fm.descent = 0;
>>>>>>> 54b6cfa... Initial Contribution

            fm.top = fm.ascent;
            fm.bottom = 0;
        }

<<<<<<< HEAD
        return rect.right;
    }

    @Override
=======
        return b.getIntrinsicWidth();
    }

>>>>>>> 54b6cfa... Initial Contribution
    public void draw(Canvas canvas, CharSequence text,
                     int start, int end, float x, 
                     int top, int y, int bottom, Paint paint) {
        Drawable b = getCachedDrawable();
        canvas.save();
        
<<<<<<< HEAD
        int transY = bottom - b.getBounds().bottom;
        if (mVerticalAlignment == ALIGN_BASELINE) {
            transY -= paint.getFontMetricsInt().descent;
        }

        canvas.translate(x, transY);
=======
        canvas.translate(x, bottom-b.getIntrinsicHeight());;
>>>>>>> 54b6cfa... Initial Contribution
        b.draw(canvas);
        canvas.restore();
    }

    private Drawable getCachedDrawable() {
<<<<<<< HEAD
        WeakReference<Drawable> wr = mDrawableRef;
        Drawable d = null;

        if (wr != null)
            d = wr.get();

        if (d == null) {
            d = getDrawable();
            mDrawableRef = new WeakReference<Drawable>(d);
        }

        return d;
    }

    private WeakReference<Drawable> mDrawableRef;
=======
        WeakReference wr = mDrawableRef;
        Drawable b = null;

        if (wr != null)
            b = (Drawable) wr.get();

        if (b == null) {
            b = getDrawable();
            mDrawableRef = new WeakReference(b);
        }

        return b;
    }

    private WeakReference mDrawableRef;
>>>>>>> 54b6cfa... Initial Contribution
}

