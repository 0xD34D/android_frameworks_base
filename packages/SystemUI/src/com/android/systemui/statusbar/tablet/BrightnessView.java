/*
 * Copyright (C) 2010 The Android Open Source Project
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

package com.android.systemui.statusbar.tablet;

import android.app.StatusBarManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Slog;
import android.widget.RelativeLayout;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.systemui.R;
import com.android.systemui.statusbar.policy.ToggleSlider;
import com.android.systemui.statusbar.policy.BrightnessController;
import com.android.systemui.statusbar.tablet.StatusBarPanel;

public class BrightnessView extends RelativeLayout implements StatusBarPanel {
    static final String TAG = "BrightnessView";

    BrightnessController mBrightness;
    boolean mShowing;
    Rect mContentArea = new Rect();

    public BrightnessView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BrightnessView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        final Context context = getContext();

        mBrightness = new BrightnessController(context,
                (ToggleSlider)findViewById(R.id.brightness));
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    /**
     * Whether the panel is showing, or, if it's animating, whether it will be
     * when the animation is done.
     */
    public boolean isShowing() {
        return mShowing;
    }

    public void show(boolean show, boolean animate) {
        mShowing = show;
        setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public boolean isInContentArea(int x, int y) {
        mContentArea.left = this.getLeft() + this.getPaddingLeft();
        mContentArea.top = this.getTop() + this.getPaddingTop();
        mContentArea.right = this.getRight() - this.getPaddingRight();
        mContentArea.bottom = this.getBottom() - this.getPaddingBottom();

        return mContentArea.contains(x, y);
    }
}

