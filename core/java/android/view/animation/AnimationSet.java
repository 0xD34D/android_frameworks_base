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

package android.view.animation;

import android.content.Context;
import android.content.res.TypedArray;
<<<<<<< HEAD
import android.os.Build;
import android.util.AttributeSet;
import android.graphics.RectF;
=======
import android.util.AttributeSet;
>>>>>>> 54b6cfa... Initial Contribution

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a group of Animations that should be played together.
 * The transformation of each individual animation are composed 
 * together into a single transform. 
 * If AnimationSet sets any properties that its children also set
 * (for example, duration or fillBefore), the values of AnimationSet
 * override the child values.
<<<<<<< HEAD
 *
 * <p>The way that AnimationSet inherits behavior from Animation is important to
 * understand. Some of the Animation attributes applied to AnimationSet affect the
 * AnimationSet itself, some are pushed down to the children, and some are ignored,
 * as follows:
 * <ul>
 *     <li>duration, repeatMode, fillBefore, fillAfter: These properties, when set
 *     on an AnimationSet object, will be pushed down to all child animations.</li>
 *     <li>repeatCount, fillEnabled: These properties are ignored for AnimationSet.</li>
 *     <li>startOffset, shareInterpolator: These properties apply to the AnimationSet itself.</li>
 * </ul>
 * Starting with {@link android.os.Build.VERSION_CODES#ICE_CREAM_SANDWICH},
 * the behavior of these properties is the same in XML resources and at runtime (prior to that
 * release, the values set in XML were ignored for AnimationSet). That is, calling
 * <code>setDuration(500)</code> on an AnimationSet has the same effect as declaring
 * <code>android:duration="500"</code> in an XML resource for an AnimationSet object.</p>
=======
>>>>>>> 54b6cfa... Initial Contribution
 */
public class AnimationSet extends Animation {
    private static final int PROPERTY_FILL_AFTER_MASK         = 0x1;
    private static final int PROPERTY_FILL_BEFORE_MASK        = 0x2;
    private static final int PROPERTY_REPEAT_MODE_MASK        = 0x4;
    private static final int PROPERTY_START_OFFSET_MASK       = 0x8;
    private static final int PROPERTY_SHARE_INTERPOLATOR_MASK = 0x10;
    private static final int PROPERTY_DURATION_MASK           = 0x20;
    private static final int PROPERTY_MORPH_MATRIX_MASK       = 0x40;
<<<<<<< HEAD
    private static final int PROPERTY_CHANGE_BOUNDS_MASK      = 0x80;

    private int mFlags = 0;
    private boolean mDirty;
    private boolean mHasAlpha;
=======

    private int mFlags = 0;
>>>>>>> 54b6cfa... Initial Contribution

    private ArrayList<Animation> mAnimations = new ArrayList<Animation>();

    private Transformation mTempTransformation = new Transformation();

    private long mLastEnd;

    private long[] mStoredOffsets;

    /**
<<<<<<< HEAD
     * Constructor used when an AnimationSet is loaded from a resource. 
=======
     * Constructor used whan an AnimationSet is loaded from a resource. 
>>>>>>> 54b6cfa... Initial Contribution
     * 
     * @param context Application context to use
     * @param attrs Attribute set from which to read values
     */
    public AnimationSet(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        TypedArray a =
            context.obtainStyledAttributes(attrs, com.android.internal.R.styleable.AnimationSet);
        
        setFlag(PROPERTY_SHARE_INTERPOLATOR_MASK,
                a.getBoolean(com.android.internal.R.styleable.AnimationSet_shareInterpolator, true));
        init();
<<<<<<< HEAD

        if (context.getApplicationInfo().targetSdkVersion >=
                Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            if (a.hasValue(com.android.internal.R.styleable.AnimationSet_duration)) {
                mFlags |= PROPERTY_DURATION_MASK;
            }
            if (a.hasValue(com.android.internal.R.styleable.AnimationSet_fillBefore)) {
                mFlags |= PROPERTY_FILL_BEFORE_MASK;
            }
            if (a.hasValue(com.android.internal.R.styleable.AnimationSet_fillAfter)) {
                mFlags |= PROPERTY_FILL_AFTER_MASK;
            }
            if (a.hasValue(com.android.internal.R.styleable.AnimationSet_repeatMode)) {
                mFlags |= PROPERTY_REPEAT_MODE_MASK;
            }
            if (a.hasValue(com.android.internal.R.styleable.AnimationSet_startOffset)) {
                mFlags |= PROPERTY_START_OFFSET_MASK;
            }
        }

=======
        
>>>>>>> 54b6cfa... Initial Contribution
        a.recycle();
    }
    
    
    /**
     * Constructor to use when building an AnimationSet from code
     * 
     * @param shareInterpolator Pass true if all of the animations in this set
     *        should use the interpolator assocciated with this AnimationSet.
     *        Pass false if each animation should use its own interpolator.
     */
    public AnimationSet(boolean shareInterpolator) {
        setFlag(PROPERTY_SHARE_INTERPOLATOR_MASK, shareInterpolator);
        init();
    }

<<<<<<< HEAD
    @Override
    protected AnimationSet clone() throws CloneNotSupportedException {
        final AnimationSet animation = (AnimationSet) super.clone();
        animation.mTempTransformation = new Transformation();
        animation.mAnimations = new ArrayList<Animation>();

        final int count = mAnimations.size();
        final ArrayList<Animation> animations = mAnimations;

        for (int i = 0; i < count; i++) {
            animation.mAnimations.add(animations.get(i).clone());
        }

        return animation;
    }

=======
>>>>>>> 54b6cfa... Initial Contribution
    private void setFlag(int mask, boolean value) {
        if (value) {
            mFlags |= mask;
        } else {
            mFlags &= ~mask;
        }
    }

    private void init() {
        mStartTime = 0;
<<<<<<< HEAD
=======
        mDuration = 0;
>>>>>>> 54b6cfa... Initial Contribution
    }

    @Override
    public void setFillAfter(boolean fillAfter) {
        mFlags |= PROPERTY_FILL_AFTER_MASK;
        super.setFillAfter(fillAfter);
    }

    @Override
    public void setFillBefore(boolean fillBefore) {
        mFlags |= PROPERTY_FILL_BEFORE_MASK;
        super.setFillBefore(fillBefore);
    }

    @Override
    public void setRepeatMode(int repeatMode) {
        mFlags |= PROPERTY_REPEAT_MODE_MASK;
        super.setRepeatMode(repeatMode);
    }

    @Override
    public void setStartOffset(long startOffset) {
        mFlags |= PROPERTY_START_OFFSET_MASK;
        super.setStartOffset(startOffset);
    }

    /**
<<<<<<< HEAD
     * @hide
     */
    @Override
    public boolean hasAlpha() {
        if (mDirty) {
            mDirty = mHasAlpha = false;

            final int count = mAnimations.size();
            final ArrayList<Animation> animations = mAnimations;

            for (int i = 0; i < count; i++) {
                if (animations.get(i).hasAlpha()) {
                    mHasAlpha = true;
                    break;
                }
            }
        }

        return mHasAlpha;
    }

    /**
=======
>>>>>>> 54b6cfa... Initial Contribution
     * <p>Sets the duration of every child animation.</p>
     *
     * @param durationMillis the duration of the animation, in milliseconds, for
     *        every child in this set
     */
    @Override
    public void setDuration(long durationMillis) {
        mFlags |= PROPERTY_DURATION_MASK;
        super.setDuration(durationMillis);
<<<<<<< HEAD
        mLastEnd = mStartOffset + mDuration;
=======
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Add a child animation to this animation set.
     * The transforms of the child animations are applied in the order
     * that they were added
     * @param a Animation to add.
     */
    public void addAnimation(Animation a) {
        mAnimations.add(a);

        boolean noMatrix = (mFlags & PROPERTY_MORPH_MATRIX_MASK) == 0;
        if (noMatrix && a.willChangeTransformationMatrix()) {
            mFlags |= PROPERTY_MORPH_MATRIX_MASK;
        }

<<<<<<< HEAD
        boolean changeBounds = (mFlags & PROPERTY_CHANGE_BOUNDS_MASK) == 0;


        if (changeBounds && a.willChangeBounds()) {
            mFlags |= PROPERTY_CHANGE_BOUNDS_MASK;
        }

        if ((mFlags & PROPERTY_DURATION_MASK) == PROPERTY_DURATION_MASK) {
            mLastEnd = mStartOffset + mDuration;
        } else {
            if (mAnimations.size() == 1) {
                mDuration = a.getStartOffset() + a.getDuration();
                mLastEnd = mStartOffset + mDuration;
            } else {
                mLastEnd = Math.max(mLastEnd, a.getStartOffset() + a.getDuration());
                mDuration = mLastEnd - mStartOffset;
            }
        }

        mDirty = true;
=======
        if (mAnimations.size() == 1) {
            mDuration = a.getStartOffset() + a.getDuration();
            mLastEnd = mStartOffset + mDuration;
        } else {
            mLastEnd = Math.max(mLastEnd, a.getStartOffset() + a.getDuration());
            mDuration = mLastEnd - mStartOffset;
        }
>>>>>>> 54b6cfa... Initial Contribution
    }
    
    /**
     * Sets the start time of this animation and all child animations
     * 
     * @see android.view.animation.Animation#setStartTime(long)
     */
    @Override
    public void setStartTime(long startTimeMillis) {
        super.setStartTime(startTimeMillis);

        final int count = mAnimations.size();
        final ArrayList<Animation> animations = mAnimations;

        for (int i = 0; i < count; i++) {
            Animation a = animations.get(i);
            a.setStartTime(startTimeMillis);
        }
    }

    @Override
    public long getStartTime() {
        long startTime = Long.MAX_VALUE;

        final int count = mAnimations.size();
        final ArrayList<Animation> animations = mAnimations;

        for (int i = 0; i < count; i++) {
            Animation a = animations.get(i);
            startTime = Math.min(startTime, a.getStartTime());
        }

        return startTime;
    }

    @Override
    public void restrictDuration(long durationMillis) {
        super.restrictDuration(durationMillis);

        final ArrayList<Animation> animations = mAnimations;
        int count = animations.size();

        for (int i = 0; i < count; i++) {
            animations.get(i).restrictDuration(durationMillis);
        }
    }
    
    /**
     * The duration of an AnimationSet is defined to be the 
     * duration of the longest child animation.
     * 
     * @see android.view.animation.Animation#getDuration()
     */
    @Override
    public long getDuration() {
        final ArrayList<Animation> animations = mAnimations;
        final int count = animations.size();
        long duration = 0;

        boolean durationSet = (mFlags & PROPERTY_DURATION_MASK) == PROPERTY_DURATION_MASK;
        if (durationSet) {
            duration = mDuration;
        } else {
            for (int i = 0; i < count; i++) {
                duration = Math.max(duration, animations.get(i).getDuration());
            }
        }

        return duration;
    }

    /**
<<<<<<< HEAD
     * The duration hint of an animation set is the maximum of the duration
     * hints of all of its component animations.
     * 
     * @see android.view.animation.Animation#computeDurationHint
     */
    public long computeDurationHint() {
        long duration = 0;
        final int count = mAnimations.size();
        final ArrayList<Animation> animations = mAnimations;
        for (int i = count - 1; i >= 0; --i) {
            final long d = animations.get(i).computeDurationHint();
            if (d > duration) duration = d;
        }
        return duration;
    }

    /**
     * @hide
     */
    public void initializeInvalidateRegion(int left, int top, int right, int bottom) {
        final RectF region = mPreviousRegion;
        region.set(left, top, right, bottom);
        region.inset(-1.0f, -1.0f);

        if (mFillBefore) {
            final int count = mAnimations.size();
            final ArrayList<Animation> animations = mAnimations;
            final Transformation temp = mTempTransformation;

            final Transformation previousTransformation = mPreviousTransformation;

            for (int i = count - 1; i >= 0; --i) {
                final Animation a = animations.get(i);
                if (!a.isFillEnabled() || a.getFillBefore() || a.getStartOffset() == 0) {
                    temp.clear();
                    final Interpolator interpolator = a.mInterpolator;
                    a.applyTransformation(interpolator != null ? interpolator.getInterpolation(0.0f)
                            : 0.0f, temp);
                    previousTransformation.compose(temp);
                }
            }
        }
    }

    /**
=======
>>>>>>> 54b6cfa... Initial Contribution
     * The transformation of an animation set is the concatenation of all of its
     * component animations.
     * 
     * @see android.view.animation.Animation#getTransformation
     */
    @Override
    public boolean getTransformation(long currentTime, Transformation t) {
        final int count = mAnimations.size();
        final ArrayList<Animation> animations = mAnimations;
        final Transformation temp = mTempTransformation;

        boolean more = false;
        boolean started = false;
        boolean ended = true;

        t.clear();

        for (int i = count - 1; i >= 0; --i) {
            final Animation a = animations.get(i);

            temp.clear();
<<<<<<< HEAD
            more = a.getTransformation(currentTime, temp, getScaleFactor()) || more;
=======
            more = a.getTransformation(currentTime, temp) || more;
>>>>>>> 54b6cfa... Initial Contribution
            t.compose(temp);

            started = started || a.hasStarted();
            ended = a.hasEnded() && ended;
        }

        if (started && !mStarted) {
            if (mListener != null) {
                mListener.onAnimationStart(this);
            }
            mStarted = true;
        }

        if (ended != mEnded) {
            if (mListener != null) {
                mListener.onAnimationEnd(this);
            }
            mEnded = ended;
        }

        return more;
    }
    
    /**
     * @see android.view.animation.Animation#scaleCurrentDuration(float)
     */
    @Override
    public void scaleCurrentDuration(float scale) {
        final ArrayList<Animation> animations = mAnimations;
        int count = animations.size();
        for (int i = 0; i < count; i++) {
            animations.get(i).scaleCurrentDuration(scale);
        }
    }

    /**
     * @see android.view.animation.Animation#initialize(int, int, int, int)
     */
    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);

        boolean durationSet = (mFlags & PROPERTY_DURATION_MASK) == PROPERTY_DURATION_MASK;
        boolean fillAfterSet = (mFlags & PROPERTY_FILL_AFTER_MASK) == PROPERTY_FILL_AFTER_MASK;
        boolean fillBeforeSet = (mFlags & PROPERTY_FILL_BEFORE_MASK) == PROPERTY_FILL_BEFORE_MASK;
        boolean repeatModeSet = (mFlags & PROPERTY_REPEAT_MODE_MASK) == PROPERTY_REPEAT_MODE_MASK;
        boolean shareInterpolator = (mFlags & PROPERTY_SHARE_INTERPOLATOR_MASK)
                == PROPERTY_SHARE_INTERPOLATOR_MASK;
        boolean startOffsetSet = (mFlags & PROPERTY_START_OFFSET_MASK)
                == PROPERTY_START_OFFSET_MASK;
<<<<<<< HEAD

=======
       
>>>>>>> 54b6cfa... Initial Contribution
        if (shareInterpolator) {
            ensureInterpolator();
        }

        final ArrayList<Animation> children = mAnimations;
        final int count = children.size();

        final long duration = mDuration;
        final boolean fillAfter = mFillAfter;
        final boolean fillBefore = mFillBefore;
        final int repeatMode = mRepeatMode;
        final Interpolator interpolator = mInterpolator;
        final long startOffset = mStartOffset;
<<<<<<< HEAD


        long[] storedOffsets = mStoredOffsets;
        if (startOffsetSet) {
            if (storedOffsets == null || storedOffsets.length != count) {
                storedOffsets = mStoredOffsets = new long[count];
            }
        } else if (storedOffsets != null) {
            storedOffsets = mStoredOffsets = null;
        }

=======
        
>>>>>>> 54b6cfa... Initial Contribution
        for (int i = 0; i < count; i++) {
            Animation a = children.get(i);
            if (durationSet) {
                a.setDuration(duration);
            }
            if (fillAfterSet) {
                a.setFillAfter(fillAfter);
            }
            if (fillBeforeSet) {
                a.setFillBefore(fillBefore);
            }
            if (repeatModeSet) {
                a.setRepeatMode(repeatMode);
            }
            if (shareInterpolator) {
                a.setInterpolator(interpolator);
            }
            if (startOffsetSet) {
<<<<<<< HEAD
                long offset = a.getStartOffset();
                a.setStartOffset(offset + startOffset);
                storedOffsets[i] = offset;
=======
                a.setStartOffset(startOffset);
>>>>>>> 54b6cfa... Initial Contribution
            }
            a.initialize(width, height, parentWidth, parentHeight);
        }
    }

<<<<<<< HEAD
    @Override
    public void reset() {
        super.reset();
        restoreChildrenStartOffset();
=======
    /**
     * @hide
     * @param startOffset the startOffset to add to the children's startOffset
     */
    void saveChildrenStartOffset(long startOffset) {
        final ArrayList<Animation> children = mAnimations;
        final int count = children.size();
        long[] storedOffsets = mStoredOffsets = new long[count];

        for (int i = 0; i < count; i++) {
            Animation animation = children.get(i);
            long offset = animation.getStartOffset();
            animation.setStartOffset(offset + startOffset);
            storedOffsets[i] = offset;
        }
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * @hide
     */
    void restoreChildrenStartOffset() {
<<<<<<< HEAD
        final long[] offsets = mStoredOffsets;
        if (offsets == null) return;

        final ArrayList<Animation> children = mAnimations;
        final int count = children.size();
=======
        final ArrayList<Animation> children = mAnimations;
        final int count = children.size();
        final long[] offsets = mStoredOffsets;
>>>>>>> 54b6cfa... Initial Contribution

        for (int i = 0; i < count; i++) {
            children.get(i).setStartOffset(offsets[i]);
        }
    }
<<<<<<< HEAD

=======
    
>>>>>>> 54b6cfa... Initial Contribution
    /**
     * @return All the child animations in this AnimationSet. Note that
     * this may include other AnimationSets, which are not expanded.
     */
    public List<Animation> getAnimations() {
        return mAnimations;
    }

    @Override
    public boolean willChangeTransformationMatrix() {
        return (mFlags & PROPERTY_MORPH_MATRIX_MASK) == PROPERTY_MORPH_MATRIX_MASK;
    }
<<<<<<< HEAD

    @Override
    public boolean willChangeBounds() {
        return (mFlags & PROPERTY_CHANGE_BOUNDS_MASK) == PROPERTY_CHANGE_BOUNDS_MASK;
    }
=======
>>>>>>> 54b6cfa... Initial Contribution
}
