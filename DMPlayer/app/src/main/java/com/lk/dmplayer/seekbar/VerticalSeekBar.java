package com.lk.dmplayer.seekbar;

// obtained from http://stackoverflow.com/questions/4892179/how-can-i-get-a-working-vertical-seekbar-in-android

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.SeekBar;

public class VerticalSeekBar extends SeekBar
{

	private Drawable mThumb;

	public VerticalSeekBar(Context context)
	{
		super(context);

//		setThumb(context.getResources().getDrawable(R.drawable.eq_control_thumb));
	}

	public VerticalSeekBar(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
//		setThumb(context.getResources().getDrawable(R.drawable.eq_control_thumb));
	}

	public VerticalSeekBar(Context context, AttributeSet attrs)
	{
		super(context, attrs);
//		setThumb(context.getResources().getDrawable(R.drawable.eq_control_thumb));
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		super.onSizeChanged(h, w, oldh, oldw);
	}

	@Override
	public synchronized void setProgress(int progress)
	{
		super.setProgress(progress);
		onSizeChanged(getWidth(), getHeight(), 0, 0);
	}

	@Override
	protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(heightMeasureSpec, widthMeasureSpec);
		setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
	}

	@Override
	public void setThumb(Drawable thumb)
	{
		super.setThumb(thumb);
		mThumb = thumb;
	}

	public Drawable getSeekBarThumb()
	{
		return mThumb;
	}

	@Override
	protected void onDraw(Canvas c)
	{
		c.rotate(-90);
		c.translate(-getHeight(), 0);

		super.onDraw(c);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event)
	{
		ViewParent parent = getParent();
		if (parent != null && parent instanceof ViewGroup)
		{
			if (!((ViewGroup) parent).isEnabled())
			{
				return super.dispatchTouchEvent(event);
			}
		}
		setEnabled(true);
		return super.dispatchTouchEvent(event);
	}
}
