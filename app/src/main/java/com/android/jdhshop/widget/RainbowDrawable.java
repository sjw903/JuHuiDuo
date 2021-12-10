package com.android.jdhshop.widget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static android.graphics.Canvas.ALL_SAVE_FLAG;

/**
 * @author dragon
 */
public class RainbowDrawable extends Drawable implements Runnable {
	private Paint bgPaint = new Paint();
	private Paint rainbowPaint = new Paint();
	private Paint paintLayer = new Paint();
	private Paint paintMask = new Paint();
	private Paint paintGradient = new Paint();
	private Path rainbowPath = new Path();
	private Path bgPath = new Path();
	private RectF drawableRect = new RectF();
	private int degree = 0;

	private final int timeStep = 33;
	private int degreeStep = 1;
	private boolean requestPlay = true;

	/**
	 * user can change.
	 */
	private int radius = 60;
	private int strokeWidth = 20;
	private int durationOnCircle = 2000;
	@ColorInt
	private int[] colors;
	@FloatRange(from = 0.0f, to = 1.0f)
	private float[] positions;
	@ColorInt
	private int bgColor;

	private RainbowDrawable() {
		init();
	}

	private void init() {
		paintGradient.setColor(Color.WHITE);
		paintGradient.setStyle(Paint.Style.FILL_AND_STROKE);
		paintMask.setColor(Color.WHITE);
		paintMask.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
		bgPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		bgPaint.setAntiAlias(true);
		paintLayer.setColor(Color.WHITE);
		rainbowPaint.setColor(Color.WHITE);
		rainbowPaint.setStyle(Paint.Style.STROKE);
		rainbowPaint.setAntiAlias(true);
	}

	@Override
	public boolean setVisible(boolean visible, boolean restart) {
		boolean changed = super.setVisible(visible, restart);
		if (changed) {
			if (visible) {
				scheduleSelf(this, SystemClock.uptimeMillis() + timeStep);
			} else {
				unscheduleSelf(this);
			}
		}
		return changed;
	}

	@Override
	public void draw(@NonNull Canvas canvas) {
		Rect rect = getBounds();
		if (bgColor != 0) {
			bgPaint.setColor(bgColor);
			bgPaint.setStrokeWidth(strokeWidth);
			canvas.drawPath(bgPath, bgPaint);
			canvas.saveLayer(drawableRect, paintLayer, ALL_SAVE_FLAG);
		}
		canvas.rotate(degree, rect.width() / 2, rect.height() / 2);
		canvas.drawCircle(rect.width() / 2, rect.height() / 2, rect.width(), paintGradient);
		canvas.rotate(-degree, rect.width() / 2, rect.height() / 2);
		{
			rainbowPaint.setStrokeWidth(strokeWidth);
			canvas.saveLayer(drawableRect, paintMask, ALL_SAVE_FLAG);
			canvas.drawPath(rainbowPath, rainbowPaint);
			canvas.restore();
		}
		if (bgColor != 0) {
			canvas.restore();
		}
		if (requestPlay) {
			requestPlay = false;
			scheduleSelf(this, SystemClock.uptimeMillis() + timeStep);
		}
	}

	@Override
	protected void onBoundsChange(Rect bounds) {
		int w = bounds.width();
		int h = bounds.height();
		float offset = strokeWidth / 2;
		degreeStep = (int) (timeStep / (durationOnCircle / 360.0));
		drawableRect.set(0, 0, w, h);
		RectF rectF = new RectF(drawableRect.left + offset, drawableRect.top + offset, drawableRect.right - offset, drawableRect.bottom - offset);
		rainbowPath.reset();
		rainbowPath.addRoundRect(rectF, radius, radius, Path.Direction.CCW);
		bgPath.reset();
		RectF rectF2 = new RectF(rectF.left + strokeWidth, rectF.top + strokeWidth, rectF.right - strokeWidth, rectF.bottom - strokeWidth);
		bgPath.addRoundRect(rectF2, radius - strokeWidth, radius - strokeWidth, Path.Direction.CCW);
		paintGradient.setShader(new SweepGradient(w / 2, h / 2, colors, positions));
	}

	@Override
	public void setAlpha(int alpha) {
		//Do nothing
	}

	@Override
	public void setColorFilter(@Nullable ColorFilter colorFilter) {
		//Do nothing
	}

	@Override
	public int getOpacity() {
		return PixelFormat.TRANSPARENT;
	}


	@Override
	public void run() {
		degree += degreeStep;
		degree %= 360;
		invalidateSelf();
		scheduleSelf(this, SystemClock.uptimeMillis() + timeStep);
	}


	public static final class Builder {
		@ColorInt
		int[] colors = new int[]{Color.BLUE, Color.RED, Color.GREEN, Color.RED, Color.BLUE};
		@FloatRange(from = 0.0f, to = 1.0f)
		float[] positions = null;

		int radius = 20;
		int strokeWidth = 10;
		int duration = 1000;

		@ColorInt
		int backgroundColor = 0;

		public Builder setGradientColorAndPosition(@ColorInt int[] colors, @FloatRange(from = 0.0f, to = 1.0f) float[] positions) {
			if (colors == null || colors.length <= 0) {
				return this;
			}
			this.colors = colors;
			if (positions != null && positions.length != colors.length) {
				return this;
			}
			this.positions = positions;
			return this;
		}

		public Builder setBackgroundColor(@ColorInt int color) {
			this.backgroundColor = color;
			return this;
		}

		public Builder setStrokeWidth(int strokeWidth) {
			this.strokeWidth = strokeWidth;
			if (radius < 2 * strokeWidth) {
				radius = 2 * strokeWidth;
			}
			return this;
		}

		public Builder setRadius(int radius) {
			this.radius = radius;
			if (radius < 2 * strokeWidth) {
				strokeWidth = radius / 2;
			}
			return this;
		}

		public Builder setDuration(int duration) {
			this.duration = duration;
			return this;
		}

		public RainbowDrawable build() {
			RainbowDrawable rainbowDrawable = new RainbowDrawable();
			rainbowDrawable.strokeWidth = strokeWidth;
			rainbowDrawable.radius = radius;
			rainbowDrawable.durationOnCircle = duration;
			rainbowDrawable.bgColor = backgroundColor;
			rainbowDrawable.colors = colors;
			rainbowDrawable.positions = positions;
			return rainbowDrawable;
		}
	}
}
