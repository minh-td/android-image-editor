package com.android.image.edit.scroll;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class ImageScrollPresentState implements ImageScrollState {
	
	private Rect displayRect = new Rect(); //rect we display to
	private Rect scrollRect = new Rect(); //rect we scroll over our bitmap with
	private int scrollRectX = 0; //current left location of scroll rect
	private int scrollRectY = 0; //current top location of scroll rect
	private float scrollByX = 0; //x amount to scroll by
	private float scrollByY = 0; //y amount to scroll by

	@Override
	public void drawBitmap(Canvas canvas, Bitmap bitmap, Paint paint, int viewWidth, int viewHeight) {
		displayRect.set(0, 0, viewWidth, viewHeight);
		// Our move updates are calculated in ACTION_MOVE in the opposite direction
		// from how we want to move the scroll rect. Think of this as dragging to
		// the left being the same as sliding the scroll rect to the right.
		int newScrollRectX = scrollRectX - (int)scrollByX;
		int newScrollRectY = scrollRectY - (int)scrollByY;

		// Don't scroll off the left or right edges of the bitmap.
		if (newScrollRectX < 0)
			newScrollRectX = 0;
		else if (newScrollRectX > (bitmap.getWidth() - viewWidth))
			newScrollRectX = (bitmap.getWidth() - viewWidth);

		// Don't scroll off the top or bottom edges of the bitmap.
		if (newScrollRectY < 0)
			newScrollRectY = 0;
		else if (newScrollRectY > (bitmap.getHeight() - viewHeight))
			newScrollRectY = (bitmap.getHeight() - viewHeight);

		// We have our updated scroll rect coordinates, set them and draw.
		scrollRect.set(newScrollRectX, newScrollRectY, 
			newScrollRectX + viewWidth, newScrollRectY + viewHeight);
		canvas.drawBitmap(bitmap, scrollRect, displayRect, paint);

		// Reset current scroll coordinates to reflect the latest updates, 
		// so we can repeat this update process.
		scrollRectX = newScrollRectX;
		scrollRectY = newScrollRectY;
		scrollByX = 0;
		scrollByY = 0;
	}

	@Override
	public void toAbsoluteCoordinates(float[] relativeCoordinates) {
		for (int i = 0; i < relativeCoordinates.length; i+=2) {
			relativeCoordinates[i] += scrollRect.left;
			relativeCoordinates[i+1] += scrollRect.top;
		}
	}

	@Override
	public void setScrollByX(float scrollByX) {
		this.scrollByX = scrollByX;
	}

	@Override
	public void setScrollByY(float scrollByY) {
		this.scrollByY = scrollByY;
	}

}
