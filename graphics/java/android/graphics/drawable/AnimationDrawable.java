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

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.res.Resources;
import android.content.res.TypedArray;
<<<<<<< HEAD
=======
import android.graphics.Canvas;
>>>>>>> 54b6cfa... Initial Contribution
import android.os.SystemClock;
import android.util.AttributeSet;

/**
 * 
<<<<<<< HEAD
 * An object used to create frame-by-frame animations, defined by a series of Drawable objects,
 * which can be used as a View object's background.
 * <p>
 * The simplest way to create a frame-by-frame animation is to define the animation in an XML
 * file, placed in the res/drawable/ folder, and set it as the background to a View object. Then, call
 * {@link #start()} to run the animation.
 * <p>
 * An AnimationDrawable defined in XML consists of a single <code>&lt;animation-list></code> element,
 * and a series of nested <code>&lt;item></code> tags. Each item defines a frame of the animation.
 * See the example below.
 * </p>
 * <p>spin_animation.xml file in res/drawable/ folder:</p>
 * <pre>&lt;!-- Animation frames are wheel0.png -- wheel5.png files inside the
 * res/drawable/ folder --&gt;
 * &lt;animation-list android:id=&quot;@+id/selected&quot; android:oneshot=&quot;false&quot;&gt;
=======
 * An object used to define frame-by-frame animations that can be used as a View object's
 * background.
 * <p>Each frame in a frame-by-frame animation is a drawable 
 * <a href="{@docRoot}devel/resources-i18n.html">resource</a>.
 * The simplest way to create a frame-by-frame animation is to define the animation in an XML
 * file in the drawable/ folder, set it as the background to a View object, then call
 * AnimationDrawable.run() to start the animation, as shown here. More details about the
 * format of the animation XML file are given in
 * <a href="{@docRoot}reference/available-resources.html#animationdrawable">Frame by Frame
 * Animation</a>.
 * spin_animation.xml file in res/drawable/ folder:</p>
 * <pre>&lt;!-- Animation frames are wheel0.png -- wheel5.png files inside the
 * res/drawable/ folder --&gt;
 * &lt;animation-list android:id=&quot;selected&quot; android:oneshot=&quot;false&quot;&gt;
>>>>>>> 54b6cfa... Initial Contribution
 *    &lt;item android:drawable=&quot;@drawable/wheel0&quot; android:duration=&quot;50&quot; /&gt;
 *    &lt;item android:drawable=&quot;@drawable/wheel1&quot; android:duration=&quot;50&quot; /&gt;
 *    &lt;item android:drawable=&quot;@drawable/wheel2&quot; android:duration=&quot;50&quot; /&gt;
 *    &lt;item android:drawable=&quot;@drawable/wheel3&quot; android:duration=&quot;50&quot; /&gt;
 *    &lt;item android:drawable=&quot;@drawable/wheel4&quot; android:duration=&quot;50&quot; /&gt;
 *    &lt;item android:drawable=&quot;@drawable/wheel5&quot; android:duration=&quot;50&quot; /&gt;
 * &lt;/animation-list&gt;</pre>
 *
 * <p>Here is the code to load and play this animation.</p>
<<<<<<< HEAD
 * <pre>
 * // Load the ImageView that will host the animation and
=======
 * <pre>// Load the ImageView that will host the animation and
>>>>>>> 54b6cfa... Initial Contribution
 * // set its background to our AnimationDrawable XML resource.
 * ImageView img = (ImageView)findViewById(R.id.spinning_wheel_image);
 * img.setBackgroundResource(R.drawable.spin_animation);
 *
 * // Get the background, which has been compiled to an AnimationDrawable object.
 * AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();
 *
 * // Start the animation (looped playback by default).
<<<<<<< HEAD
 * frameAnimation.start();
 * </pre>
 *
 * <div class="special reference">
 * <h3>Developer Guides</h3>
 * <p>For more information about animating with {@code AnimationDrawable}, read the
 * <a href="{@docRoot}guide/topics/graphics/drawable-animation.html">Drawable Animation</a>
 * developer guide.</p>
 * </div>
 *
 * @attr ref android.R.styleable#AnimationDrawable_visible
 * @attr ref android.R.styleable#AnimationDrawable_variablePadding
 * @attr ref android.R.styleable#AnimationDrawable_oneshot
 * @attr ref android.R.styleable#AnimationDrawableItem_duration
 * @attr ref android.R.styleable#AnimationDrawableItem_drawable
 */
public class AnimationDrawable extends DrawableContainer implements Runnable, Animatable {
    private final AnimationState mAnimationState;
    private int mCurFrame = -1;
    private boolean mMutated;

