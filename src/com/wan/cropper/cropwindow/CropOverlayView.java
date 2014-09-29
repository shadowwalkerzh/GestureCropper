package com.wan.cropper.cropwindow;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.wan.cropper.CropImageView;
import com.wan.cropper.cropwindow.edge.Edge;
import com.wan.cropper.cropwindow.handle.Handle;
import com.wan.cropper.util.AspectRatioUtil;
import com.wan.cropper.util.HandleUtil;
import com.wan.cropper.util.PaintUtil;

public class CropOverlayView extends View {

	private static final int SNAP_RADIUS_DP = 6;
	private static final float DEFAULT_SHOW_GUIDELINES_LIMIT = 100;

	private static final float DEFAULT_CORNER_THICKNESS_DP = PaintUtil
			.getCornerThickness();
	private static final float DEFAULT_LINE_THICKNESS_DP = PaintUtil
			.getLineThickness();
	private static final float DEFAULT_CORNER_OFFSET_DP = (DEFAULT_CORNER_THICKNESS_DP / 2)
			- (DEFAULT_LINE_THICKNESS_DP / 2);
	private static final float DEFAULT_CORNER_EXTENSION_DP = DEFAULT_CORNER_THICKNESS_DP
			/ 2 + DEFAULT_CORNER_OFFSET_DP;
	private static final float DEFAULT_CORNER_LENGTH_DP = 20;

	private static final int GUIDELINES_OFF = 0;
	private static final int GUIDELINES_ON_TOUCH = 1;
	private static final int GUIDELINES_ON = 2;

	// The Paint used to draw the white rectangle around the crop area.
	private Paint mBorderPaint;

	// The Paint used to draw the guidelines within the crop area when pressed.
	private Paint mGuidelinePaint;

	// The Paint used to draw the corners of the Border
	private Paint mCornerPaint;

	// The Paint used to darken the surrounding areas outside the crop area.
	private Paint mBackgroundPaint;

	// The bounding box around the Bitmap that we are cropping.
	private Rect mBitmapRect;

	// The radius of the touch zone (in pixels) around a given Handle.
	private float mHandleRadius;

	private float mSnapRadius;

	private Pair<Float, Float> mTouchOffset;

	// The Handle that is currently pressed; null if no Handle is pressed.
	private Handle mPressedHandle;

	private boolean mFixAspectRatio = CropImageView.DEFAULT_FIXED_ASPECT_RATIO;

	// Floats to save the current aspect ratio of the image
	private int mAspectRatioX = CropImageView.DEFAULT_ASPECT_RATIO_X;
	private int mAspectRatioY = CropImageView.DEFAULT_ASPECT_RATIO_Y;

	// The aspect ratio that the crop area should maintain; this variable is
	// only used when mMaintainAspectRatio is true.
	private float mTargetAspectRatio = ((float) mAspectRatioX) / mAspectRatioY;

	// Instance variables for customizable attributes
	private int mGuidelines;

	// Whether the Crop View has been initialized for the first time
	private boolean initializedCropWindow = false;

	// Instance variables for the corner values
	private float mCornerExtension;
	private float mCornerOffset;
	private float mCornerLength;

	public CropOverlayView(Context context) {
		super(context);
		init(context);
	}

