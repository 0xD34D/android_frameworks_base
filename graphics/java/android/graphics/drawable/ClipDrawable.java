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

package android.graphics.drawable;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.*;
import android.view.Gravity;
import android.util.AttributeSet;
<<<<<<< HEAD
=======
import android.util.Log;
>>>>>>> 54b6cfa... Initial Contribution

import java.io.IOException;

/**
<<<<<<< HEAD
 * A Drawable that clips another Drawable based on this Drawable's current
 * level value.  You can control how much the child Drawable gets clipped in width
 * and height based on the level, as well as a gravity to control where it is
 * placed in its overall container.  Most often used to implement things like
 * progress bars, by increasing the drawable's level with {@link
 * android.graphics.drawable.Drawable#setLevel(int) setLevel()}.
 * <p class="note"><strong>Note:</strong> The drawable is clipped completely and not visible when
 * the level is 0 and fully revealed when the level is 10,000.</p>
 *
 * <p>It can be defined in an XML file with the <code>&lt;clip></code> element.  For more
 * information, see the guide to <a
 * href="{@docRoot}guide/topics/resources/drawable-resource.html">Drawable Resources</a>.</p>
 *
 * @attr ref android.R.styleable#ClipDrawable_clipOrientation
 * @attr ref android.R.styleable#ClipDrawable_gravity
 * @attr ref android.R.styleable#ClipDrawable_drawable
=======
 * A drawable that clips another drawable based on this drawable's current
 * level value.  You can control how much the child drawable gets clipped in width
 * and height based on the level, as well as a gravity to control where it is
 * placed in its overall container.  Most often used to implement things like
 * progress bars.
>>>>>>> 54b6cfa... Initial Contribution
 */
public class ClipDrawable extends Drawable implements Drawable.Callback {
    private ClipState mClipState;
    private final Rect mTmpRect = new Rect();

    public static final int HORIZONTAL = 1;
    public static final int VERTICAL = 2;
    
