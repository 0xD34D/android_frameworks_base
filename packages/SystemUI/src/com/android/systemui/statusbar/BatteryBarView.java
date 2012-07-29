/**
 * 
 */
package com.android.systemui.statusbar;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author lithium
 *
 */
public class BatteryBarView extends View {

	private boolean mIsCentered;
	private float mLevel;
	private Paint mFullColor;
	private Paint mMediumColor;
	private Paint mLowColor;
	
	/**
	 * @param context
	 */
	public BatteryBarView(Context context) {
		this(context, null);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public BatteryBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		Resources res = context.getResources();
		mFullColor = new Paint();
		mFullColor.setColor(res.getColor(android.R.color.holo_blue_light));
		mMediumColor = new Paint();
		mMediumColor.setColor(0xFFFF8000);
		mLowColor = new Paint();
		mLowColor.setColor(0xFFFF0000);
		mLevel = 1.0f;
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public BatteryBarView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public void setLevel(int level) {
		mLevel = (float)level / 100.0f;
		this.invalidate();
	}

	/* (non-Javadoc)
	 * @see android.view.View#draw(android.graphics.Canvas)
	 */
	@Override
	public void draw(Canvas canvas) {
		canvas.drawColor(0x00FFFFFF);
		Paint p = null;
		if(mLevel > 0.30)
			p = mFullColor;
		else if (mLevel > 0.10)
			p = mMediumColor;
		else
			p = mLowColor;
		
		p.setStrokeWidth(this.getHeight());
		canvas.drawRect(0, 0, (float)this.getWidth() * mLevel, this.getHeight(), p);
	}
}