	public CropOverlayView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		initCropWindow(mBitmapRect);
	}

	@Override
	protected void onDraw(Canvas canvas) {

		super.onDraw(canvas);

		// Draw translucent background for the cropped area.
		drawBackground(canvas, mBitmapRect);
		if (showGuidelines()) {
			// Determines whether guidelines should be drawn or not
			if (mGuidelines == GUIDELINES_ON) {
				drawRuleOfThirdsGuidelines(canvas);
			} else if (mGuidelines == GUIDELINES_ON_TOUCH) {
				// Draw only when resizing
				if (mPressedHandle != null)
					drawRuleOfThirdsGuidelines(canvas);
			} else if (mGuidelines == GUIDELINES_OFF) {
				// Do nothing
			}
		}

		// Draws the main crop window border.
		canvas.drawRect(Edge.LEFT.getCoordinate(), Edge.TOP.getCoordinate(),
				Edge.RIGHT.getCoordinate(), Edge.BOTTOM.getCoordinate(),
				mBorderPaint);

		drawCorners(canvas);
	}

	/**
	 * 在这里监听拖拽、缩放、旋转等操作
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// If this View is not enabled, don't allow for touch interactions.
		if (!isEnabled()) {
			return false;
		}
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			onActionDown(event.getX(), event.getY());
			return true;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			getParent().requestDisallowInterceptTouchEvent(false);
			onActionUp();
			return true;
		case MotionEvent.ACTION_MOVE:
			onActionMove(event.getX(), event.getY());
			getParent().requestDisallowInterceptTouchEvent(true);
			return true;
		default:
			return false;
		}
	}

	public void setBitmapRect(Rect bitmapRect) {
		mBitmapRect = bitmapRect;
		initCropWindow(mBitmapRect);
	}

	public void resetCropOverlayView() {

		if (initializedCropWindow) {
			initCropWindow(mBitmapRect);
			invalidate();
		}
	}

	public void setGuidelines(int guidelines) {
		if (guidelines < 0 || guidelines > 2)
			throw new IllegalArgumentException(
					"Guideline value must be set between 0 and 2. See documentation.");
		else {
			mGuidelines = guidelines;

			if (initializedCropWindow) {
				initCropWindow(mBitmapRect);
				invalidate();
			}
		}
	}

	public void setFixedAspectRatio(boolean fixAspectRatio) {
		mFixAspectRatio = fixAspectRatio;

		if (initializedCropWindow) {
			initCropWindow(mBitmapRect);
			invalidate();
		}
	}

	public void setAspectRatioX(int aspectRatioX) {
		if (aspectRatioX <= 0)
			throw new IllegalArgumentException(
					"Cannot set aspect ratio value to a number less than or equal to 0.");
		else {
			mAspectRatioX = aspectRatioX;
			mTargetAspectRatio = ((float) mAspectRatioX) / mAspectRatioY;

			if (initializedCropWindow) {
				initCropWindow(mBitmapRect);
				invalidate();
			}
		}
	}

	public void setAspectRatioY(int aspectRatioY) {
		if (aspectRatioY <= 0)
			throw new IllegalArgumentException(
					"Cannot set aspect ratio value to a number less than or equal to 0.");
		else {
			mAspectRatioY = aspectRatioY;
			mTargetAspectRatio = ((float) mAspectRatioX) / mAspectRatioY;

			if (initializedCropWindow) {
				initCropWindow(mBitmapRect);
				invalidate();
			}
		}
	}

	public void setInitialAttributeValues(int guidelines,
			boolean fixAspectRatio, int aspectRatioX, int aspectRatioY) {
		if (guidelines < 0 || guidelines > 2)
			throw new IllegalArgumentException(
					"Guideline value must be set between 0 and 2. See documentation.");
		else
			mGuidelines = guidelines;

		mFixAspectRatio = fixAspectRatio;

		if (aspectRatioX <= 0)
			throw new IllegalArgumentException(
					"Cannot set aspect ratio value to a number less than or equal to 0.");
		else {
			mAspectRatioX = aspectRatioX;
			mTargetAspectRatio = ((float) mAspectRatioX) / mAspectRatioY;
		}

		if (aspectRatioY <= 0)
			throw new IllegalArgumentException(
					"Cannot set aspect ratio value to a number less than or equal to 0.");
		else {
			mAspectRatioY = aspectRatioY;
			mTargetAspectRatio = ((float) mAspectRatioX) / mAspectRatioY;
		}

	}

	private void init(Context context) {

		DisplayMetrics displayMetrics = context.getResources()
				.getDisplayMetrics();

		mHandleRadius = HandleUtil.getTargetRadius(context);

		mSnapRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				SNAP_RADIUS_DP, displayMetrics);

		mBorderPaint = PaintUtil.newBorderPaint(context);
		mGuidelinePaint = PaintUtil.newGuidelinePaint();
		mBackgroundPaint = PaintUtil.newBackgroundPaint(context);
		mCornerPaint = PaintUtil.newCornerPaint(context);

		// Sets the values for the corner sizes
		mCornerOffset = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				DEFAULT_CORNER_OFFSET_DP, displayMetrics);
		mCornerExtension = TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, DEFAULT_CORNER_EXTENSION_DP,
				displayMetrics);
		mCornerLength = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				DEFAULT_CORNER_LENGTH_DP, displayMetrics);

		// Sets guidelines to default until specified otherwise
		mGuidelines = CropImageView.DEFAULT_GUIDELINES;
	}

	private void initCropWindow(Rect bitmapRect) {

		// Tells the attribute functions the crop window has already been
		// initialized
		if (initializedCropWindow == false)
			initializedCropWindow = true;

		if (mFixAspectRatio) {

			// If the image aspect ratio is wider than the crop aspect ratio,
			// then the image height is the determining initial length. Else,
			// vice-versa.
			if (AspectRatioUtil.calculateAspectRatio(bitmapRect) > mTargetAspectRatio) {

				Edge.TOP.setCoordinate(bitmapRect.top);
				Edge.BOTTOM.setCoordinate(bitmapRect.bottom);

				final float centerX = getWidth() / 2f;

				// Limits the aspect ratio to no less than 40 wide or 40 tall
				final float cropWidth = Math
						.max(Edge.MIN_CROP_LENGTH_PX, AspectRatioUtil
								.calculateWidth(Edge.TOP.getCoordinate(),
										Edge.BOTTOM.getCoordinate(),
										mTargetAspectRatio));

				// Create new TargetAspectRatio if the original one does not fit
				// the screen
				if (cropWidth == Edge.MIN_CROP_LENGTH_PX)
					mTargetAspectRatio = (Edge.MIN_CROP_LENGTH_PX)
							/ (Edge.BOTTOM.getCoordinate() - Edge.TOP
									.getCoordinate());

				final float halfCropWidth = cropWidth / 2f;
				Edge.LEFT.setCoordinate(centerX - halfCropWidth);
				Edge.RIGHT.setCoordinate(centerX + halfCropWidth);

			} else {

				Edge.LEFT.setCoordinate(bitmapRect.left);
				Edge.RIGHT.setCoordinate(bitmapRect.right);

				final float centerY = getHeight() / 2f;

				// Limits the aspect ratio to no less than 40 wide or 40 tall
				final float cropHeight = Math
						.max(Edge.MIN_CROP_LENGTH_PX, AspectRatioUtil
								.calculateHeight(Edge.LEFT.getCoordinate(),
										Edge.RIGHT.getCoordinate(),
										mTargetAspectRatio));

				// Create new TargetAspectRatio if the original one does not fit
				// the screen
				if (cropHeight == Edge.MIN_CROP_LENGTH_PX)
					mTargetAspectRatio = (Edge.RIGHT.getCoordinate() - Edge.LEFT
							.getCoordinate()) / Edge.MIN_CROP_LENGTH_PX;

				final float halfCropHeight = cropHeight / 2f;
				Edge.TOP.setCoordinate(centerY - halfCropHeight);
				Edge.BOTTOM.setCoordinate(centerY + halfCropHeight);
			}

		} else { // ... do not fix aspect ratio...

			// Initialize crop window to have 10% padding w/ respect to image.
			final float horizontalPadding = 0.1f * bitmapRect.width();
			final float verticalPadding = 0.1f * bitmapRect.height();

			Edge.LEFT.setCoordinate(bitmapRect.left + horizontalPadding);
			Edge.TOP.setCoordinate(bitmapRect.top + verticalPadding);
			Edge.RIGHT.setCoordinate(bitmapRect.right - horizontalPadding);
			Edge.BOTTOM.setCoordinate(bitmapRect.bottom - verticalPadding);
		}
	}

	public static boolean showGuidelines() {
		if ((Math.abs(Edge.LEFT.getCoordinate() - Edge.RIGHT.getCoordinate()) < DEFAULT_SHOW_GUIDELINES_LIMIT)
				|| (Math.abs(Edge.TOP.getCoordinate()
						- Edge.BOTTOM.getCoordinate()) < DEFAULT_SHOW_GUIDELINES_LIMIT))
			return false;
		else
			return true;
	}

	private void drawRuleOfThirdsGuidelines(Canvas canvas) {

		final float left = Edge.LEFT.getCoordinate();
		final float top = Edge.TOP.getCoordinate();
		final float right = Edge.RIGHT.getCoordinate();
		final float bottom = Edge.BOTTOM.getCoordinate();

		// Draw vertical guidelines.
		final float oneThirdCropWidth = Edge.getWidth() / 3;

		final float x1 = left + oneThirdCropWidth;
		canvas.drawLine(x1, top, x1, bottom, mGuidelinePaint);
		final float x2 = right - oneThirdCropWidth;
		canvas.drawLine(x2, top, x2, bottom, mGuidelinePaint);

		// Draw horizontal guidelines.
		final float oneThirdCropHeight = Edge.getHeight() / 3;

		final float y1 = top + oneThirdCropHeight;
		canvas.drawLine(left, y1, right, y1, mGuidelinePaint);
		final float y2 = bottom - oneThirdCropHeight;
		canvas.drawLine(left, y2, right, y2, mGuidelinePaint);
	}

	private void drawBackground(Canvas canvas, Rect bitmapRect) {

		final float left = Edge.LEFT.getCoordinate();
		final float top = Edge.TOP.getCoordinate();
		final float right = Edge.RIGHT.getCoordinate();
		final float bottom = Edge.BOTTOM.getCoordinate();

		// Draw "top", "bottom", "left", then "right" quadrants.
		canvas.drawRect(bitmapRect.left, bitmapRect.top, bitmapRect.right, top,
				mBackgroundPaint);
		canvas.drawRect(bitmapRect.left, bottom, bitmapRect.right,
				bitmapRect.bottom, mBackgroundPaint);
		canvas.drawRect(bitmapRect.left, top, left, bottom, mBackgroundPaint);
		canvas.drawRect(right, top, bitmapRect.right, bottom, mBackgroundPaint);
	}

	private void drawCorners(Canvas canvas) {
		final float left = Edge.LEFT.getCoordinate();
		final float top = Edge.TOP.getCoordinate();
		final float right = Edge.RIGHT.getCoordinate();
		final float bottom = Edge.BOTTOM.getCoordinate();

		// Top left
		canvas.drawLine(left - mCornerOffset, top - mCornerExtension, left
				- mCornerOffset, top + mCornerLength, mCornerPaint);
		canvas.drawLine(left, top - mCornerOffset, left + mCornerLength, top
				- mCornerOffset, mCornerPaint);
		// Top right
		canvas.drawLine(right + mCornerOffset, top - mCornerExtension, right
				+ mCornerOffset, top + mCornerLength, mCornerPaint);
		canvas.drawLine(right, top - mCornerOffset, right - mCornerLength, top
				- mCornerOffset, mCornerPaint);

		// Bottom left
		canvas.drawLine(left - mCornerOffset, bottom + mCornerExtension, left
				- mCornerOffset, bottom - mCornerLength, mCornerPaint);
		canvas.drawLine(left, bottom + mCornerOffset, left + mCornerLength,
				bottom + mCornerOffset, mCornerPaint);

		// Bottom left
		canvas.drawLine(right + mCornerOffset, bottom + mCornerExtension, right
				+ mCornerOffset, bottom - mCornerLength, mCornerPaint);
		canvas.drawLine(right, bottom + mCornerOffset, right - mCornerLength,
				bottom + mCornerOffset, mCornerPaint);

	}

	/**
	 * Handles a {@link MotionEvent#ACTION_DOWN} event.
	 * @param x
	 *            the x-coordinate of the down action
	 * @param y
	 *            the y-coordinate of the down action
	 */
	private void onActionDown(float x, float y) {

		final float left = Edge.LEFT.getCoordinate();
		final float top = Edge.TOP.getCoordinate();
		final float right = Edge.RIGHT.getCoordinate();
		final float bottom = Edge.BOTTOM.getCoordinate();

		mPressedHandle = HandleUtil.getPressedHandle(x, y, left, top, right,
				bottom, mHandleRadius);

		if (mPressedHandle == null)
			return;

		// Calculate the offset of the touch point from the precise location
		// of the handle. Save these values in a member variable since we want
		// to maintain this offset as we drag the handle.
		mTouchOffset = HandleUtil.getOffset(mPressedHandle, x, y, left, top,
				right, bottom);

		invalidate();
	}

	/**
	 * Handles a {@link MotionEvent#ACTION_UP} or
	 * {@link MotionEvent#ACTION_CANCEL} event.
	 */
	private void onActionUp() {

		if (mPressedHandle == null)
			return;

		mPressedHandle = null;

		invalidate();
	}

	/**
	 * Handles a {@link MotionEvent#ACTION_MOVE} event.
	 * 
	 * @param x
	 *            the x-coordinate of the move event
	 * @param y
	 *            the y-coordinate of the move event
	 */
	private void onActionMove(float x, float y) {
		if (mPressedHandle == null)
			return;
		// Adjust the coordinates for the finger position's offset (i.e. the
		// distance from the initial touch to the precise handle location).
		// We want to maintain the initial touch's distance to the pressed
		// handle so that the crop window size does not "jump".
		x += mTouchOffset.first;
		y += mTouchOffset.second;
		// Calculate the new crop window size/position.
		if (mFixAspectRatio) {
			mPressedHandle.updateCropWindow(x, y, mTargetAspectRatio,
					mBitmapRect, mSnapRadius);
		} else {
			mPressedHandle.updateCropWindow(x, y, mBitmapRect, mSnapRadius);
		}
		invalidate();
	}
}
