package com.android.systemui.recent;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class BarGraphView extends View {
    Paint mUsedPaint;
    Paint mFreePaint;
    long free;
    long used;

    /**
	 * @param context
	 */
 	public BarGraphView(Context context) {
   		this(context, null);
   	}
    
   	/**
   	 * @param context
   	 * @param attrs
   	 */
   	public BarGraphView(Context context, AttributeSet attrs) {
   		this(context, attrs, 0);
   	}
    
   	/**
   	 * @param context
   	 * @param attrs
   	 * @param defStyle
   	 */
   	public BarGraphView(Context context, AttributeSet attrs, int defStyle) {
   		super(context, attrs, defStyle);
   		Resources res = context.getResources();
        mUsedPaint = new Paint();
        mUsedPaint.setColor(res.getColor(android.R.color.holo_blue_light));
        mUsedPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mUsedPaint.setAntiAlias(true);
        mFreePaint = new Paint();
        mFreePaint.setColor(res.getColor(android.R.color.background_light));
        mFreePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mFreePaint.setAntiAlias(true);
   	}

    public void setLevels(long free, long used) {
        this.free = free;
        this.used = used;
    }

   	/* (non-Javadoc)
   	 * @see android.view.View#draw(android.graphics.Canvas)
   	 */
   	@Override
   	public void draw(Canvas canvas) {
        int width = (int)((float)this.getWidth() * (float)used/(float)(used+free));
        float cy = (float)this.getHeight() / 2.0f;
        canvas.drawCircle(cy, cy, cy, mUsedPaint);
        canvas.drawRect((int)cy, 0, width, this.getHeight(), mUsedPaint);
        float cx = this.getWidth() - cy;
        canvas.drawCircle(cx, cy, cy, mFreePaint);
        canvas.drawRect(width, 0, (int)cx, this.getHeight(), mFreePaint);
   	}
}

