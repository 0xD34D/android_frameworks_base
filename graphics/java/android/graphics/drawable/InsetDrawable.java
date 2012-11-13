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

package android.graphics.drawable;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.Log;

import java.io.IOException;

/**
 * A Drawable that insets another Drawable by a specified distance.
 * This is used when a View needs a background that is smaller than
 * the View's actual bounds.
<<<<<<< HEAD
 *
 * <p>It can be defined in an XML file with the <code>&lt;inset></code> element. For more
 * information, see the guide to <a
 * href="{@docRoot}guide/topics/resources/drawable-resource.html">Drawable Resources</a>.</p>
 *
 * @attr ref android.R.styleable#InsetDrawable_visible
 * @attr ref android.R.styleable#InsetDrawable_drawable
 * @attr ref android.R.styleable#InsetDrawable_insetLeft
 * @attr ref android.R.styleable#InsetDrawable_insetRight
 * @attr ref android.R.styleable#InsetDrawable_insetTop
 * @attr ref android.R.styleable#InsetDrawable_insetBottom
=======
>>>>>>> 54b6cfa... Initial Contribution
 */
public class InsetDrawable extends Drawable implements Drawable.Callback
{
    // Most of this is copied from ScaleDrawable.
<<<<<<< HEAD
    private InsetState mInsetState;
    private final Rect mTmpRect = new Rect();
    private boolean mMutated;

