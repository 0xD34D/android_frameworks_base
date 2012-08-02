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

package com.android.systemui.recent;

import com.android.internal.util.MemInfoReader;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Slog;
import android.widget.RelativeLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;

import com.android.systemui.R;
import com.android.systemui.recent.BarGraphView;

public class MemoryInfoView extends RelativeLayout {
    static final String TAG = "MemoryInfoView";
    static final boolean DEBUG = true;

    MemInfoReader mMemInfoReader = new MemInfoReader();

    TextView mMemFree;
    TextView mMemUsed;
    BarGraphView mBarGraphView;

    public MemoryInfoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MemoryInfoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
		Resources res = context.getResources();
        if (DEBUG)
            Slog.i(TAG, "Creating MemoryInfoView");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        final Context context = getContext();
        mMemFree = (TextView)findViewById(R.id.free_memory);
        mMemUsed = (TextView)findViewById(R.id.used_memory);
        mBarGraphView = (BarGraphView)findViewById(R.id.bar_graph);

        mMemInfoReader.readMemInfo();
        long free = mMemInfoReader.getFreeSize() + mMemInfoReader.getCachedSize();
        long used = mMemInfoReader.getTotalSize() - free;
        mMemFree.setText(String.format("%d MB", free/1048576));
        mMemUsed.setText(String.format("%d MB", used/1048576));
        if (DEBUG)
            Slog.i(TAG, String.format("Memory: %d/%d", free, free+used));
        mBarGraphView.setLevels(free, used);
    }

    public void update() {
        mMemInfoReader.readMemInfo();
        long free = mMemInfoReader.getFreeSize() + mMemInfoReader.getCachedSize();
        long used = mMemInfoReader.getTotalSize() - free;
        mMemFree.setText(String.format("%d MB", free/1048576));
        mMemUsed.setText(String.format("%d MB", used/1048576));
        if (DEBUG)
            Slog.i(TAG, String.format("Memory: %d/%d", free, free+used));
        mBarGraphView.setLevels(free, used);
        mBarGraphView.invalidate();
        mMemFree.invalidate();
        mMemUsed.invalidate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}