    public AnimationDrawable() {
        this(null, null);
=======
 * frameAnimation.start()
 * </pre>
 */
public class AnimationDrawable extends DrawableContainer implements Runnable {
    public AnimationDrawable() {
        this(null);
>>>>>>> 54b6cfa... Initial Contribution
    }

    @Override
    public boolean setVisible(boolean visible, boolean restart) {
        boolean changed = super.setVisible(visible, restart);
        if (visible) {
            if (changed || restart) {
                setFrame(0, true, true);
            }
        } else {
            unscheduleSelf(this);
        }
        return changed;
    }

    /**
     * <p>Starts the animation, looping if necessary. This method has no effect
<<<<<<< HEAD
     * if the animation is running. Do not call this in the {@link android.app.Activity#onCreate}
     * method of your activity, because the {@link android.graphics.drawable.AnimationDrawable} is
     * not yet fully attached to the window. If you want to play
     * the animation immediately, without requiring interaction, then you might want to call it
     * from the {@link android.app.Activity#onWindowFocusChanged} method in your activity,
     * which will get called when Android brings your window into focus.</p>
=======
     * if the animation is running.</p>
>>>>>>> 54b6cfa... Initial Contribution
     *
     * @see #isRunning()
     * @see #stop()
     */
    public void start() {
        if (!isRunning()) {
            run();
        }
    }

    /**
     * <p>Stops the animation. This method has no effect if the animation is
     * not running.</p>
     *
     * @see #isRunning()
     * @see #start()
     */
    public void stop() {
        if (isRunning()) {
            unscheduleSelf(this);
        }
    }

    /**
     * <p>Indicates whether the animation is currently running or not.</p>
     *
     * @return true if the animation is running, false otherwise
     */
    public boolean isRunning() {
        return mCurFrame > -1;
    }

    /**
     * <p>This method exists for implementation purpose only and should not be
     * called directly. Invoke {@link #start()} instead.</p>
     *
     * @see #start()
     */
    public void run() {
        nextFrame(false);
    }

    @Override
    public void unscheduleSelf(Runnable what) {
        mCurFrame = -1;
        super.unscheduleSelf(what);
    }

    /**
     * @return The number of frames in the animation
     */
    public int getNumberOfFrames() {
        return mAnimationState.getChildCount();
    }
    
    /**
     * @return The Drawable at the specified frame index
     */
    public Drawable getFrame(int index) {
        return mAnimationState.getChildren()[index];
    }
    
    /**
     * @return The duration in milliseconds of the frame at the 
     * specified index
     */
    public int getDuration(int i) {
        return mAnimationState.mDurations[i];
    }
    
    /**
     * @return True of the animation will play once, false otherwise
     */
    public boolean isOneShot() {
        return mAnimationState.mOneShot;
    }
    
    /**
     * Sets whether the animation should play once or repeat.
     * 
     * @param oneShot Pass true if the animation should only play once
     */
    public void setOneShot(boolean oneShot) {
        mAnimationState.mOneShot = oneShot;
    }
    
    /**
     * Add a frame to the animation
     * 
     * @param frame The frame to add
     * @param duration How long in milliseconds the frame should appear
     */
    public void addFrame(Drawable frame, int duration) {
        mAnimationState.addFrame(frame, duration);
<<<<<<< HEAD
        if (mCurFrame < 0) {
            setFrame(0, true, false);
        }
=======
>>>>>>> 54b6cfa... Initial Contribution
    }
    
    private void nextFrame(boolean unschedule) {
        int next = mCurFrame+1;
        final int N = mAnimationState.getChildCount();
        if (next >= N) {
            next = 0;
        }
<<<<<<< HEAD
        setFrame(next, unschedule, !mAnimationState.mOneShot || next < (N - 1));
=======
        setFrame(next, unschedule, !mAnimationState.mOneShot || next < (N-1));
>>>>>>> 54b6cfa... Initial Contribution
    }

    private void setFrame(int frame, boolean unschedule, boolean animate) {
        if (frame >= mAnimationState.getChildCount()) {
            return;
        }
        mCurFrame = frame;
        selectDrawable(frame);
        if (unschedule) {
            unscheduleSelf(this);
        }
        if (animate) {
<<<<<<< HEAD
            // Unscheduling may have clobbered this value; restore it to record that we're animating
            mCurFrame = frame;
            scheduleSelf(this, SystemClock.uptimeMillis() + mAnimationState.mDurations[frame]);
=======
            scheduleSelf(this, SystemClock.uptimeMillis()
                         + mAnimationState.mDurations[frame]);
>>>>>>> 54b6cfa... Initial Contribution
        }
    }

    @Override
<<<<<<< HEAD
    public void inflate(Resources r, XmlPullParser parser, AttributeSet attrs)
=======
    public void inflate(Resources r, XmlPullParser parser,
            AttributeSet attrs)
>>>>>>> 54b6cfa... Initial Contribution
            throws XmlPullParserException, IOException {
        
        TypedArray a = r.obtainAttributes(attrs,
                com.android.internal.R.styleable.AnimationDrawable);

        super.inflateWithAttributes(r, parser, a,
                com.android.internal.R.styleable.AnimationDrawable_visible);
        
        mAnimationState.setVariablePadding(a.getBoolean(
                com.android.internal.R.styleable.AnimationDrawable_variablePadding, false));
            
        mAnimationState.mOneShot = a.getBoolean(
                com.android.internal.R.styleable.AnimationDrawable_oneshot, false);
        
        a.recycle();
        
        int type;

        final int innerDepth = parser.getDepth()+1;
        int depth;
<<<<<<< HEAD
        while ((type=parser.next()) != XmlPullParser.END_DOCUMENT &&
                ((depth = parser.getDepth()) >= innerDepth || type != XmlPullParser.END_TAG)) {
=======
        while ((type=parser.next()) != XmlPullParser.END_DOCUMENT
               && ((depth=parser.getDepth()) >= innerDepth
                       || type != XmlPullParser.END_TAG)) {
>>>>>>> 54b6cfa... Initial Contribution
            if (type != XmlPullParser.START_TAG) {
                continue;
            }

            if (depth > innerDepth || !parser.getName().equals("item")) {
                continue;
            }
            
<<<<<<< HEAD
            a = r.obtainAttributes(attrs, com.android.internal.R.styleable.AnimationDrawableItem);
=======
            a = r.obtainAttributes(attrs,
                    com.android.internal.R.styleable.AnimationDrawableItem);
            
>>>>>>> 54b6cfa... Initial Contribution
            int duration = a.getInt(
                    com.android.internal.R.styleable.AnimationDrawableItem_duration, -1);
            if (duration < 0) {
                throw new XmlPullParserException(
                        parser.getPositionDescription()
                        + ": <item> tag requires a 'duration' attribute");
            }
            int drawableRes = a.getResourceId(
                    com.android.internal.R.styleable.AnimationDrawableItem_drawable, 0);
            
            a.recycle();
            
            Drawable dr;
            if (drawableRes != 0) {
                dr = r.getDrawable(drawableRes);
            } else {
                while ((type=parser.next()) == XmlPullParser.TEXT) {
<<<<<<< HEAD
                    // Empty
                }
                if (type != XmlPullParser.START_TAG) {
                    throw new XmlPullParserException(parser.getPositionDescription() +
                            ": <item> tag requires a 'drawable' attribute or child tag" +
                            " defining a drawable");
=======
                }
                if (type != XmlPullParser.START_TAG) {
                    throw new XmlPullParserException(
                            parser.getPositionDescription()
                            + ": <item> tag requires a 'drawable' attribute or "
                            + "child tag defining a drawable");
>>>>>>> 54b6cfa... Initial Contribution
                }
                dr = Drawable.createFromXmlInner(r, parser, attrs);
            }
            
            mAnimationState.addFrame(dr, duration);
            if (dr != null) {
                dr.setCallback(this);
            }
        }

        setFrame(0, true, false);
    }

<<<<<<< HEAD
    @Override
    public Drawable mutate() {
        if (!mMutated && super.mutate() == this) {
            mAnimationState.mDurations = mAnimationState.mDurations.clone();
            mMutated = true;
        }
        return this;
    }

    private final static class AnimationState extends DrawableContainerState {
        private int[] mDurations;
        private boolean mOneShot;

        AnimationState(AnimationState orig, AnimationDrawable owner,
                Resources res) {
            super(orig, owner, res);
=======
    private final static class AnimationState extends DrawableContainerState
    {
        AnimationState(AnimationState orig, AnimationDrawable owner)
        {
            super(orig, owner);
>>>>>>> 54b6cfa... Initial Contribution

            if (orig != null) {
                mDurations = orig.mDurations;
                mOneShot = orig.mOneShot;
            } else {
                mDurations = new int[getChildren().length];
                mOneShot = true;
            }
        }

        @Override
<<<<<<< HEAD
        public Drawable newDrawable() {
            return new AnimationDrawable(this, null);
        }

        @Override
        public Drawable newDrawable(Resources res) {
            return new AnimationDrawable(this, res);
        }

        public void addFrame(Drawable dr, int dur) {
            // Do not combine the following. The array index must be evaluated before 
            // the array is accessed because super.addChild(dr) has a side effect on mDurations.
=======
        public Drawable newDrawable()
        {
            return new AnimationDrawable(this);
        }

        public void addFrame(Drawable dr, int dur)
        {
>>>>>>> 54b6cfa... Initial Contribution
            int pos = super.addChild(dr);
            mDurations[pos] = dur;
        }

        @Override
<<<<<<< HEAD
        public void growArray(int oldSize, int newSize) {
=======
        public void growArray(int oldSize, int newSize)
        {
>>>>>>> 54b6cfa... Initial Contribution
            super.growArray(oldSize, newSize);
            int[] newDurations = new int[newSize];
            System.arraycopy(mDurations, 0, newDurations, 0, oldSize);
            mDurations = newDurations;
        }
<<<<<<< HEAD
    }

    private AnimationDrawable(AnimationState state, Resources res) {
        AnimationState as = new AnimationState(state, this, res);
=======

        private int[]       mDurations;
        private boolean     mOneShot;
    }

    private AnimationDrawable(AnimationState state) {
        AnimationState as = new AnimationState(state, this);
>>>>>>> 54b6cfa... Initial Contribution
        mAnimationState = as;
        setConstantState(as);
        if (state != null) {
            setFrame(0, true, false);
        }
    }
<<<<<<< HEAD
=======

    private final AnimationState mAnimationState;

    private int mCurFrame = -1;
>>>>>>> 54b6cfa... Initial Contribution
}

