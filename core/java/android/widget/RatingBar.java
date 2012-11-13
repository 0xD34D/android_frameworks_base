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

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;
<<<<<<< HEAD
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
=======
>>>>>>> 54b6cfa... Initial Contribution

import com.android.internal.R;

/**
 * A RatingBar is an extension of SeekBar and ProgressBar that shows a rating in
<<<<<<< HEAD
 * stars. The user can touch/drag or use arrow keys to set the rating when using
 * the default size RatingBar. The smaller RatingBar style (
 * {@link android.R.attr#ratingBarStyleSmall}) and the larger indicator-only
 * style ({@link android.R.attr#ratingBarStyleIndicator}) do not support user
 * interaction and should only be used as indicators.
 * <p>
 * When using a RatingBar that supports user interaction, placing widgets to the
 * left or right of the RatingBar is discouraged.
=======
 * stars. The user can touch and/or drag to set the rating when using the
 * default size RatingBar. The smaller RatingBar style ({@link android.R.attr#ratingBarStyleSmall})
 * and the larger indicator-only style ({@link android.R.attr#ratingBarStyleIndicator})
 * do not support user interaction and should only be used as indicators.
>>>>>>> 54b6cfa... Initial Contribution
 * <p>
 * The number of stars set (via {@link #setNumStars(int)} or in an XML layout)
 * will be shown when the layout width is set to wrap content (if another layout
 * width is set, the results may be unpredictable).
 * <p>
 * The secondary progress should not be modified by the client as it is used
 * internally as the background for a fractionally filled star.
 * 
 * @attr ref android.R.styleable#RatingBar_numStars
 * @attr ref android.R.styleable#RatingBar_rating
 * @attr ref android.R.styleable#RatingBar_stepSize
 * @attr ref android.R.styleable#RatingBar_isIndicator
 */
public class RatingBar extends AbsSeekBar {
<<<<<<< HEAD

    /**
     * A callback that notifies clients when the rating has been changed. This
     * includes changes that were initiated by the user through a touch gesture
     * or arrow key/trackball as well as changes that were initiated
     * programmatically.
=======
    
    /**
     * A callback that notifies clients when the rating has been changed. This 
     * includes changes that were initiated by the user through a touch gesture as well
     * as changes that were initiated programmatically.
>>>>>>> 54b6cfa... Initial Contribution
     */
    public interface OnRatingBarChangeListener {
        