    /*package*/ InsetDrawable() {
        this(null, null);
=======

    /*package*/ InsetDrawable() {
        this(null);
>>>>>>> 54b6cfa... Initial Contribution
    }

    public InsetDrawable(Drawable drawable, int inset) {
        this(drawable, inset, inset, inset, inset);
    }

    public InsetDrawable(Drawable drawable, int insetLeft, int insetTop,
                         int insetRight, int insetBottom) {
<<<<<<< HEAD
        this(null, null);
=======
        this(null);
>>>>>>> 54b6cfa... Initial Contribution
        
        mInsetState.mDrawable = drawable;
        mInsetState.mInsetLeft = insetLeft;
        mInsetState.mInsetTop = insetTop;
        mInsetState.mInsetRight = insetRight;
        mInsetState.mInsetBottom = insetBottom;
        
        if (drawable != null) {
            drawable.setCallback(this);
        }
    }
    
    @Override public void inflate(Resources r, XmlPullParser parser,
                                  AttributeSet attrs)
    throws XmlPullParserException, IOException {
        int type;
        
        TypedArray a = r.obtainAttributes(attrs,
                com.android.internal.R.styleable.InsetDrawable);

        super.inflateWithAttributes(r, parser, a,
                com.android.internal.R.styleable.InsetDrawable_visible);

        int drawableRes = a.getResourceId(com.android.internal.R.styleable.
                                    InsetDrawable_drawable, 0);

        int inLeft = a.getDimensionPixelOffset(com.android.internal.R.styleable.
                                    InsetDrawable_insetLeft, 0);
        int inTop = a.getDimensionPixelOffset(com.android.internal.R.styleable.
                                    InsetDrawable_insetTop, 0);
        int inRight = a.getDimensionPixelOffset(com.android.internal.R.styleable.
                                    InsetDrawable_insetRight, 0);
        int inBottom = a.getDimensionPixelOffset(com.android.internal.R.styleable.
                                    InsetDrawable_insetBottom, 0);

        a.recycle();

        Drawable dr;
        if (drawableRes != 0) {
            dr = r.getDrawable(drawableRes);
        } else {
            while ((type=parser.next()) == XmlPullParser.TEXT) {
            }
            if (type != XmlPullParser.START_TAG) {
                throw new XmlPullParserException(
                        parser.getPositionDescription()
                        + ": <inset> tag requires a 'drawable' attribute or "
                        + "child tag defining a drawable");
            }
            dr = Drawable.createFromXmlInner(r, parser, attrs);
        }

        if (dr == null) {
            Log.w("drawable", "No drawable specified for <inset>");
        }

        mInsetState.mDrawable = dr;
        mInsetState.mInsetLeft = inLeft;
        mInsetState.mInsetRight = inRight;
        mInsetState.mInsetTop = inTop;
        mInsetState.mInsetBottom = inBottom;

        if (dr != null) {
            dr.setCallback(this);
        }
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
    public void draw(Canvas canvas) {
        mInsetState.mDrawable.draw(canvas);
    }

    @Override
    public int getChangingConfigurations() {
        return super.getChangingConfigurations()
                | mInsetState.mChangingConfigurations
                | mInsetState.mDrawable.getChangingConfigurations();
    }
    
    @Override
    public boolean getPadding(Rect padding) {
        boolean pad = mInsetState.mDrawable.getPadding(padding);

        padding.left += mInsetState.mInsetLeft;
        padding.right += mInsetState.mInsetRight;
        padding.top += mInsetState.mInsetTop;
        padding.bottom += mInsetState.mInsetBottom;

        if (pad || (mInsetState.mInsetLeft | mInsetState.mInsetRight | 
                    mInsetState.mInsetTop | mInsetState.mInsetBottom) != 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean setVisible(boolean visible, boolean restart) {
        mInsetState.mDrawable.setVisible(visible, restart);
        return super.setVisible(visible, restart);
    }

    @Override
    public void setAlpha(int alpha) {
        mInsetState.mDrawable.setAlpha(alpha);
    }
    
    @Override
    public void setColorFilter(ColorFilter cf) {
        mInsetState.mDrawable.setColorFilter(cf);
    }
    
    @Override
    public int getOpacity() {
        return mInsetState.mDrawable.getOpacity();
    }
    
    @Override
    public boolean isStateful() {
        return mInsetState.mDrawable.isStateful();
    }

    @Override
    protected boolean onStateChange(int[] state) {
        boolean changed = mInsetState.mDrawable.setState(state);
        onBoundsChange(getBounds());
        return changed;
    }
    
    @Override
    protected void onBoundsChange(Rect bounds) {
        final Rect r = mTmpRect;
        r.set(bounds);

        r.left += mInsetState.mInsetLeft;
        r.top += mInsetState.mInsetTop;
        r.right -= mInsetState.mInsetRight;
        r.bottom -= mInsetState.mInsetBottom;

        mInsetState.mDrawable.setBounds(r.left, r.top, r.right, r.bottom);
    }

    @Override
    public int getIntrinsicWidth() {
        return mInsetState.mDrawable.getIntrinsicWidth();
    }

    @Override
    public int getIntrinsicHeight() {
        return mInsetState.mDrawable.getIntrinsicHeight();
    }

    @Override
    public ConstantState getConstantState() {
        if (mInsetState.canConstantState()) {
<<<<<<< HEAD
            mInsetState.mChangingConfigurations = getChangingConfigurations();
=======
            mInsetState.mChangingConfigurations = super.getChangingConfigurations();
>>>>>>> 54b6cfa... Initial Contribution
            return mInsetState;
        }
        return null;
    }

<<<<<<< HEAD
    @Override
    public Drawable mutate() {
        if (!mMutated && super.mutate() == this) {
            mInsetState.mDrawable.mutate();
            mMutated = true;
        }
        return this;
    }

    final static class InsetState extends ConstantState {
        Drawable mDrawable;
        int mChangingConfigurations;

        int mInsetLeft;
        int mInsetTop;
        int mInsetRight;
        int mInsetBottom;

        boolean mCheckedConstantState;
        boolean mCanConstantState;

        InsetState(InsetState orig, InsetDrawable owner, Resources res) {
            if (orig != null) {
                if (res != null) {
                    mDrawable = orig.mDrawable.getConstantState().newDrawable(res);
                } else {
                    mDrawable = orig.mDrawable.getConstantState().newDrawable();
                }
=======
    final static class InsetState extends ConstantState {
        InsetState(InsetState orig, InsetDrawable owner) {
            if (orig != null) {
                mDrawable = orig.mDrawable.getConstantState().newDrawable();
>>>>>>> 54b6cfa... Initial Contribution
                mDrawable.setCallback(owner);
                mInsetLeft = orig.mInsetLeft;
                mInsetTop = orig.mInsetTop;
                mInsetRight = orig.mInsetRight;
                mInsetBottom = orig.mInsetBottom;
                mCheckedConstantState = mCanConstantState = true;
            }
        }

        @Override
        public Drawable newDrawable() {
<<<<<<< HEAD
            return new InsetDrawable(this, null);
        }
        
        @Override
        public Drawable newDrawable(Resources res) {
            return new InsetDrawable(this, res);
=======
            return new InsetDrawable(this);
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

    private InsetDrawable(InsetState state, Resources res) {
        mInsetState = new InsetState(state, this, res);
    }
=======

        Drawable mDrawable;
        int mChangingConfigurations;

        int mInsetLeft;
        int mInsetTop;
        int mInsetRight;
        int mInsetBottom;

        boolean mCheckedConstantState;
        boolean mCanConstantState;
    }

    private InsetDrawable(InsetState state) {
        InsetState as = new InsetState(state, this);
        mInsetState = as;
    }

    private InsetState  mInsetState;
    private final Rect  mTmpRect = new Rect();
>>>>>>> 54b6cfa... Initial Contribution
}