    ClipDrawable() {
<<<<<<< HEAD
        this(null, null);
=======
        this(null);
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * @param orientation Bitwise-or of {@link #HORIZONTAL} and/or {@link #VERTICAL}
     */
    public ClipDrawable(Drawable drawable, int gravity, int orientation) {
<<<<<<< HEAD
        this(null, null);
=======
        this(null);
>>>>>>> 54b6cfa... Initial Contribution

        mClipState.mDrawable = drawable;
        mClipState.mGravity = gravity;
        mClipState.mOrientation = orientation;

        if (drawable != null) {
            drawable.setCallback(this);
        }
    }

    @Override
    public void inflate(Resources r, XmlPullParser parser, AttributeSet attrs)
            throws XmlPullParserException, IOException {
        super.inflate(r, parser, attrs);

        int type;

        TypedArray a = r.obtainAttributes(attrs, com.android.internal.R.styleable.ClipDrawable);

        int orientation = a.getInt(
                com.android.internal.R.styleable.ClipDrawable_clipOrientation,
                HORIZONTAL);
        int g = a.getInt(com.android.internal.R.styleable.ClipDrawable_gravity, Gravity.LEFT);
        Drawable dr = a.getDrawable(com.android.internal.R.styleable.ClipDrawable_drawable);

        a.recycle();

        final int outerDepth = parser.getDepth();
        while ((type = parser.next()) != XmlPullParser.END_DOCUMENT
                && (type != XmlPullParser.END_TAG || parser.getDepth() > outerDepth)) {
            if (type != XmlPullParser.START_TAG) {
                continue;
            }
            dr = Drawable.createFromXmlInner(r, parser, attrs);
        }

        if (dr == null) {
            throw new IllegalArgumentException("No drawable specified for <clip>");
        }

        mClipState.mDrawable = dr;
        mClipState.mOrientation = orientation;
        mClipState.mGravity = g;
<<<<<<< HEAD

        dr.setCallback(this);
=======
        if (dr != null) {
            dr.setCallback(this);
        }
>>>>>>> 54b6cfa... Initial Contribution
    }

    // overrides from Drawable.Callback

    public void invalidateDrawable(Drawable who) {
<<<<<<< HEAD
        final Callback callback = getCallback();
        if (callback != null) {
            callback.invalidateDrawable(this);
=======
        if (mCallback != null) {
            mCallback.invalidateDrawable(this);
>>>>>>> 54b6cfa... Initial Contribution
        }
    }

    public void scheduleDrawable(Drawable who, Runnable what, long when) {
<<<<<<< HEAD
        final Callback callback = getCallback();
        if (callback != null) {
            callback.scheduleDrawable(this, what, when);
=======
        if (mCallback != null) {
            mCallback.scheduleDrawable(this, what, when);
>>>>>>> 54b6cfa... Initial Contribution
        }
    }

    public void unscheduleDrawable(Drawable who, Runnable what) {
<<<<<<< HEAD
        final Callback callback = getCallback();
        if (callback != null) {
            callback.unscheduleDrawable(this, what);
=======
        if (mCallback != null) {
            mCallback.unscheduleDrawable(this, what);
>>>>>>> 54b6cfa... Initial Contribution
        }
    }

    // overrides from Drawable

    @Override
    public int getChangingConfigurations() {
        return super.getChangingConfigurations()
                | mClipState.mChangingConfigurations
                | mClipState.mDrawable.getChangingConfigurations();
    }

    @Override
    public boolean getPadding(Rect padding) {
        // XXX need to adjust padding!
        return mClipState.mDrawable.getPadding(padding);
    }

    @Override
    public boolean setVisible(boolean visible, boolean restart) {
        mClipState.mDrawable.setVisible(visible, restart);
        return super.setVisible(visible, restart);
    }

    @Override
    public void setAlpha(int alpha) {
        mClipState.mDrawable.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mClipState.mDrawable.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return mClipState.mDrawable.getOpacity();
    }

    @Override
    public boolean isStateful() {
        return mClipState.mDrawable.isStateful();
    }

    @Override
    protected boolean onStateChange(int[] state) {
<<<<<<< HEAD
        return mClipState.mDrawable.setState(state);
=======
        boolean changed = mClipState.mDrawable.setState(state);
        return changed;
>>>>>>> 54b6cfa... Initial Contribution
    }

    @Override
    protected boolean onLevelChange(int level) {
        mClipState.mDrawable.setLevel(level);
        invalidateSelf();
        return true;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        mClipState.mDrawable.setBounds(bounds);
    }

    @Override
    public void draw(Canvas canvas) {
        
        if (mClipState.mDrawable.getLevel() == 0) {
            return;
        }

        final Rect r = mTmpRect;
        final Rect bounds = getBounds();
        int level = getLevel();
        int w = bounds.width();
        final int iw = 0; //mClipState.mDrawable.getIntrinsicWidth();
        if ((mClipState.mOrientation & HORIZONTAL) != 0) {
            w -= (w - iw) * (10000 - level) / 10000;
        }
        int h = bounds.height();
        final int ih = 0; //mClipState.mDrawable.getIntrinsicHeight();
        if ((mClipState.mOrientation & VERTICAL) != 0) {
            h -= (h - ih) * (10000 - level) / 10000;
        }
<<<<<<< HEAD
        final int layoutDirection = getResolvedLayoutDirectionSelf();
        Gravity.apply(mClipState.mGravity, w, h, bounds, r, layoutDirection);
=======
        Gravity.apply(mClipState.mGravity, w, h, bounds, r);
>>>>>>> 54b6cfa... Initial Contribution

        if (w > 0 && h > 0) {
            canvas.save();
            canvas.clipRect(r);
            mClipState.mDrawable.draw(canvas);
            canvas.restore();
        }
    }

    @Override
    public int getIntrinsicWidth() {
        return mClipState.mDrawable.getIntrinsicWidth();
    }

    @Override
    public int getIntrinsicHeight() {
        return mClipState.mDrawable.getIntrinsicHeight();
    }

    @Override
    public ConstantState getConstantState() {
        if (mClipState.canConstantState()) {
<<<<<<< HEAD
            mClipState.mChangingConfigurations = getChangingConfigurations();
=======
            mClipState.mChangingConfigurations = super.getChangingConfigurations();
>>>>>>> 54b6cfa... Initial Contribution
            return mClipState;
        }
        return null;
    }

<<<<<<< HEAD
    

    final static class ClipState extends ConstantState {
        Drawable mDrawable;
        int mChangingConfigurations;
        int mOrientation;
        int mGravity;

        private boolean mCheckedConstantState;
        private boolean mCanConstantState;

        ClipState(ClipState orig, ClipDrawable owner, Resources res) {
            if (orig != null) {
                if (res != null) {
                    mDrawable = orig.mDrawable.getConstantState().newDrawable(res);
                } else {
                    mDrawable = orig.mDrawable.getConstantState().newDrawable();
                }
=======
    final static class ClipState extends ConstantState {
        ClipState(ClipState orig, ClipDrawable owner) {
            if (orig != null) {
                mDrawable = orig.mDrawable.getConstantState().newDrawable();
>>>>>>> 54b6cfa... Initial Contribution
                mDrawable.setCallback(owner);
                mOrientation = orig.mOrientation;
                mGravity = orig.mGravity;
                mCheckedConstantState = mCanConstantState = true;
            }
        }

        @Override
        public Drawable newDrawable() {
<<<<<<< HEAD
            return new ClipDrawable(this, null);
        }

        @Override
        public Drawable newDrawable(Resources res) {
            return new ClipDrawable(this, res);
=======
            return new ClipDrawable(this);
>>>>>>> 54b6cfa... Initial Contribution
        }

        @Override
        public int getChangingConfigurations() {
            return mChangingConfigurations;
        }

        boolean canConstantState() {
            if (!mCheckedConstantState) {
                mCanConstantState = mDrawable.getConstantState() != null;
                mCheckedConstantState = true;
            }

            return mCanConstantState;
        }
<<<<<<< HEAD
    }

    private ClipDrawable(ClipState state, Resources res) {
        mClipState = new ClipState(state, this, res);
=======

        Drawable mDrawable;
        int mChangingConfigurations;
        int mOrientation;
        int mGravity;

        private boolean mCheckedConstantState;
        private boolean mCanConstantState;
    }

    private ClipDrawable(ClipState state) {
        mClipState = new ClipState(state, this);
>>>>>>> 54b6cfa... Initial Contribution
    }
}