        /**
         * Notification that the rating has changed. Clients can use the
<<<<<<< HEAD
         * fromUser parameter to distinguish user-initiated changes from those
=======
         * fromTouch parameter to distinguish user-initiated changes from those
>>>>>>> 54b6cfa... Initial Contribution
         * that occurred programmatically. This will not be called continuously
         * while the user is dragging, only when the user finalizes a rating by
         * lifting the touch.
         * 
         * @param ratingBar The RatingBar whose rating has changed.
         * @param rating The current rating. This will be in the range
         *            0..numStars.
<<<<<<< HEAD
         * @param fromUser True if the rating change was initiated by a user's
         *            touch gesture or arrow key/horizontal trackbell movement.
         */
        void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser);
=======
         * @param fromTouch True if the rating change was initiated by a user's
         *            touch gesture.
         */
        void onRatingChanged(RatingBar ratingBar, float rating, boolean fromTouch);
>>>>>>> 54b6cfa... Initial Contribution

    }

    private int mNumStars = 5;

    private int mProgressOnStartTracking;
    
    private OnRatingBarChangeListener mOnRatingBarChangeListener;
    
    public RatingBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RatingBar,
                defStyle, 0);
        final int numStars = a.getInt(R.styleable.RatingBar_numStars, mNumStars);
        setIsIndicator(a.getBoolean(R.styleable.RatingBar_isIndicator, !mIsUserSeekable));
        final float rating = a.getFloat(R.styleable.RatingBar_rating, -1);
        final float stepSize = a.getFloat(R.styleable.RatingBar_stepSize, -1);
        a.recycle();

        if (numStars > 0 && numStars != mNumStars) {
            setNumStars(numStars);            
        }
        
        if (stepSize >= 0) {
            setStepSize(stepSize);
        } else {
            setStepSize(0.5f);
        }
        
        if (rating >= 0) {
            setRating(rating);
        }
        
        // A touch inside a star fill up to that fractional area (slightly more
        // than 1 so boundaries round up).
        mTouchProgressOffset = 1.1f;
    }

    public RatingBar(Context context, AttributeSet attrs) {
        this(context, attrs, com.android.internal.R.attr.ratingBarStyle);
    }

    public RatingBar(Context context) {
        this(context, null);
    }
    
    /**
     * Sets the listener to be called when the rating changes.
     * 
     * @param listener The listener.
     */
    public void setOnRatingBarChangeListener(OnRatingBarChangeListener listener) {
        mOnRatingBarChangeListener = listener;
    }
    
    /**
     * @return The listener (may be null) that is listening for rating change
     *         events.
     */
    public OnRatingBarChangeListener getOnRatingBarChangeListener() {
        return mOnRatingBarChangeListener;
    }

    /**
     * Whether this rating bar should only be an indicator (thus non-changeable
     * by the user).
     * 
     * @param isIndicator Whether it should be an indicator.
<<<<<<< HEAD
     *
     * @attr ref android.R.styleable#RatingBar_isIndicator
     */
    public void setIsIndicator(boolean isIndicator) {
        mIsUserSeekable = !isIndicator;
        setFocusable(!isIndicator);
=======
     */
    public void setIsIndicator(boolean isIndicator) {
        mIsUserSeekable = !isIndicator;
>>>>>>> 54b6cfa... Initial Contribution
    }
    
    /**
     * @return Whether this rating bar is only an indicator.
<<<<<<< HEAD
     *
     * @attr ref android.R.styleable#RatingBar_isIndicator
=======
>>>>>>> 54b6cfa... Initial Contribution
     */
    public boolean isIndicator() {
        return !mIsUserSeekable;
    }
    
    /**
     * Sets the number of stars to show. In order for these to be shown
     * properly, it is recommended the layout width of this widget be wrap
     * content.
     * 
     * @param numStars The number of stars.
     */
    public void setNumStars(final int numStars) {
        if (numStars <= 0) {
            return;
        }
        
        mNumStars = numStars;
        
        // This causes the width to change, so re-layout
        requestLayout();
    }

    /**
     * Returns the number of stars shown.
     * @return The number of stars shown.
     */
    public int getNumStars() {
        return mNumStars;
    }
    
    /**
     * Sets the rating (the number of stars filled).
     * 
     * @param rating The rating to set.
     */
    public void setRating(float rating) {
<<<<<<< HEAD
        setProgress(Math.round(rating * getProgressPerStar()));
=======
        setProgress((int) (rating * getProgressPerStar()));
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Gets the current rating (number of stars filled).
     * 
     * @return The current rating.
     */
    public float getRating() {
        return getProgress() / getProgressPerStar();        
    }

    /**
     * Sets the step size (granularity) of this rating bar.
     * 
     * @param stepSize The step size of this rating bar. For example, if
     *            half-star granularity is wanted, this would be 0.5.
     */
    public void setStepSize(float stepSize) {
        if (stepSize <= 0) {
            return;
        }
        
        final float newMax = mNumStars / stepSize;
        final int newProgress = (int) (newMax / getMax() * getProgress());
        setMax((int) newMax);
        setProgress(newProgress);
    }

    /**
     * Gets the step size of this rating bar.
     * 
     * @return The step size.
     */
    public float getStepSize() {
        return (float) getNumStars() / getMax();
    }
    
    /**
     * @return The amount of progress that fits into a star
     */
    private float getProgressPerStar() {
        if (mNumStars > 0) {
            return 1f * getMax() / mNumStars;
        } else {
            return 1;
        }
    }

    @Override
    Shape getDrawableShape() {
        // TODO: Once ProgressBar's TODOs are fixed, this won't be needed
        return new RectShape();
    }

    @Override
<<<<<<< HEAD
    void onProgressRefresh(float scale, boolean fromUser) {
        super.onProgressRefresh(scale, fromUser);
=======
    void onProgressRefresh(float scale, boolean fromTouch) {
        super.onProgressRefresh(scale, fromTouch);
>>>>>>> 54b6cfa... Initial Contribution

        // Keep secondary progress in sync with primary
        updateSecondaryProgress(getProgress());
        
<<<<<<< HEAD
        if (!fromUser) {
            // Callback for non-user rating changes
=======
        if (!fromTouch) {
            // Callback for non-touch rating changes
>>>>>>> 54b6cfa... Initial Contribution
            dispatchRatingChange(false);
        }
    }

    /**
     * The secondary progress is used to differentiate the background of a
     * partially filled star. This method keeps the secondary progress in sync
     * with the progress.
     * 
     * @param progress The primary progress level.
     */
    private void updateSecondaryProgress(int progress) {
        final float ratio = getProgressPerStar();
        if (ratio > 0) {
            final float progressInStars = progress / ratio;
            final int secondaryProgress = (int) (Math.ceil(progressInStars) * ratio);
            setSecondaryProgress(secondaryProgress);
        }
    }
    
    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        
        if (mSampleTile != null) {
            // TODO: Once ProgressBar's TODOs are gone, this can be done more
            // cleanly than mSampleTile
            final int width = mSampleTile.getWidth() * mNumStars;
<<<<<<< HEAD
            setMeasuredDimension(resolveSizeAndState(width, widthMeasureSpec, 0),
                    getMeasuredHeight());
=======
            setMeasuredDimension(resolveSize(width, widthMeasureSpec), mMeasuredHeight);
>>>>>>> 54b6cfa... Initial Contribution
        }
    }

    @Override
    void onStartTrackingTouch() {
        mProgressOnStartTracking = getProgress();
        
        super.onStartTrackingTouch();
    }

    @Override
    void onStopTrackingTouch() {
        super.onStopTrackingTouch();

        if (getProgress() != mProgressOnStartTracking) {
            dispatchRatingChange(true);
        }
    }
<<<<<<< HEAD

    @Override
    void onKeyChange() {
        super.onKeyChange();
        dispatchRatingChange(true);
    }

    void dispatchRatingChange(boolean fromUser) {
        if (mOnRatingBarChangeListener != null) {
            mOnRatingBarChangeListener.onRatingChanged(this, getRating(),
                    fromUser);
=======
    
    void dispatchRatingChange(boolean fromTouch) {
        if (mOnRatingBarChangeListener != null) {
            mOnRatingBarChangeListener.onRatingChanged(this, getRating(),
                    fromTouch);
>>>>>>> 54b6cfa... Initial Contribution
        }
    }

    @Override
    public synchronized void setMax(int max) {
        // Disallow max progress = 0
        if (max <= 0) {
            return;
        }
        
        super.setMax(max);
    }
    
<<<<<<< HEAD
    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(RatingBar.class.getName());
    }

    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(RatingBar.class.getName());
    }
=======
>>>>>>> 54b6cfa... Initial Contribution
}
